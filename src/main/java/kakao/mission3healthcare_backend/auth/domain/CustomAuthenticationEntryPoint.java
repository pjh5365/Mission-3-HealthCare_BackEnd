package kakao.mission3healthcare_backend.auth.domain;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 인증되지 않은 사용자가 접근했을 시 처리할 로직
 *
 * @author : parkjihyeok
 * @since : 2024/07/07
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		// TODO: 2024/07/7 로그인 페이지 혹은 권한없음 페이지로 변경
		response.sendRedirect("https://www.kakaocorp.com/");
	}
}
