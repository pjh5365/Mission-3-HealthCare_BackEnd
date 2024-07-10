package kakao.mission3healthcare_backend.auth.domain.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 로그인 성공 Handler
 *
 * @author : parkjihyeok
 * @since : 2024/07/06
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		HttpSession session = request.getSession();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		session.setMaxInactiveInterval(10 * 60);  // 아무동작도 하지않으면 세션은 10분 후 만료
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

		// TODO: 2024/07/6 프론트엔드 서버 주소로 변경하기
		setDefaultTargetUrl("https://www.kakaocorp.com/");
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
