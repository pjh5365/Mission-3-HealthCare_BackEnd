package kakao.mission3healthcare_backend.diet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kakao.mission3healthcare_backend.diet.domain.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {

	// 어차피 이미 diet가 조회된 상태이므로 굳이 페치조인으로 diet를 가져올 필요는 없을듯
	List<Food> findByDietId(Long dietId);
}

