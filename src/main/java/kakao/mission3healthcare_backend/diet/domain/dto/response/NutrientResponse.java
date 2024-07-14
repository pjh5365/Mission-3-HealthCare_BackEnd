package kakao.mission3healthcare_backend.diet.domain.dto.response;

import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 영양소 정보를 담은 객체
 *
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class NutrientResponse {

	private NutrientType nutrientType; // 영양소 종류
	private Double amount; // 용량
}
