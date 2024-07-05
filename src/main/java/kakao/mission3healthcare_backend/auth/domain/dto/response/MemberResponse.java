package kakao.mission3healthcare_backend.auth.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 Response
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@Getter
@AllArgsConstructor
public class MemberResponse {

	private String username; // 회원 ID
	private String name; // 회원 이름
}
