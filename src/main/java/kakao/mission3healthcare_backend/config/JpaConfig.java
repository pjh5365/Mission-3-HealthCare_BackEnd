package kakao.mission3healthcare_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import kakao.mission3healthcare_backend.common.AuditorAwareImpl;

/**
 * JPA Auditor를 사용하기 위한 설정클래스
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@Configuration
public class JpaConfig {

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}
}
