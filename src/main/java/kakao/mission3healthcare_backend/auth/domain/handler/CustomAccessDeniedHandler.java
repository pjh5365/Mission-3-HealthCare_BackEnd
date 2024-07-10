package kakao.mission3healthcare_backend.auth.domain.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 커스텀 AccessDeniedHandler
 *
 * @author : parkjihyeok
 * @since : 2024/07/07
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		// TODO: 2024/07/7 로그인 페이지 혹은 권한없음 페이지로 변경
		response.sendRedirect("https://www.kakaocorp.com/");
	}
}
