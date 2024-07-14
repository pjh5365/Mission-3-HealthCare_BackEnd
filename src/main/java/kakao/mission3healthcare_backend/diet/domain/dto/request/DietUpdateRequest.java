package kakao.mission3healthcare_backend.diet.domain.dto.request;

import java.util.List;

import kakao.mission3healthcare_backend.diet.domain.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 식단 수정 요청
 *
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@Getter
@AllArgsConstructor
public class DietUpdateRequest {

	private Long dietId;
	private String username; // 회원ID
	private MealType mealType; // 식단 구분
	private List<String> foodNames; // 먹은 음식리스트
}
