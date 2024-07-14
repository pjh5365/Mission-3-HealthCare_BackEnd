package kakao.mission3healthcare_backend.diet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.common.service.CommonService;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietDeleteRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietUpdateRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.response.DietResponse;
import kakao.mission3healthcare_backend.diet.domain.dto.response.FoodInfo;
import kakao.mission3healthcare_backend.diet.domain.dto.response.NutrientResponse;
import kakao.mission3healthcare_backend.diet.domain.entity.Diet;
import kakao.mission3healthcare_backend.diet.domain.entity.Food;
import kakao.mission3healthcare_backend.diet.domain.entity.Nutrient;
import kakao.mission3healthcare_backend.diet.repository.DietRepository;
import kakao.mission3healthcare_backend.diet.repository.FoodRepository;
import kakao.mission3healthcare_backend.diet.repository.NutrientRepository;
import lombok.RequiredArgsConstructor;

/**
 * 식단을 담당하는 서비스
 *
 * @author : parkjihyeok
 * @since : 2024/07/13
 */
@Service
@RequiredArgsConstructor
public class DietService {

	private final CommonService commonService;
	private final DietRepository dietRepository;
	private final FoodRepository foodRepository;
	private final NutrientRepository nutrientRepository;

	/**
	 * 한 사용자의 모든 식단 정보를 담아 반환하는 메서드
	 *
	 * @param username 회원ID
	 * @return 회원이 기록한 모든 식단정보
	 */
	public List<DietResponse> getDiets(String username) {
		return dietRepository.findByUsername(username)
				.stream().map(this::getDiet)
				.toList();
	}

	/**
	 * 한 식단에 대한 종합정보를 담아 반환하는 메서드
	 *
	 * @param diet 식단정보
	 * @return 식단에 대한 종합정보
	 */
	public DietResponse getDiet(Diet diet) {
		List<FoodInfo> foodInfos = foodRepository.findByDietId(diet.getId())
				.stream()
				.map(Food::getFoodName) // 음식 이름들을 가져와서
				.map(this::getFoodInfo) // 각 음식에 대한 정보를 가져온다.
				.toList();

		return new DietResponse(diet.getMember().getUsername(), diet.getMealType(), foodInfos, diet.getDietDate());
	}

	/**
	 * 식단을 추가하는 메서드
	 *
	 * @param dietRequest 식단에 대한 정보
	 */
	@Transactional
	public void addDiet(DietRequest dietRequest) {
		Member member = commonService.findMember(dietRequest.getUsername(), true);

		Diet diet = Diet.builder()
				.member(member)
				.mealType(dietRequest.getMealType())
				.dietDate(dietRequest.getDietDate())
				.build();

		dietRepository.save(diet);

		dietRequest.getFoodNames()
				.forEach(f -> foodRepository.save(
						Food.builder()
								.diet(diet)
								.foodName(f)
								.build()
				));
	}

	/**
	 * 식단 수정 메서드
	 *
	 * @param updateRequest 식단 수정에 대한 정보
	 */
	@Transactional
	public void updateDiet(DietUpdateRequest updateRequest) {
		Member member = commonService.findMember(updateRequest.getUsername(), true);
		Diet diet = findDiet(updateRequest.getDietId());

		List<Food> foods = foodRepository.findByDietId(updateRequest.getDietId());
		foodRepository.deleteAll(foods); // 연결된 음식정보를 모두 제거하고

		// 새로운 음식들로 새롭게 저장한다.
		updateRequest.getFoodNames()
				.forEach(f -> foodRepository.save(
						Food.builder()
								.diet(diet)
								.foodName(f)
								.build()
				));
	}

	/**
	 * 식단 삭제 메서드
	 *
	 * @param deleteRequest 식단 삭제에 대한 요청
	 */
	@Transactional
	public void deleteDiet(DietDeleteRequest deleteRequest) {
		commonService.findMember(deleteRequest.getUsername(), true); // 삭제 권한이 있는지 확인
		Diet diet = findDiet(deleteRequest.getId());
		foodRepository.deleteAll(foodRepository.findByDietId(deleteRequest.getId()));
		dietRepository.delete(diet);
	}

	/**
	 * 식단을 찾아오는 메서드
	 *
	 * @param id 식단 ID
	 * @return 식단
	 */
	private Diet findDiet(Long id) {
		return dietRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 식단 정보를 찾을 수 없습니다."));
	}

	/**
	 * 음식에 대한 세부정보를 반환하는 메서드
	 *
	 * @param foodName 음식이름
	 * @return 음식에 대한 세부정보를 담고있는 객체
	 */
	private FoodInfo getFoodInfo(String foodName) {
		List<NutrientResponse> nutrients = nutrientRepository
				.findByFoodName(foodName) // 음식이름으로 영양소 정보들을 가져온다.
				.stream()
				.map(this::nutrientToDto) // 영양소 정보를 응답하기 위한 객체로 변경한다.
				.toList();

		return new FoodInfo(foodName, nutrients);
	}

	/**
	 * 영양소 엔티티를 DTO로 변경해주는 메서드
	 *
	 * @param nutrient 영양소 엔티티
	 * @return 반환할 DTO
	 */
	private NutrientResponse nutrientToDto(Nutrient nutrient) {
		return new NutrientResponse(nutrient.getNutrientType(), nutrient.getAmount());
	}
}
