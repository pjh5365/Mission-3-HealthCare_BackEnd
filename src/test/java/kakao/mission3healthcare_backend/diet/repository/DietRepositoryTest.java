package kakao.mission3healthcare_backend.diet.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

/**
 * @author : parkjihyeok
 * @since : 2024/07/11
 */
@SpringBootTest
@Transactional
@DisplayName("식단 Repository")
class DietRepositoryTest {

	@Autowired MemberRepository memberRepository;
	@Autowired DietRepository dietRepository;

	@Test
	@DisplayName("식단 저장 테스트")
	@WithMockUser
	void saveTest() {
	    // Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);

		Diet diet = Diet.builder().member(member).mealType(MealType.BREAKFAST).dietDate(LocalDate.of(2024, 1, 1)).build();

		// When
		dietRepository.save(diet);
		Diet result = dietRepository.findById(diet.getId()).get();

		// Then
		assertNotNull(diet.getId());
		assertEquals(diet, result);
	}

	@Test
	@DisplayName("사용자의 식단을 불러오는 테스트")
	@WithMockUser
	void findByUsernameTest() {
	    // Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);

		Diet diet1 = Diet.builder().member(member).mealType(MealType.BREAKFAST).dietDate(LocalDate.of(2024, 1, 1)).build();
		Diet diet2 = Diet.builder().member(member).mealType(MealType.LUNCH).dietDate(LocalDate.of(2024, 1, 1)).build();
		Diet diet3 = Diet.builder().member(member).mealType(MealType.SNACK).dietDate(LocalDate.of(2024, 1, 1)).build();
		Diet diet4 = Diet.builder().member(member).mealType(MealType.DINNER).dietDate(LocalDate.of(2024, 1, 1)).build();
		Diet diet5 = Diet.builder().member(member).mealType(MealType.BREAKFAST).dietDate(LocalDate.of(2024, 1, 2)).build();

		dietRepository.save(diet1);
		dietRepository.save(diet2);
		dietRepository.save(diet3);
		dietRepository.save(diet4);
		dietRepository.save(diet5);

	    // When
		List<Diet> result = dietRepository.findByUsername("testId");

		// Then
		assertEquals(5, result.size());
		assertEquals(MealType.LUNCH, result.get(1).getMealType());
	}

	@Test
	@DisplayName("식단 삭제 테스트")
	@WithMockUser
	void deleteTest() {
	    // Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);

		Diet diet = Diet.builder().member(member).mealType(MealType.BREAKFAST).dietDate(LocalDate.of(2024, 1, 1)).build();
		dietRepository.save(diet);

		// When
		dietRepository.deleteById(diet.getId());

	    // Then
		assertEquals(Optional.empty(), dietRepository.findById(diet.getId()));
	}
}
