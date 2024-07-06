package kakao.mission3healthcare_backend.auth.domain.dto.request;

import lombok.Getter;

/**
 * 로그인 Request
 *
 * @author : parkjihyeok
 * @since : 2024/07/06
 */
@Getter
public class MemberLoginRequest {

	private String username;
	private String password;
}
