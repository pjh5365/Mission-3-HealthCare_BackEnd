package kakao.mission3healthcare_backend.auth.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원정보 수정 Request
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@Getter
@NoArgsConstructor
public class MemberUpdateRequest {

	private String username; // 회원ID
	private String name; // 변경할 이름
	private String beforePassword; // 이전에 사용하던 비밀번호
	private String newPassword; // 변경할 비밀번호

	public MemberUpdateRequest(String username, String name) {
		this.username = username;
		this.name = name;
	}

	public MemberUpdateRequest(String username, String beforePassword, String newPassword) {
		this.username = username;
		this.beforePassword = beforePassword;
		this.newPassword = newPassword;
	}
}
