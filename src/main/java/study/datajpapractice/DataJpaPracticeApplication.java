package study.datajpapractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaPracticeApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
//		return new AuditorAware<String>() {
//
//			@Override
//			public Optional<String> getCurrentAuditor() {
//				return Optional.of(UUID.randomUUID().toString());
//			}
//		};
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
