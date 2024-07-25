package kakao.mission3healthcare_backend.activity.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 걷기 삭제 Request
 *
 * @author : parkjihyeok
 * @since : 2024/07/24
 */
@Getter
@AllArgsConstructor
public class WalkDeleteRequest {

	private Long id; // 활동ID
	private String username;
}
