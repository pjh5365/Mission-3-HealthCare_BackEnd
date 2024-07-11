package kakao.mission3healthcare_backend.diet.domain;

import lombok.Getter;

/**
 * 영양소 타입
 *
 * @author : parkjihyeok
 * @since : 2024/07/11
 */
@Getter
public enum NutrientType {
	PROTEIN("단백질"),
	FAT("지방"),
	CARBOHYDRATES("탄수화물"),
	VITAMIN("비타민"),
	MINERALS("무기질"),
	IRON("철분");

	private final String nutrient;

	NutrientType(String nutrient) {
		this.nutrient = nutrient;
	}
}
