package kakao.mission3healthcare_backend.diet.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kakao.mission3healthcare_backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 식단 사진 Entity
 *
 * @author : parkjihyeok
 * @since : 2024/07/15
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "diet_image_id")
	private Long id;

	@Setter
	@Column(name = "image_name", nullable = false)
	private String imageName;

	@OneToOne
	@JoinColumn(nullable = false, name = "diet_id")
	private Diet diet;
}
