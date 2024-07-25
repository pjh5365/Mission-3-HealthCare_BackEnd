package kakao.mission3healthcare_backend.activity.domain.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 걷기 수정 Request
 *
 * @author : parkjihyeok
 * @since : 2024/07/24
 */
@Getter
@AllArgsConstructor
public class WalkUpdateRequest {

	private Long id; // 활동 ID
	private String username;
	private Double distance; // 거리
	private Double avgHeartRate; // 평균 심박수
	private LocalDate walkDate; // 활동 날짜
}
