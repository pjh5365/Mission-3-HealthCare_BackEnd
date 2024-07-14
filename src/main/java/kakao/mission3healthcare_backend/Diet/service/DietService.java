package kakao.mission3healthcare_backend.diet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.common.service.CommonService;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietDeleteRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietUpdateRequest;
import kakao.mission3healthcare_backend.diet.domain.entity.Diet;
import kakao.mission3healthcare_backend.diet.domain.entity.Food;
import kakao.mission3healthcare_backend.diet.repository.DietRepository;
import kakao.mission3healthcare_backend.diet.repository.FoodRepository;
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
}
