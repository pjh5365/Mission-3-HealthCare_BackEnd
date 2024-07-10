package kakao.mission3healthcare_backend.common;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * JPA에서 자동으로 작성자, 수정자를 입력하기 위한 클래스
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 권한정보를 찾을 수 없다면
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UsernameNotFoundException("로그인 정보를 찾을 수 없습니다.");
		}

		return Optional.of(authentication.getName());
	}
}
