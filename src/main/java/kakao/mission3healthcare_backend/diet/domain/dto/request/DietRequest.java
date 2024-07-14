package kakao.mission3healthcare_backend.diet.domain.dto.request;

import java.time.LocalDate;
import java.util.List;

import kakao.mission3healthcare_backend.diet.domain.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 식단 요청
 *
 * @author : parkjihyeok
 * @since : 2024/07/13
 */
@Getter
@AllArgsConstructor
public class DietRequest {

	private String username; // 회원ID
	private MealType mealType; // 식단 구분
	private List<String> foodNames; // 먹은 음식리스트
	private LocalDate dietDate; // 식단 날짜
}
