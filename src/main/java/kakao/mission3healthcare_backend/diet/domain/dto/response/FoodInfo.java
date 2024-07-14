package kakao.mission3healthcare_backend.diet.domain.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 음식이름과 영양소 정보를 담고있는 객체
 *
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class FoodInfo {

	private String foodName;
	private List<NutrientResponse> nutrients;
}
