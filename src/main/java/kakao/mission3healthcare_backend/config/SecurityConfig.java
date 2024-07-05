package kakao.mission3healthcare_backend.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 스프링 시큐리티 설정파일
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth ->
				// 로그인, 로그아웃, 회원가입은 누구나 접근가능
				auth.requestMatchers("/api/login", "/api/logout", "/api/users")
						.permitAll()
						.requestMatchers(PathRequest.toH2Console()).permitAll() // 개발단계에선 h2-console 허용
						.anyRequest()
						.authenticated()
		);

		// csrf는 사용하지 않음
		http.csrf(AbstractHttpConfigurer::disable);

		http.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // h2-console 허용

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
