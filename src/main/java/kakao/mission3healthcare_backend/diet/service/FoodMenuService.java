package kakao.mission3healthcare_backend.diet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kakao.mission3healthcare_backend.diet.domain.dto.request.SaveFoodRequest;
import kakao.mission3healthcare_backend.diet.domain.entity.FoodMenu;
import kakao.mission3healthcare_backend.diet.domain.entity.Nutrient;
import kakao.mission3healthcare_backend.diet.repository.FoodMenuRepository;
import kakao.mission3healthcare_backend.diet.repository.NutrientRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodMenuService {

	private final FoodMenuRepository foodMenuRepository;
	private final NutrientRepository nutrientRepository;

	/**
	 * 영양소와 함께 저장되어 있는 음식 리스트를 반환하는 메서드
	 *
	 * @return 음식 리스트
	 */
	public List<String> getFood() {
		List<FoodMenu> result = foodMenuRepository.findAll();

		return result.stream()
				.map(FoodMenu::getFoodName)
				.toList();
	}

	/**
	 * 영양소와 함께 음식메뉴를 저장하는 메서드
	 *
	 * @param saveFoodRequest 음식메뉴명과 영양소 정보
	 */
	@Transactional
	public void saveFoodMenu(SaveFoodRequest saveFoodRequest) {
		FoodMenu foodMenu = FoodMenu.builder().foodName(saveFoodRequest.getFoodName()).build();
		foodMenuRepository.save(foodMenu);
		saveFoodRequest.getNutrientRequests()
				.forEach(n -> nutrientRepository.save(
						Nutrient.builder()
								.foodMenu(foodMenu)
								.nutrientType(n.getNutrientType())
								.amount(n.getAmount())
								.build()
				));
	}
}
