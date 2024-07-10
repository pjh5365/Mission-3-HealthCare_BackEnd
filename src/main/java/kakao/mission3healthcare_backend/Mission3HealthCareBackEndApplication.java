package kakao.mission3healthcare_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Mission3HealthCareBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mission3HealthCareBackEndApplication.class, args);
	}

}
