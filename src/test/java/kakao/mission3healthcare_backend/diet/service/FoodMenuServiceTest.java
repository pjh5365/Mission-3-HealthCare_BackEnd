package kakao.mission3healthcare_backend.diet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import kakao.mission3healthcare_backend.diet.domain.dto.request.NutrientRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.SaveFoodRequest;
import kakao.mission3healthcare_backend.diet.domain.entity.FoodMenu;
import kakao.mission3healthcare_backend.diet.repository.FoodMenuRepository;
import kakao.mission3healthcare_backend.diet.repository.NutrientRepository;

/**
 * @author : parkjihyeok
 * @since : 2024/07/13
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("음식메뉴 서비스 테스트")
class FoodMenuServiceTest {
	@InjectMocks FoodMenuService foodMenuService;
	@Mock FoodMenuRepository foodMenuRepository;
	@Mock NutrientRepository nutrientRepository;

	@Test
	@DisplayName("저장된 음식 리스트를 반환하는 메서드 테스트")
	void getFoodTest() {
	    // Given
		FoodMenu f1 = new FoodMenu(1L, "햄버거");
		FoodMenu f2 = new FoodMenu(2L, "감자튀김");
		FoodMenu f3 = new FoodMenu(3L, "짜장면");
		FoodMenu f4 = new FoodMenu(4L, "탕수육");
		FoodMenu f5 = new FoodMenu(5L, "짬뽕");
		FoodMenu f6 = new FoodMenu(6L, "우동");
		FoodMenu f7 = new FoodMenu(7L, "김치");
		FoodMenu f8 = new FoodMenu(8L, "라면");
		given(foodMenuRepository.findAll()).willReturn(List.of(f1, f2, f3, f4, f5, f6, f7, f8));

	    // When
		List<String> result = foodMenuService.getFood();

		// Then
		assertEquals("햄버거", result.get(0));
		assertEquals("감자튀김", result.get(1));
		assertEquals("짜장면", result.get(2));
		assertEquals("탕수육", result.get(3));
		assertEquals("짬뽕", result.get(4));
		assertEquals("우동", result.get(5));
		assertEquals("김치", result.get(6));
		assertEquals("라면", result.get(7));
	}

	@Test
	@DisplayName("음식메뉴 저장 테스트")
	void saveFoodTest() {
	    // Given
		NutrientRequest n1 = new NutrientRequest(NutrientType.PROTEIN, 10.0);
		NutrientRequest n2 = new NutrientRequest(NutrientType.IRON, 13.0);
		NutrientRequest n3 = new NutrientRequest(NutrientType.FAT, 100.0);
		SaveFoodRequest saveFoodRequest = new SaveFoodRequest("햄버거", List.of(n1, n2, n3));

	    // When
		foodMenuService.saveFoodMenu(saveFoodRequest);

	    // Then
		verify(foodMenuRepository).save(any());
		verify(nutrientRepository, times(3)).save(any());
	}
}
