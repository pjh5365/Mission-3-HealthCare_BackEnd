package kakao.mission3healthcare_backend.diet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.common.service.CommonService;
import kakao.mission3healthcare_backend.diet.domain.MealType;
import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietDeleteRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietUpdateRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.response.DietResponse;
import kakao.mission3healthcare_backend.diet.domain.dto.response.FoodInfo;
import kakao.mission3healthcare_backend.diet.domain.dto.response.NutrientResponse;
import kakao.mission3healthcare_backend.diet.domain.entity.Diet;
import kakao.mission3healthcare_backend.diet.domain.entity.Food;
import kakao.mission3healthcare_backend.diet.domain.entity.FoodMenu;
import kakao.mission3healthcare_backend.diet.domain.entity.Nutrient;
import kakao.mission3healthcare_backend.diet.repository.DietImageRepository;
import kakao.mission3healthcare_backend.diet.repository.DietRepository;
import kakao.mission3healthcare_backend.diet.repository.FoodRepository;
import kakao.mission3healthcare_backend.diet.repository.NutrientRepository;

/**
 * @author : parkjihyeok
 * @since : 2024/07/13
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("식단 서비스 테스트")
class DietServiceTest {

	@InjectMocks DietService dietService;
	@Mock CommonService commonService;
	@Mock DietRepository dietRepository;
	@Mock DietImageRepository dietImageRepository;
	@Mock FoodRepository foodRepository;
	@Mock NutrientRepository nutrientRepository;

	@Test
	@DisplayName("식단 추가 테스트 (실패 - 회원정보를 찾을 수 없음)")
	void addDietFail() {
	    // Given
		DietRequest dietRequest = new DietRequest("testId", MealType.BREAKFAST, List.of("김치", "라면"), LocalDate.of(2024, 1, 1), null);
		given(commonService.findMember("testId", true)).willThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

	    // When
		assertThrows(UsernameNotFoundException.class, () -> dietService.addDiet(dietRequest));

	    // Then
		verify(commonService).findMember("testId", true);
		verify(dietRepository, never()).save(any());
		verify(foodRepository, never()).save(any());
	}

	@Test
	@DisplayName("식단 추가 테스트 (성공)")
	void addDiet() {
	    // Given
		DietRequest dietRequest = new DietRequest("testId", MealType.BREAKFAST, List.of("김치", "라면"), LocalDate.of(2024, 1, 1), null);
		Member member = Member.builder().username("testId").build();
		given(commonService.findMember("testId", true)).willReturn(member);

	    // When
		assertDoesNotThrow(() -> dietService.addDiet(dietRequest));

	    // Then
		verify(commonService).findMember("testId", true);
		verify(dietRepository).save(any());
		verify(foodRepository, times(2)).save(any());
	}

	@Test
	@DisplayName("식단 추가 테스트 (성공 - 이미지)")
	void addDietImage() {
		// Given
		DietRequest dietRequest = new DietRequest("testId", MealType.BREAKFAST, null, LocalDate.of(2024, 1, 1), "test.png");
		Member member = Member.builder().username("testId").build();
		given(commonService.findMember("testId", true)).willReturn(member);

		// When
		assertDoesNotThrow(() -> dietService.addDiet(dietRequest));

		// Then
		verify(commonService).findMember("testId", true);
		verify(dietRepository).save(any());
		verify(foodRepository, never()).save(any());
		verify(dietImageRepository).save(any());
	}

	// 수정 성공
	@Test
	@DisplayName("식단 수정 테스트 (실패)")
	void updatedDietFail() {
	    // Given
		DietUpdateRequest request = new DietUpdateRequest(10L, "testId", MealType.BREAKFAST, List.of("냉면", "단무지"));
		given(commonService.findMember("testId", true)).willThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

	    // When
		assertThrows(UsernameNotFoundException.class, () -> dietService.updateDiet(request));

	    // Then
		verify(commonService).findMember("testId", true);
	}

	@Test
	@DisplayName("식단 수정 테스트 (성공)")
	void updateDiet() {
	    // Given
		DietUpdateRequest request = new DietUpdateRequest(10L, "testId", MealType.BREAKFAST, List.of("냉면", "단무지"));
		Member member = Member.builder().username("testId").build();
		Diet diet = new Diet(10L, member, MealType.BREAKFAST, LocalDate.of(2024, 1, 1));
		given(commonService.findMember("testId", true)).willReturn(member);
		given(dietRepository.findById(10L)).willReturn(Optional.of(diet));

	    // When
		assertDoesNotThrow(() -> dietService.updateDiet(request));

	    // Then
		verify(commonService).findMember("testId", true);
		verify(dietRepository).findById(10L);
		verify(foodRepository).deleteAll(any());
		verify(foodRepository, times(2)).save(any());
	}

	@Test
	@DisplayName("식단 삭제 테스트 (실패)")
	void deleteDietFail() {
	    // Given
		DietDeleteRequest request = new DietDeleteRequest(10L, "testId");
		given(commonService.findMember("testId", true)).willThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

	    // When
		assertThrows(UsernameNotFoundException.class, () -> dietService.deleteDiet(request));

	    // Then
		verify(commonService).findMember("testId", true);
	}

	@Test
	@DisplayName("식단 삭제 테스트 (성공)")
	void deleteDiet() {
	    // Given
		DietDeleteRequest request = new DietDeleteRequest(10L, "testId");
		Member member = Member.builder().username("testId").build();
		Diet diet = new Diet(10L, member, MealType.BREAKFAST, LocalDate.of(2024, 1, 1));
		given(commonService.findMember("testId", true)).willReturn(member);
		given(dietRepository.findById(10L)).willReturn(Optional.of(diet));

	    // When
		assertDoesNotThrow(() -> dietService.deleteDiet(request));

	    // Then
		verify(commonService).findMember("testId", true);
		verify(dietRepository).findById(10L);
		verify(foodRepository).deleteAll(any());
		verify(dietRepository).delete(any());
	}

	@Test
	@DisplayName("한 식단에 대한 종합정보 반환 테스트")
	void getDietTest() {
	    // Given
		Member member = Member.builder().username("testId").build();
		Diet diet = new Diet(10L, member, MealType.BREAKFAST, LocalDate.of(2024, 1, 1));
		Food f1 = new Food(101L, diet, "햄버거");
		Food f2 = new Food(102L, diet, "감자튀김");
		Food f3 = new Food(103L, diet, "치즈스틱");
		Food f4 = new Food(104L, diet, "치킨텐더");
		Food f5 = new Food(105L, diet, "콜라");

		FoodMenu fm1 = FoodMenu.builder().foodName("햄버거").build();
		Nutrient fm1n1 = Nutrient.builder().foodMenu(fm1).amount(1.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient fm1n2 = Nutrient.builder().foodMenu(fm1).amount(1.2).nutrientType(NutrientType.IRON).build();
		Nutrient fm1n3 = Nutrient.builder().foodMenu(fm1).amount(1.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient fm1n4 = Nutrient.builder().foodMenu(fm1).amount(1.4).nutrientType(NutrientType.FAT).build();

		FoodMenu fm2 = FoodMenu.builder().foodName("감자튀김").build();
		Nutrient fm2n1 = Nutrient.builder().foodMenu(fm2).amount(2.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient fm2n2 = Nutrient.builder().foodMenu(fm2).amount(2.2).nutrientType(NutrientType.IRON).build();
		Nutrient fm2n3 = Nutrient.builder().foodMenu(fm2).amount(2.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient fm2n4 = Nutrient.builder().foodMenu(fm2).amount(2.4).nutrientType(NutrientType.FAT).build();

		FoodMenu fm3 = FoodMenu.builder().foodName("치즈스틱").build();
		Nutrient fm3n1 = Nutrient.builder().foodMenu(fm3).amount(3.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient fm3n2 = Nutrient.builder().foodMenu(fm3).amount(3.2).nutrientType(NutrientType.IRON).build();
		Nutrient fm3n3 = Nutrient.builder().foodMenu(fm3).amount(3.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient fm3n4 = Nutrient.builder().foodMenu(fm3).amount(3.4).nutrientType(NutrientType.FAT).build();

		FoodMenu fm4 = FoodMenu.builder().foodName("치킨텐더").build();
		Nutrient fm4n1 = Nutrient.builder().foodMenu(fm4).amount(4.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient fm4n2 = Nutrient.builder().foodMenu(fm4).amount(4.2).nutrientType(NutrientType.IRON).build();
		Nutrient fm4n3 = Nutrient.builder().foodMenu(fm4).amount(4.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient fm4n4 = Nutrient.builder().foodMenu(fm4).amount(4.4).nutrientType(NutrientType.FAT).build();

		FoodMenu fm5 = FoodMenu.builder().foodName("콜라").build();
		Nutrient fm5n1 = Nutrient.builder().foodMenu(fm5).amount(5.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient fm5n2 = Nutrient.builder().foodMenu(fm5).amount(5.2).nutrientType(NutrientType.IRON).build();
		Nutrient fm5n3 = Nutrient.builder().foodMenu(fm5).amount(5.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient fm5n4 = Nutrient.builder().foodMenu(fm5).amount(5.4).nutrientType(NutrientType.FAT).build();

		given(foodRepository.findByDietId(10L)).willReturn(List.of(f1, f2, f3, f4, f5));
		given(nutrientRepository.findByFoodName("햄버거")).willReturn(List.of(fm1n1, fm1n2, fm1n3, fm1n4));
		given(nutrientRepository.findByFoodName("감자튀김")).willReturn(List.of(fm2n1, fm2n2, fm2n3, fm2n4));
		given(nutrientRepository.findByFoodName("치즈스틱")).willReturn(List.of(fm3n1, fm3n2, fm3n3, fm3n4));
		given(nutrientRepository.findByFoodName("치킨텐더")).willReturn(List.of(fm4n1, fm4n2, fm4n3, fm4n4));
		given(nutrientRepository.findByFoodName("콜라")).willReturn(List.of(fm5n1, fm5n2, fm5n3, fm5n4));

	    // When
		DietResponse result = dietService.getDiet(diet);

		// Then
		assertEquals(FoodMenuToFoodInfo("햄버거", List.of(fm1n1, fm1n2, fm1n3, fm1n4)), result.getFoods().get(0));
		assertEquals(FoodMenuToFoodInfo("감자튀김", List.of(fm2n1, fm2n2, fm2n3, fm2n4)), result.getFoods().get(1));
		assertEquals(FoodMenuToFoodInfo("치즈스틱", List.of(fm3n1, fm3n2, fm3n3, fm3n4)), result.getFoods().get(2));
		assertEquals(FoodMenuToFoodInfo("치킨텐더", List.of(fm4n1, fm4n2, fm4n3, fm4n4)), result.getFoods().get(3));
		assertEquals(FoodMenuToFoodInfo("콜라", List.of(fm5n1, fm5n2, fm5n3, fm5n4)), result.getFoods().get(4));
		assertEquals("testId", result.getUsername());
		assertEquals(MealType.BREAKFAST, result.getMealType());
	}

	@Test
	@DisplayName("한 회원의 모든 식단 정보 반환 테스트")
	void getDietsTest() {
	    // Given
		Member member = Member.builder().username("testId").build();
		Diet d1 = new Diet(10L, member, MealType.BREAKFAST, LocalDate.of(2024, 1, 1));
		Food d1f1 = new Food(101L, d1, "햄버거");
		Food d1f2 = new Food(102L, d1, "감자튀김");
		Food d1f3 = new Food(103L, d1, "치즈스틱");
		Food d1f4 = new Food(104L, d1, "치킨텐더");
		Food d1f5 = new Food(105L, d1, "콜라");

		FoodMenu d1fm1 = FoodMenu.builder().foodName("햄버거").build();
		Nutrient d1fm1n1 = Nutrient.builder().foodMenu(d1fm1).amount(1.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d1fm1n2 = Nutrient.builder().foodMenu(d1fm1).amount(1.2).nutrientType(NutrientType.IRON).build();
		Nutrient d1fm1n3 = Nutrient.builder().foodMenu(d1fm1).amount(1.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d1fm1n4 = Nutrient.builder().foodMenu(d1fm1).amount(1.4).nutrientType(NutrientType.FAT).build();

		FoodMenu d1fm2 = FoodMenu.builder().foodName("감자튀김").build();
		Nutrient d1fm2n1 = Nutrient.builder().foodMenu(d1fm2).amount(2.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d1fm2n2 = Nutrient.builder().foodMenu(d1fm2).amount(2.2).nutrientType(NutrientType.IRON).build();
		Nutrient d1fm2n3 = Nutrient.builder().foodMenu(d1fm2).amount(2.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d1fm2n4 = Nutrient.builder().foodMenu(d1fm2).amount(2.4).nutrientType(NutrientType.FAT).build();

		FoodMenu d1fm3 = FoodMenu.builder().foodName("치즈스틱").build();
		Nutrient d1fm3n1 = Nutrient.builder().foodMenu(d1fm3).amount(3.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d1fm3n2 = Nutrient.builder().foodMenu(d1fm3).amount(3.2).nutrientType(NutrientType.IRON).build();
		Nutrient d1fm3n3 = Nutrient.builder().foodMenu(d1fm3).amount(3.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d1fm3n4 = Nutrient.builder().foodMenu(d1fm3).amount(3.4).nutrientType(NutrientType.FAT).build();

		FoodMenu d1fm4 = FoodMenu.builder().foodName("치킨텐더").build();
		Nutrient d1fm4n1 = Nutrient.builder().foodMenu(d1fm4).amount(4.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d1fm4n2 = Nutrient.builder().foodMenu(d1fm4).amount(4.2).nutrientType(NutrientType.IRON).build();
		Nutrient d1fm4n3 = Nutrient.builder().foodMenu(d1fm4).amount(4.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d1fm4n4 = Nutrient.builder().foodMenu(d1fm4).amount(4.4).nutrientType(NutrientType.FAT).build();

		FoodMenu d1fm5 = FoodMenu.builder().foodName("콜라").build();
		Nutrient d1fm5n1 = Nutrient.builder().foodMenu(d1fm5).amount(5.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d1fm5n2 = Nutrient.builder().foodMenu(d1fm5).amount(5.2).nutrientType(NutrientType.IRON).build();
		Nutrient d1fm5n3 = Nutrient.builder().foodMenu(d1fm5).amount(5.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d1fm5n4 = Nutrient.builder().foodMenu(d1fm5).amount(5.4).nutrientType(NutrientType.FAT).build();

		given(foodRepository.findByDietId(10L)).willReturn(List.of(d1f1, d1f2, d1f3, d1f4, d1f5));
		given(nutrientRepository.findByFoodName("햄버거")).willReturn(List.of(d1fm1n1, d1fm1n2, d1fm1n3, d1fm1n4));
		given(nutrientRepository.findByFoodName("감자튀김")).willReturn(List.of(d1fm2n1, d1fm2n2, d1fm2n3, d1fm2n4));
		given(nutrientRepository.findByFoodName("치즈스틱")).willReturn(List.of(d1fm3n1, d1fm3n2, d1fm3n3, d1fm3n4));
		given(nutrientRepository.findByFoodName("치킨텐더")).willReturn(List.of(d1fm4n1, d1fm4n2, d1fm4n3, d1fm4n4));
		given(nutrientRepository.findByFoodName("콜라")).willReturn(List.of(d1fm5n1, d1fm5n2, d1fm5n3, d1fm5n4));

		Diet d2 = new Diet(20L, member, MealType.LUNCH, LocalDate.of(2024, 1, 1));
		Food d2f1 = new Food(201L, d2, "짜장면");
		Food d2f2 = new Food(202L, d2, "짬뽕");
		Food d2f3 = new Food(203L, d2, "탕수육");
		Food d2f4 = new Food(204L, d2, "유산슬");

		FoodMenu d2fm1 = FoodMenu.builder().foodName("짜장면").build();
		Nutrient d2fm1n1 = Nutrient.builder().foodMenu(d2fm1).amount(1.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d2fm1n2 = Nutrient.builder().foodMenu(d2fm1).amount(1.2).nutrientType(NutrientType.IRON).build();
		Nutrient d2fm1n3 = Nutrient.builder().foodMenu(d2fm1).amount(1.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d2fm1n4 = Nutrient.builder().foodMenu(d2fm1).amount(1.4).nutrientType(NutrientType.FAT).build();

		FoodMenu d2fm2 = FoodMenu.builder().foodName("짬뽕").build();
		Nutrient d2fm2n1 = Nutrient.builder().foodMenu(d2fm2).amount(2.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d2fm2n2 = Nutrient.builder().foodMenu(d2fm2).amount(2.2).nutrientType(NutrientType.IRON).build();
		Nutrient d2fm2n3 = Nutrient.builder().foodMenu(d2fm2).amount(2.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d2fm2n4 = Nutrient.builder().foodMenu(d2fm2).amount(2.4).nutrientType(NutrientType.FAT).build();

		FoodMenu d2fm3 = FoodMenu.builder().foodName("탕수육").build();
		Nutrient d2fm3n1 = Nutrient.builder().foodMenu(d2fm3).amount(3.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d2fm3n2 = Nutrient.builder().foodMenu(d2fm3).amount(3.2).nutrientType(NutrientType.IRON).build();
		Nutrient d2fm3n3 = Nutrient.builder().foodMenu(d2fm3).amount(3.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d2fm3n4 = Nutrient.builder().foodMenu(d2fm3).amount(3.4).nutrientType(NutrientType.FAT).build();

		FoodMenu d2fm4 = FoodMenu.builder().foodName("유산슬").build();
		Nutrient d2fm4n1 = Nutrient.builder().foodMenu(d2fm4).amount(4.1).nutrientType(NutrientType.CARBOHYDRATES).build();
		Nutrient d2fm4n2 = Nutrient.builder().foodMenu(d2fm4).amount(4.2).nutrientType(NutrientType.IRON).build();
		Nutrient d2fm4n3 = Nutrient.builder().foodMenu(d2fm4).amount(4.3).nutrientType(NutrientType.PROTEIN).build();
		Nutrient d2fm4n4 = Nutrient.builder().foodMenu(d2fm4).amount(4.4).nutrientType(NutrientType.FAT).build();

		given(dietRepository.findByUsername("testId")).willReturn(List.of(d1, d2));
		given(foodRepository.findByDietId(20L)).willReturn(List.of(d2f1, d2f2, d2f3, d2f4));
		given(nutrientRepository.findByFoodName("짜장면")).willReturn(List.of(d2fm1n1, d2fm1n2, d2fm1n3, d2fm1n4));
		given(nutrientRepository.findByFoodName("짬뽕")).willReturn(List.of(d2fm2n1, d2fm2n2, d2fm2n3, d2fm2n4));
		given(nutrientRepository.findByFoodName("탕수육")).willReturn(List.of(d2fm3n1, d2fm3n2, d2fm3n3, d2fm3n4));
		given(nutrientRepository.findByFoodName("유산슬")).willReturn(List.of(d2fm4n1, d2fm4n2, d2fm4n3, d2fm4n4));

		// When
		List<DietResponse> result = dietService.getDiets("testId");

		// Then
		assertEquals(FoodMenuToFoodInfo("햄버거", List.of(d1fm1n1, d1fm1n2, d1fm1n3, d1fm1n4)), result.get(0).getFoods().get(0));
		assertEquals(FoodMenuToFoodInfo("감자튀김", List.of(d1fm2n1, d1fm2n2, d1fm2n3, d1fm2n4)), result.get(0).getFoods().get(1));
		assertEquals(FoodMenuToFoodInfo("치즈스틱", List.of(d1fm3n1, d1fm3n2, d1fm3n3, d1fm3n4)), result.get(0).getFoods().get(2));
		assertEquals(FoodMenuToFoodInfo("치킨텐더", List.of(d1fm4n1, d1fm4n2, d1fm4n3, d1fm4n4)), result.get(0).getFoods().get(3));
		assertEquals(FoodMenuToFoodInfo("콜라", List.of(d1fm5n1, d1fm5n2, d1fm5n3, d1fm5n4)), result.get(0).getFoods().get(4));
		assertEquals("testId", result.get(0).getUsername());
		assertEquals(MealType.BREAKFAST, result.get(0).getMealType());

		assertEquals(FoodMenuToFoodInfo("짜장면", List.of(d2fm1n1, d2fm1n2, d2fm1n3, d2fm1n4)), result.get(1).getFoods().get(0));
		assertEquals(FoodMenuToFoodInfo("짬뽕", List.of(d2fm2n1, d2fm2n2, d2fm2n3, d2fm2n4)), result.get(1).getFoods().get(1));
		assertEquals(FoodMenuToFoodInfo("탕수육", List.of(d2fm3n1, d2fm3n2, d2fm3n3, d2fm3n4)), result.get(1).getFoods().get(2));
		assertEquals(FoodMenuToFoodInfo("유산슬", List.of(d2fm4n1, d2fm4n2, d2fm4n3, d2fm4n4)), result.get(1).getFoods().get(3));
		assertEquals("testId", result.get(1).getUsername());
		assertEquals(MealType.LUNCH, result.get(1).getMealType());
	}

	private NutrientResponse NutrientToNutrientResponse(Nutrient nutrient) {
		return new NutrientResponse(nutrient.getNutrientType(), nutrient.getAmount());
	}

	private FoodInfo FoodMenuToFoodInfo(String foodName, List<Nutrient> nutrients) {
		return new FoodInfo(foodName, nutrients.stream()
				.map(this::NutrientToNutrientResponse)
				.toList());
	}
}
