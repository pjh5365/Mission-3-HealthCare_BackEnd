package kakao.mission3healthcare_backend.diet.domain.dto.response;

import java.time.LocalDate;
import java.util.List;

import kakao.mission3healthcare_backend.diet.domain.MealType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 식단 정보를 담을 객체
 *
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class DietResponse {

	private String username; // 작성자
	private MealType mealType; // 식단 구분
	private List<FoodInfo>  foods; // 식단에 포함된 음식정보
	private LocalDate dietDate; // 식단 날짜
}
