package kakao.mission3healthcare_backend.activity.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkDeleteRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkUpdateRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.response.WalkResponse;
import kakao.mission3healthcare_backend.activity.service.ActivityService;
import kakao.mission3healthcare_backend.common.response.ApiMultiResponse;
import kakao.mission3healthcare_backend.common.response.ApiSingleResponse;
import lombok.RequiredArgsConstructor;

/**
 * 활동을 담당하는 컨트롤러
 *
 * @author : parkjihyeok
 * @since : 2024/07/25
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ActivityController {

	private final ActivityService activityService;

	/**
	 * 한 사용자의 걷기 정보를 불러오는 API
	 *
	 * @param username 회원 ID
	 * @return 조회결과
	 */
	@GetMapping("/activities/{username}")
	public ResponseEntity<ApiMultiResponse<WalkResponse>> getWalks(@PathVariable String username) {
		List<WalkResponse> responses = activityService.findByUsername(username);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiMultiResponse<>("성공", username + "의 걷기 정보", responses));
	}

	/**
	 * 한 사용자의 특정 날짜의 걷기 정보를 불러오는 API
	 *
	 * @param username 회원ID
	 * @param date 조회할 날짜
	 * @return 조회결과
	 */
	@GetMapping("/activities/{username}/{date}")
	public ResponseEntity<ApiMultiResponse<WalkResponse>> getWalksByDate(@PathVariable String username,
			@PathVariable LocalDate date) {

		List<WalkResponse> responses = activityService.findByUsernameAndDate(username, date);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiMultiResponse<>("성공", username + "의 " + date + " 날짜의 걷기 정보", responses));
	}

	/**
	 * 걷기 정보 단건 조회
	 * @param id 조회할 걷기 정보 ID
	 * @return 조회결과
	 */
	@GetMapping("/activities")
	public ResponseEntity<ApiSingleResponse<WalkResponse>> getWalk(Long id) {
		WalkResponse response = activityService.findById(id);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiSingleResponse<>("성공", "id = " + id + "의 걷기 정보", response));
	}

	/**
	 * 걷기 정보 입력
	 *
	 * @param request 저장 요청
	 * @return 처리결과
	 */
	@PostMapping("/activities")
	public ResponseEntity<ApiSingleResponse<String>> saveWalk(@RequestBody WalkRequest request) {
		activityService.saveWalk(request);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiSingleResponse<>("성공", "걷기 정보 추가에 성공하였습니다.", ""));
	}

	/**
	 * 걷기 정보 수정
	 *
	 * @param request 수정 요청
	 * @return 처리결과
	 */
	@PatchMapping("/activities")
	public ResponseEntity<ApiSingleResponse<String>> updateWalk(@RequestBody WalkUpdateRequest request) {
		activityService.updateWalk(request);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiSingleResponse<>("성공", "걷기 정보 수정에 성공하였습니다.", ""));
	}

	/**
	 * 걷기 정보 삭제
	 *
	 * @param request 삭제 요청
	 * @return 처리결과
	 */
	@DeleteMapping("/activities")
	public ResponseEntity<ApiSingleResponse<String>> deleteWalk(@RequestBody WalkDeleteRequest request) {
		activityService.deleteWalk(request);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiSingleResponse<>("성공", "걷기 정보 삭제에 성공하였습니다.", ""));
	}
}
