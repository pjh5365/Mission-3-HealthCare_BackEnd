package kakao.mission3healthcare_backend.diet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kakao.mission3healthcare_backend.diet.domain.entity.DietImage;

/**
 * 식단 사진을 담당하는 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/07/15
 */
public interface DietImageRepository extends JpaRepository<DietImage, Long> {

	Optional<DietImage> findByDietId(Long id);

	void deleteByImageName(String imageName);

	boolean existsByImageName(String imageName);
}

