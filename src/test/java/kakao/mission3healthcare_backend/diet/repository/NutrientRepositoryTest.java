package kakao.mission3healthcare_backend.diet.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import kakao.mission3healthcare_backend.diet.domain.entity.FoodMenu;
import kakao.mission3healthcare_backend.diet.domain.entity.Nutrient;

/**
 * @author : parkjihyeok
 * @since : 2024/07/12
 */
@SpringBootTest
@DisplayName("영양소 Repository 테스트")
@Transactional
@WithMockUser
class NutrientRepositoryTest {

	@Autowired FoodMenuRepository foodMenuRepository;
	@Autowired NutrientRepository nutrientRepository;

	private FoodMenu savedFoodMenu;

	@BeforeEach
	void setUp() {
		FoodMenu food = FoodMenu.builder().foodName("햄버거").build();
		foodMenuRepository.save(food);
		savedFoodMenu = food;
	}

	@Test
	@DisplayName("영양소 저장 테스트")
	void saveTest() {
	    // Given
		Nutrient n = Nutrient.builder().foodMenu(savedFoodMenu).amount(100.0).nutrientType(NutrientType.CARBOHYDRATES).build();

		// When
		nutrientRepository.save(n);
		Nutrient result = nutrientRepository.findById(n.getId()).get();

		// Then
		assertEquals(n, result);
	}

	@Test
	@DisplayName("음식이름으로 영영소를 찾는 테스트")
	void findByFoodNameTest() {
	    // Given
		Nutrient n1 = Nutrient.builder().foodMenu(savedFoodMenu).amount(100.0).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient n2 = Nutrient.builder().foodMenu(savedFoodMenu).amount(11.2).nutrientType(NutrientType.IRON).build();
		Nutrient n3 = Nutrient.builder().foodMenu(savedFoodMenu).amount(56.4).nutrientType(NutrientType.PROTEIN).build();
		Nutrient n4 = Nutrient.builder().foodMenu(savedFoodMenu).amount(12.4).nutrientType(NutrientType.FAT).build();

		nutrientRepository.save(n1);
		nutrientRepository.save(n2);
		nutrientRepository.save(n3);
		nutrientRepository.save(n4);

	    // When
		List<Nutrient> result = nutrientRepository.findByFoodName("햄버거");

		// Then
		assertEquals(4, result.size());
		assertEquals(11.2, result.get(1).getAmount());
		assertEquals(NutrientType.PROTEIN, result.get(2).getNutrientType());
	}

	@Test
	@DisplayName("영양소 삭제 테스트")
	void deleteTest() {
	    // Given
		Nutrient n = Nutrient.builder().foodMenu(savedFoodMenu).amount(100.0).nutrientType(NutrientType.CARBOHYDRATES).build();
		nutrientRepository.save(n);

		// When
		nutrientRepository.deleteById(n.getId());

	    // Then
		assertEquals(Optional.empty(), nutrientRepository.findById(n.getId()));
	}
}
