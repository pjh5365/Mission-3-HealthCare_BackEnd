package kakao.mission3healthcare_backend.auth.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 상세정보 Response
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@Getter
@AllArgsConstructor
public class MemberInfoResponse {

	private String username; // 회원 ID
	private double goalWeight; // 목표 체중
}
