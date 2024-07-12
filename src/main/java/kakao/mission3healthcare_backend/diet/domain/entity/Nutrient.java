package kakao.mission3healthcare_backend.diet.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nutrient {

	@Id
	@GeneratedValue
	@Column(name = "nutrient_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_menu_id", nullable = false)
	private FoodMenu foodMenu;

	@Enumerated(EnumType.STRING)
	private NutrientType nutrientType; // 영양소 종류
	private Double amount; // 용량
}
