package kakao.mission3healthcare_backend.auth.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 Request
 *
 * @author : parkjihyeok
 * @since : 2024/07/06
 */
@Getter
@AllArgsConstructor
public class MemberLoginRequest {

	private String username;
	private String password;
}
