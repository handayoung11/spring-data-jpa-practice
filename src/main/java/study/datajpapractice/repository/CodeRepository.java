package study.datajpapractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpapractice.entity.Code;

public interface CodeRepository extends JpaRepository<Code, String> {
}
