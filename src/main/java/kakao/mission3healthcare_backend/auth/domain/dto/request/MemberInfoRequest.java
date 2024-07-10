package kakao.mission3healthcare_backend.auth.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원추가정보 Request
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@Getter
@AllArgsConstructor
public class MemberInfoRequest {

	private String username; // 회원ID
	private double goalWeight; // 목표체중
}
