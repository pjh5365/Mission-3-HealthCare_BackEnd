package kakao.mission3healthcare_backend.diet.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import kakao.mission3healthcare_backend.diet.domain.entity.Food;
import kakao.mission3healthcare_backend.diet.domain.entity.FoodMenu;

/**
 * @author : parkjihyeok
 * @since : 2024/07/12
 */
@SpringBootTest
@DisplayName("음식메뉴 Repository 테스트")
@Transactional
@WithMockUser
class FoodMenuRepositoryTest {

	@Autowired FoodMenuRepository foodMenuRepository;

	@Test
	@DisplayName("음식메뉴 저장 테스트")
	void saveTest() {
	    // Given
		FoodMenu food = FoodMenu.builder().foodName("햄버거").build();

		// When
		foodMenuRepository.save(food);
		FoodMenu result = foodMenuRepository.findById(food.getId()).get();

		// Then
		assertEquals(food, result);
	}

	@Test
	@DisplayName("음식메뉴 삭제 테스트")
	void deleteTest() {
		// Given
		FoodMenu food = FoodMenu.builder().foodName("햄버거").build();
		foodMenuRepository.save(food);

		// When
		foodMenuRepository.deleteById(food.getId());

		// Then
		assertEquals(Optional.empty(), foodMenuRepository.findById(food.getId()));
	}
}
