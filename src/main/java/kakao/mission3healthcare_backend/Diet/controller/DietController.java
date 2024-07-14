package kakao.mission3healthcare_backend.diet.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kakao.mission3healthcare_backend.common.response.ApiMultiResponse;
import kakao.mission3healthcare_backend.common.response.ApiSingleResponse;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietDeleteRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietUpdateRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.SaveFoodRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.response.DietResponse;
import kakao.mission3healthcare_backend.diet.service.DietService;
import kakao.mission3healthcare_backend.diet.service.FoodMenuService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FoodController {

	private final DietService dietService;
	private final FoodMenuService foodMenuService;

	/**
	 * 음식 검색 API
	 *
	 * @return 처리결과
	 */
	@GetMapping("/foods")
	public ApiMultiResponse<String> getFoods() {
		return new ApiMultiResponse<>("성공", "현재 저장되어 있는 음식메뉴들", foodMenuService.getFood());
	}

	/**
	 * 음식 메뉴 추가 API
	 *
	 * @param saveFoodRequest 음식메뉴 추가 요청
	 * @return 처리결과
	 */
	@PostMapping("/foods")
	public ApiSingleResponse<String> addFood(@RequestBody SaveFoodRequest saveFoodRequest) {
		foodMenuService.saveFoodMenu(saveFoodRequest);

		return new ApiSingleResponse<>("성공", "음식 메뉴 추가에 성공하였습니다.", "");
	}

	/**
	 * 한 회원의 식단 정보를 반환하는 API
	 *
	 * @param username 회원ID
	 * @return 식단 정보
	 */
	@GetMapping("/diets/{username}")
	public ApiMultiResponse<DietResponse> getDiets(@PathVariable String username) {
		return new ApiMultiResponse<>("성공", username + "의 식단 정보입니다.", dietService.getDiets(username));
	}

	/**
	 * 회원ID와 식단 날짜로 식단 정보를 검색하는 API
	 *
	 * @param username 회원ID
	 * @param date 식단 날짜
	 * @return 식단 정보
	 */
	@GetMapping("/diets/{username}")
	public ApiMultiResponse<DietResponse> getDietByUsernameAndDate(@PathVariable String username, LocalDate date) {
		return new ApiMultiResponse<>("성공", username + "의 식단 정보입니다.",
				dietService.getDietByUsernameAndDate(username, date));
	}

	/**
	 * 식단 ID로 식단을 검색하는 API
	 *
	 * @param id 식단 ID
	 * @return 식단 정보
	 */
	@GetMapping("/diets/{id}")
	public ApiSingleResponse<DietResponse> getDiet(@PathVariable Long id) {
		return new ApiSingleResponse<>("성공", "식단 정보입니다.",
				dietService.getDietById(id));
	}

	/**
	 * 식단 추가 API
	 *
	 * @param dietRequest 식단 추가 요청
	 * @return 처리결과
	 */
	@PostMapping("/diets")
	public ApiSingleResponse<String> addDiet(@RequestBody DietRequest dietRequest) {
		dietService.addDiet(dietRequest);
		return new ApiSingleResponse<>("성공", "식단 추가에 성공하였습니다.", "");
	}

	/**
	 * 식단 수정 API
	 *
	 * @param updateRequest 식단 수정 요청
	 * @return 처리결과
	 */
	@PatchMapping("/diets")
	public ApiSingleResponse<String> updateDiet(@RequestBody DietUpdateRequest updateRequest) {
		dietService.updateDiet(updateRequest);
		return new ApiSingleResponse<>("성공", "식단 수정에 성공하였습니다.", "");
	}

	/**
	 * 식단 삭제 API
	 *
	 * @param deleteRequest 식단 삭제 요청
	 * @return 처리결과
	 */
	@DeleteMapping("/diets")
	public ApiSingleResponse<String> deleteDiet(@RequestBody DietDeleteRequest deleteRequest) {
		dietService.deleteDiet(deleteRequest);
		return new ApiSingleResponse<>("성공", "식단 삭제에 성공하였습니다.", "");
	}
}
