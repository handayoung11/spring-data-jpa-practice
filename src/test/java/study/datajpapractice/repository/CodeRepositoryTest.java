package study.datajpapractice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpapractice.entity.Code;

import java.util.UUID;

@SpringBootTest
class CodeRepositoryTest {
    @Autowired CodeRepository codeRepository;

    @Test
    public void save() {
        Code code = new Code(UUID.randomUUID() + "");
        codeRepository.save(code);
    }
}