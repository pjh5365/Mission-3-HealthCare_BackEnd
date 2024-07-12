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
import kakao.mission3healthcare_backend.diet.domain.entity.Diet;
import kakao.mission3healthcare_backend.diet.domain.entity.Food;

/**
 * @author : parkjihyeok
 * @since : 2024/07/11
 */
@SpringBootTest
@Transactional
@DisplayName("음식 Repository 테스트")
@WithMockUser
class FoodRepositoryTest {

	@Autowired MemberRepository memberRepository;
	@Autowired DietRepository dietRepository;
	@Autowired FoodRepository foodRepository;
	private Diet savedDiet;

	@BeforeEach
	void setUp() {
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);
		Diet diet = Diet.builder().member(member).mealType(MealType.BREAKFAST).build();
		dietRepository.save(diet);

		savedDiet = diet;
	}

	@Test
	@DisplayName("저장 테스트")
	void saveTest() {
	    // Given
		Food food = Food.builder().foodName("햄버거").diet(savedDiet).build();

		// When
		foodRepository.save(food);
		Food result = foodRepository.findById(food.getId()).get();

		// Then
		assertEquals(food, result);
	}

	@Test
	@DisplayName("식단 ID로 조회 테스트")
	void findByDietIdTest() {
	    // Given
		Food food1 = Food.builder().foodName("햄버거").diet(savedDiet).build();
		Food food2 = Food.builder().foodName("감자튀김").diet(savedDiet).build();
		Food food3 = Food.builder().foodName("짬뽕").diet(savedDiet).build();
		Food food4 = Food.builder().foodName("짜장면").diet(savedDiet).build();

		foodRepository.save(food1);
		foodRepository.save(food2);
		foodRepository.save(food3);
		foodRepository.save(food4);

	    // When
		List<Food> result = foodRepository.findByDietId(savedDiet.getId());

		// Then
		assertEquals(4, result.size());
		assertEquals("짬뽕", result.get(2).getFoodName());
	}

	@Test
	@DisplayName("삭제 테스트")
	void deleteTest() {
	    // Given
		Food food = Food.builder().foodName("햄버거").diet(savedDiet).build();
		foodRepository.save(food);

		// When
		foodRepository.deleteById(food.getId());

	    // Then
		assertEquals(Optional.empty(), foodRepository.findById(food.getId()));
	}
}
