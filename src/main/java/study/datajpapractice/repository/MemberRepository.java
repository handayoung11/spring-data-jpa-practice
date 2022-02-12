package study.datajpapractice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpapractice.dto.MemberDTO;
import study.datajpapractice.dto.MemberInfoDTO;
import study.datajpapractice.dto.UsernameOnly;
import study.datajpapractice.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository
        extends JpaRepository<Member, Long>
        , MemberCustomRepository
        , JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findByNameAndAge(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpapractice.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTOList();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); // Collection
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional

    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int plusAgeOfAllMems(@Param("age") int age);

    @Query("select m from Member m")
    @EntityGraph(attributePaths = {"team"})
    List<Member> fetchTeam();

    @EntityGraph("Members.all")
    List<Member> findTeamByUsername(@Param("username") String username);

    @QueryHints(@QueryHint(name = "org.hibernate.readonly", value = "true"))
    Optional<Member> findROById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Member> findLockById(Long id);

    List<UsernameOnly> findUnByUsername(@Param("username") String username);
    List<MemberInfoDTO> findMemberInfoDTOByUsername(@Param("username") String username);
}
