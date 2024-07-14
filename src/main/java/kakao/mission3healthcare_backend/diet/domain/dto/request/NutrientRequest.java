package kakao.mission3healthcare_backend.diet.domain.dto.request;

import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 음식을 등록할 때 함께 보내는 영양소 정보
 *
 * @author : parkjihyeok
 * @since : 2024/07/11
 */
@Getter
@AllArgsConstructor
public class NutrientRequest {

	private NutrientType nutrientType;
	private Double amount;
}
