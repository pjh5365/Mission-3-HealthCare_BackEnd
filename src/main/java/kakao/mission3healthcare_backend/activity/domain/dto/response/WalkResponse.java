package kakao.mission3healthcare_backend.activity.domain.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 걷기 정보를 담을 Response
 *
 * @author : parkjihyeok
 * @since : 2024/07/24
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class WalkResponse {

	private String username;
	private Double distance; // 거리
	private Double avgHeartRate; // 평균 심박수
	private LocalDate walkDate; // 활동 날짜
}
