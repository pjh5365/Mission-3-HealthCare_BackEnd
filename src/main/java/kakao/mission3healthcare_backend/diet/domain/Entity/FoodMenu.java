package kakao.mission3healthcare_backend.diet.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kakao.mission3healthcare_backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 음식에 대한 정보만 저장할 Entity
 *
 * @author : parkjihyeok
 * @since : 2024/07/12
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenu extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "food_id")
	private Long id;

	@Column(name = "food_name", nullable = false)
	private String foodName;
}
