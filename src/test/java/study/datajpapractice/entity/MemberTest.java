package study.datajpapractice.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpapractice.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@Transactional
@SpringBootTest
class MemberTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        em.persist(team1);
        em.persist(team2);

        Member member1 = new Member("member1", 10, team1);
        Member member2 = new Member("member2", 20, team1);
        Member member3 = new Member("member3", 30, team2);
        Member member4 = new Member("member4", 40, team2);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

    @Test
    public void udpatedAndCreatedDateTest() {
        Member member = new Member("member1", 10, null);
        memberRepository.save(member);

        member.updateUsername("member2");
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        assertThat(findMember.getCreatedDate()).isNotNull();
        assertThat(findMember.getCreatedDate()).isEqualTo(member.getCreatedDate());
        assertThat(findMember.getModifiedDate()).isAfter(findMember.getCreatedDate());
    }

    @Test
    public void creatorAndModifierTest() {
        Member member = new Member("member1", 10, null);
        memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        assertThat(findMember.getCreatedBy()).isNotNull();
        assertThat(findMember.getModifiedBy()).isEqualTo(member.getCreatedBy());
    }
}