package kakao.mission3healthcare_backend.auth.domain.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao.mission3healthcare_backend.common.response.ApiSingleResponse;

/**
 * 로그인 실패 Handler
 *
 * @author : parkjihyeok
 * @since : 2024/07/06
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final Gson gson;

	@Autowired
	public LoginFailureHandler(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter()
				.write(gson.toJson(new ApiSingleResponse<>(HttpStatus.UNAUTHORIZED.toString(),
						"로그인에 실패했습니다.",
						getErrorMessage(exception)
				)));
	}

	private static String getErrorMessage(AuthenticationException exception) {
		String error;
		if (exception instanceof BadCredentialsException) {
			error = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
		} else if (exception instanceof InternalAuthenticationServiceException) {
			error = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
		} else if (exception instanceof UsernameNotFoundException) {
			error = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			error = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
		} else {
			error = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
		}
		return error;
	}
}
