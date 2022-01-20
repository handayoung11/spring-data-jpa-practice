package study.datajpapractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpapractice.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
