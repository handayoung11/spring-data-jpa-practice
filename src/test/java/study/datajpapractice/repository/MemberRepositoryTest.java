package study.datajpapractice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpapractice.dto.Mem$Team;
import study.datajpapractice.dto.MemberDTO;
import study.datajpapractice.dto.MemberInfoDTO;
import study.datajpapractice.dto.UsernameOnly;
import study.datajpapractice.entity.Member;
import study.datajpapractice.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback(false)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

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
        assertThat(members.contains(member1)).isTrue();
    }

    @Test
    public void findByNameAndAge() {
        Member member1 = new Member("AAA", 10, null);
        memberRepository.save(member1);

        List<Member> members = memberRepository.findByNameAndAge("AAA", 10);
        assertThat(members.contains(member1)).isTrue();
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

    @Test
    public void findMemberDTOList() {
        Team team = new Team("프로그래밍부");
        Member member = new Member("AAA", 10, team);

        teamRepository.save(team);
        memberRepository.save(member);

        List<MemberDTO> members = memberRepository.findMemberDTOList();
        assertThat(members.contains(new MemberDTO(member.getId(), member.getUsername(), team.getName()))).isTrue();
    }

    @Test
    public void findByNames() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 10, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        assertThat(members.contains(member1) && members.contains(member2)).isTrue();
    }

    @Test
    public void returnTypeTest() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 10, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findListByUsername("AAA");
        Member findMember1 = memberRepository.findMemberByUsername("AAA");
        Member findMember2 = memberRepository.findOptionalByUsername("AAA").orElseThrow(NullPointerException::new);
        assertThat(members.contains(findMember1) && members.contains(findMember2)).isTrue();
    }

    @Test
    public void findByAge() {
        int age = 10, page = 0, size = 3;

        memberRepository.save(new Member("member1", age, null));
        memberRepository.save(new Member("member2", age, null));
        memberRepository.save(new Member("member3", age, null));
        memberRepository.save(new Member("member4", age, null));
        memberRepository.save(new Member("member5", age, null));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> pagedMembers = memberRepository.findByAge(age, pageRequest);
        Page<MemberDTO> memberDTOPage = pagedMembers.map(m -> new MemberDTO(m.getId(), m.getUsername(), null));
        Page<String> memberNamePage = pagedMembers.map(m -> m.getUsername());


        assertThat(pagedMembers.getContent().size()).isEqualTo(size); // paging result
        assertThat(pagedMembers.getTotalElements() >= 5).isTrue(); // total elements
        assertThat(pagedMembers.getNumber()).isEqualTo(page); // page number
        assertThat(pagedMembers.getTotalPages() >= 2).isTrue(); // total pages
        assertThat(pagedMembers.isFirst()).isTrue();
        assertThat(pagedMembers.hasNext()).isTrue();
    }

    @Test
    public void plusAgeOfAllMems() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 20, null));
        Member member3 = memberRepository.save(new Member("member3", 30, null));
        memberRepository.save(new Member("member4", 40, null));
        memberRepository.save(new Member("member5", 50, null));

        int resultCount = memberRepository.plusAgeOfAllMems(30);
        Member findMember = memberRepository.findById(member3.getId()).orElse(null);
        assertThat(findMember.getAge()).isEqualTo(31);
        assertThat(resultCount >= 3).isTrue();
    }

    @Test
    public void fetchTeam() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        memberRepository.flush();
        em.clear();

        List<Member> members = memberRepository.fetchTeam();
        assertThat(members.stream()
                .filter(m -> m.getId() == member1.getId()).findFirst().orElse(null)
                .getTeam().getName())
                .isEqualTo("teamA");
        assertThat(members.stream()
                .filter(m -> m.getId() == member2.getId()).findFirst().orElse(null)
                .getTeam().getName())
                .isEqualTo("teamB");
    }

    @Test
    public void findTeamByUsername() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        memberRepository.flush();
        em.clear();

        List<Member> members = memberRepository.findTeamByUsername("member1");
        assertThat(members.stream()
                .filter(m -> m.getId() == member1.getId()).findFirst().orElse(null)
                .getTeam().getName())
                .isEqualTo("teamA");
        assertThat(members.stream()
                .filter(m -> m.getId() == member2.getId()).findFirst().orElse(null)
                .getTeam().getName())
                .isEqualTo("teamB");
    }

    @Test
    public void findRoById() {
        Member member1 = new Member("member1", 10, null);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findROById(member1.getId()).orElse(null);
        findMember.updateUsername("member2");
        em.flush();
    }

    @Test
    public void findLockById() {
        // Lock 기능은 심화기능. 추후 필요할 때 공부하도록 하자
        Member member1 = memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();

        memberRepository.findLockById(member1.getId()).orElse(null);
    }

    @Test
    public void findMemberCustom() {
        Member member = memberRepository.save(new Member("member1", 10, null));

        List<Member> members = memberRepository.findMemberCustom();
        assertThat(members.contains(member)).isTrue();
    }

    @Test
    public void specBasic() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();

        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);
        assertThat(result.stream().map(Member::getId).collect(Collectors.toList()).contains(m1.getId())).isTrue();
    }

    @Test
    public void queryByExample() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 10, teamA);
        em.persist(m1);
        em.persist(new Member("m2", 10, teamA));
        em.flush();
        em.clear();

        //Probe 생성
        Team team = new Team("teamA");
        Member member = new Member("m1", 0, team);

        //ExampleMatcher 생성, age property 무시
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> members = memberRepository.findAll(example);

        assertThat(members.stream().filter(m -> m.getId() == m1.getId()).count() == 1).isTrue();
    }

    @Test
    public void findUsernameByUsername() {
        Member m1 = new Member("m1", 10, null);
        em.persist(m1);

        List<UsernameOnly> members = memberRepository.findUnByUsername("m1");
        assertThat(members.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void findMemberInfoDTOByUn() {
        Member m1 = new Member("m1", 10, null);
        em.persist(m1);

        List<MemberInfoDTO> members = memberRepository.findMemberInfoDTOByUsername("m1");
        assertThat(members.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void findProjectionByUsernmae() {
        Member m1 = new Member("m1", 10, null);
        em.persist(m1);

        List<MemberInfoDTO> members1 = memberRepository.findProjectionByUsername("m1", MemberInfoDTO.class);
        List<UsernameOnly> members2 = memberRepository.findProjectionByUsername("m1", UsernameOnly.class);
        assertThat(members1.get(0).getUsername()).isEqualTo("m1");
        assertThat(members2.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void findProjectionWithAssociated() {
        Team t1 = new Team("t1");
        em.persist(t1);

        Member m1 = new Member("m1", 10, t1);
        em.persist(m1);

        List<Mem$Team> members = memberRepository.findProjectionByUsername("m1", Mem$Team.class);
        Mem$Team member = members.get(0);
        assertThat(member.getUsername()).isEqualTo("m1");
        assertThat(member.getTeam().getName()).isEqualTo("t1");
    }
}