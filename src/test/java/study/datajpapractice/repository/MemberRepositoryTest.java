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

    @Autowired MemberRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 20, null);
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(savedMember.getId()).orElse(null);

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
        assertThat(members.size()).isEqualTo(memberJpaRepository.count());

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
    }
}