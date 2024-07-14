package kakao.mission3healthcare_backend.diet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kakao.mission3healthcare_backend.diet.domain.entity.Nutrient;

/**
 * 영양소 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/07/12
 */
public interface NutrientRepository extends JpaRepository<Nutrient, Long> {

	@Query("select n from Nutrient n "
			+ "join fetch n.foodMenu fm "
			+ "where fm.foodName = :foodName")
	List<Nutrient> findByFoodName(String foodName);
}
