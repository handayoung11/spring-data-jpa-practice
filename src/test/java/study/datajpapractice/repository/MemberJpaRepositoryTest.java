package study.datajpapractice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpapractice.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback(false)
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 20, null);
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1", 20, null);
        Member member2 = new Member("member2", 20, null);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).orElse(null);
        Member findMember2 = memberJpaRepository.findById(member2.getId()).orElse(null);
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        // 리스트조회 및 카운트 검증
        List<Member> members = memberJpaRepository.findAll();
        assertThat((long) members.size()).isEqualTo(memberJpaRepository.count());

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
    }
    
    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("Test", 10, null);
        Member member2 = new Member("Test", 20, null);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThan("Test", 15);

        assertThat(members.get(0).getUsername()).isEqualTo("Test");
        assertThat(members.get(0).getAge()).isEqualTo(20);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void findByUsername() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> members = memberJpaRepository.findByUsername("AAA");
        assertThat(members.get(0)).isEqualTo(member1);
    }

    @Test
    public void pageByAge() {
        int age = 10, offset = 0, limit = 3;

        memberJpaRepository.save(new Member("member1", age, null));
        memberJpaRepository.save(new Member("member2", age, null));
        memberJpaRepository.save(new Member("member3", age, null));
        memberJpaRepository.save(new Member("member4", age, null));
        memberJpaRepository.save(new Member("member5", age, null));

        List<Member> members = memberJpaRepository.pageByAge(age, offset, limit);
        long count = memberJpaRepository.countByAge(age);

        assertThat(members.size()).isEqualTo(limit);
        assertThat(count >= 5).isTrue();
    }

    @Test
    public void plusAgeOfAllMems() {
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 20, null));
        memberJpaRepository.save(new Member("member3", 30, null));
        memberJpaRepository.save(new Member("member4", 40, null));
        memberJpaRepository.save(new Member("member5", 50, null));

        int resultCount = memberJpaRepository.plusAgeOfAllMems(30);
        assertThat(resultCount >= 3).isTrue();
    }
}