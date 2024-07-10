package kakao.mission3healthcare_backend.auth.domain;

import lombok.Getter;

/**
 * 회원들의 권한을 지정하는 Enum
 * 관리자, 일반회원을 가진다.
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@Getter
public enum UserRole {
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_USER("ROLE_USER");

	private final String role;

	UserRole(String role) {
		this.role = role;
	}
}
