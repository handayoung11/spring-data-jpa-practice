package study.datajpapractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpapractice.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
