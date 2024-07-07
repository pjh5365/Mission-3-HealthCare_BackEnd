package kakao.mission3healthcare_backend.auth.domain.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import kakao.mission3healthcare_backend.auth.domain.CustomUserDetails;
import kakao.mission3healthcare_backend.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 기본 로그인을 담당할 Provider
 *
 * @author : parkjihyeok
 * @since : 2024/07/06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicAuthenticationProvider implements AuthenticationProvider {

	private final CustomUserDetailsService customUserDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		try {
			CustomUserDetails userDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(username);
			if (bCryptPasswordEncoder.matches(password, userDetails.getPassword())) { // 비밀번호가 일치한다면
				log.info("[로그인] - [{}] 가 로그인에 성공했습니다.", username);
				return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
			}
		} catch (UsernameNotFoundException e) {
			log.error("[로그인] - {} username = [{}]", e.getMessage(), username);
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		log.error("[로그인] - [{}] 가 비밀번호가 맞지않아 로그인에 실패했습니다.", username);
		throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
