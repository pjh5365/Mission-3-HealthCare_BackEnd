package kakao.mission3healthcare_backend.diet.domain;

import lombok.Getter;

/**
 * 식사 종류
 *
 * @author : parkjihyeok
 * @since : 2024/07/11
 */
@Getter
public enum MealType {
	BREAKFAST("아침"),
	LUNCH("점심"),
	DINNER("저녁"),
	SNACK("간식");

	private final String meal;

	MealType(String meal) {
		this.meal = meal;
	}
}
