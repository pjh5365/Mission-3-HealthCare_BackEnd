package kakao.mission3healthcare_backend.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kakao.mission3healthcare_backend.auth.domain.CustomAuthenticationEntryPoint;
import kakao.mission3healthcare_backend.auth.domain.filter.LoginFilter;
import kakao.mission3healthcare_backend.auth.domain.handler.CustomAccessDeniedHandler;
import kakao.mission3healthcare_backend.auth.domain.handler.LogOutSuccessHandler;
import kakao.mission3healthcare_backend.auth.domain.handler.LoginFailureHandler;
import kakao.mission3healthcare_backend.auth.domain.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;

/**
 * 스프링 시큐리티 설정파일
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;
	private final LogOutSuccessHandler logOutSuccessHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth ->
				// 로그인, 로그아웃, 회원가입은 누구나 접근가능
				auth.requestMatchers("/api/login", "/api/logout", "/api/users", "/error")
						.permitAll()
						.requestMatchers(PathRequest.toH2Console()).permitAll() // 개발단계에선 h2-console 허용
						.anyRequest()
						.authenticated()
		);

		// csrf는 사용하지 않음
		http.csrf(AbstractHttpConfigurer::disable);

		http.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // h2-console 허용

		http.logout(logout -> logout
				.logoutUrl("/api/logout")
				.logoutSuccessHandler(logOutSuccessHandler));

		http.exceptionHandling(handler -> handler
				.authenticationEntryPoint(customAuthenticationEntryPoint)
				.accessDeniedHandler(customAccessDeniedHandler));

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
			Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public LoginFilter loginFilter() throws Exception {
		LoginFilter loginFilter = new LoginFilter();
		loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
		loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
		loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
		return loginFilter;
	}
}
