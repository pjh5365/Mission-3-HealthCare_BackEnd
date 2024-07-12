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

import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;
import kakao.mission3healthcare_backend.diet.domain.MealType;
import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import kakao.mission3healthcare_backend.diet.domain.entity.Diet;
import kakao.mission3healthcare_backend.diet.domain.entity.Food;
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

	@Autowired MemberRepository memberRepository;
	@Autowired DietRepository dietRepository;
	@Autowired FoodRepository foodRepository;
	@Autowired NutrientRepository nutrientRepository;

	private Food savedFood;

	@BeforeEach
	void setUp() {
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);
		Diet diet = Diet.builder().member(member).mealType(MealType.BREAKFAST).build();
		dietRepository.save(diet);
		Food food = Food.builder().diet(diet).foodName("햄버거").build();
		foodRepository.save(food);
		savedFood = food;
	}

	@Test
	@DisplayName("영양소 저장 테스트")
	void saveTest() {
	    // Given
		Nutrient n = Nutrient.builder().food(savedFood).amount(100.0).nutrientType(NutrientType.CARBOHYDRATES).build();

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
		Nutrient n1 = Nutrient.builder().food(savedFood).amount(100.0).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient n2 = Nutrient.builder().food(savedFood).amount(11.2).nutrientType(NutrientType.IRON).build();
		Nutrient n3 = Nutrient.builder().food(savedFood).amount(56.4).nutrientType(NutrientType.PROTEIN).build();
		Nutrient n4 = Nutrient.builder().food(savedFood).amount(12.4).nutrientType(NutrientType.FAT).build();

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
	@DisplayName("음식 ID로 영양소를 검색하는 테스트")
	void findBryFoodIdTest() {
	    // Given
		Nutrient n1 = Nutrient.builder().food(savedFood).amount(100.0).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient n2 = Nutrient.builder().food(savedFood).amount(11.2).nutrientType(NutrientType.IRON).build();
		Nutrient n3 = Nutrient.builder().food(savedFood).amount(56.4).nutrientType(NutrientType.PROTEIN).build();
		Nutrient n4 = Nutrient.builder().food(savedFood).amount(12.4).nutrientType(NutrientType.FAT).build();

		nutrientRepository.save(n1);
		nutrientRepository.save(n2);
		nutrientRepository.save(n3);
		nutrientRepository.save(n4);

	    // When
		List<Nutrient> result = nutrientRepository.findByFoodId(savedFood.getId());

		// Then
		assertEquals(4, result.size());
		assertEquals(11.2, result.get(1).getAmount());
		assertEquals(NutrientType.PROTEIN, result.get(2).getNutrientType());
	}

	@Test
	@DisplayName("영양소 삭제 테스트")
	void deleteTest() {
	    // Given
		Nutrient n = Nutrient.builder().food(savedFood).amount(100.0).nutrientType(NutrientType.CARBOHYDRATES).build();
		nutrientRepository.save(n);

		// When
		nutrientRepository.deleteById(n.getId());

	    // Then
		assertEquals(Optional.empty(), nutrientRepository.findById(n.getId()));
	}
}
