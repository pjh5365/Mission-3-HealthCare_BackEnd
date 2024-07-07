package kakao.mission3healthcare_backend.auth.domain.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그아웃 성공 Handler
 *
 * @author : parkjihyeok
 * @since : 2024/07/07
 */
@Slf4j
@Component
public class LogOutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		if (authentication != null) {
			log.info("[로그아웃] - [{}] 가 로그아웃하였습니다.", authentication.getName());
		}

		// TODO: 2024/07/7 프론트엔드 서버 주소로 변경하기
		response.sendRedirect("https://www.kakaocorp.com/");
	}
}
