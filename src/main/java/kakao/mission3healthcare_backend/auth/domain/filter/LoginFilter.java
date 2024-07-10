package kakao.mission3healthcare_backend.auth.domain.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberLoginRequest;

/**
 * 로그인을 담당하는 필터
 *
 * @author : parkjihyeok
 * @since : 2024/07/06
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	Gson gson = new Gson();

	public LoginFilter() {
		super("/api/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
			AuthenticationException, IOException {

		String method = request.getMethod();

		if (!method.equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		MemberLoginRequest getRequest = gson.fromJson(request.getReader(), MemberLoginRequest.class);
		String username = getRequest.getUsername();
		String password = getRequest.getPassword();

		UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
				.unauthenticated(username, password);

		setDetails(request, authRequest);
		return getAuthenticationManager().authenticate(authRequest);
	}

	private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}
}
