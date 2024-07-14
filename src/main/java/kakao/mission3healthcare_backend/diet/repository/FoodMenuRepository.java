package kakao.mission3healthcare_backend.diet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kakao.mission3healthcare_backend.diet.domain.entity.FoodMenu;

/**
 * 음식 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/07/12
 */
public interface FoodMenuRepository extends JpaRepository<FoodMenu, Long> {
}
