package kakao.mission3healthcare_backend.diet.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 식단 삭제 요청
 *
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@Getter
@AllArgsConstructor
public class DietDeleteRequest {

	private Long id;
	private String username;
}
