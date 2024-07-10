package kakao.mission3healthcare_backend.auth.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 Request
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@Getter
@AllArgsConstructor
public class MemberJoinRequest {

	private String username; // 회원ID
	private String name; // 회원이름
	private String password; // 비밀번호
}
