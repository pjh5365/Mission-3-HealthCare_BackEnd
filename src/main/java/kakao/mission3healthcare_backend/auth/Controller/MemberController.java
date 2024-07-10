package kakao.mission3healthcare_backend.auth.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberInfoRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberJoinRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberUpdateRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.response.MemberInfoResponse;
import kakao.mission3healthcare_backend.auth.domain.dto.response.MemberResponse;
import kakao.mission3healthcare_backend.auth.service.MemberService;
import kakao.mission3healthcare_backend.common.response.ApiSingleResponse;
import lombok.RequiredArgsConstructor;

/**
 * 회원 Controller
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	/**
	 * 회원가입 API
	 *
	 * @param request 회원가입에 필요한 정보
	 * @return 처리결과
	 */
	@PostMapping("/users")
	public ResponseEntity<ApiSingleResponse<MemberResponse>> join(@RequestBody MemberJoinRequest request) {
		MemberResponse response = memberService.join(request);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiSingleResponse<>("성공", "회원가입에 성공하였습니다.", response));
	}

	/**
	 * 회원정보수정 API
	 * @param request 회원정보수정에 필요한 정보
	 * @return 처리결과
	 */
	@PatchMapping("/users")
	public ResponseEntity<ApiSingleResponse<MemberResponse>> updateMember(@RequestBody MemberUpdateRequest request) {
		MemberResponse response = memberService.updateMember(request);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiSingleResponse<>("성공", "회원정보 수정에 성공하였습니다.", response));
	}

	/**
	 * 회원추가정보 입력 API
	 *
	 * @param request 추가정보입력에 필요한 정보
	 * @return 처리결과
	 */
	@PostMapping("/users/details")
	public ResponseEntity<ApiSingleResponse<MemberInfoResponse>> addMemberInfo(@RequestBody MemberInfoRequest request) {
		MemberInfoResponse response = memberService.addMemberInfo(request);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiSingleResponse<>("성공", "회원추가정보 입력에 성공하였습니다.", response));
	}
}
