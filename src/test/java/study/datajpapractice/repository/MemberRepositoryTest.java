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
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 20, null);
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).orElse(null);

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1", 20, null);
        Member member2 = new Member("member2", 20, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).orElse(null);
        Member findMember2 = memberRepository.findById(member2.getId()).orElse(null);
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        // 리스트조회 및 카운트 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(memberRepository.count());

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("Test", 10, null);
        Member member2 = new Member("Test", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("Test", 15);

        assertThat(members.get(0).getUsername()).isEqualTo("Test");
        assertThat(members.get(0).getAge()).isEqualTo(20);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void findByUsername() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 10, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsername("AAA");
        assertThat(members.get(0)).isEqualTo(member1);
    }

    @Test
    public void findByNameAndAge() {
        Member member1 = new Member("AAA", 10, null);
        memberRepository.save(member1);

        List<Member> members = memberRepository.findByNameAndAge("AAA", 10);
        assertThat(members.get(0)).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        assertThat(usernameList.contains("AAA")).isTrue();
        assertThat(usernameList.contains("BBB")).isTrue();
    }

}