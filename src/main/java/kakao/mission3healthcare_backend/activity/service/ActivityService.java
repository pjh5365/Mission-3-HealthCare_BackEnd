package kakao.mission3healthcare_backend.activity.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkDeleteRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkUpdateRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.response.WalkResponse;
import kakao.mission3healthcare_backend.activity.domain.entity.Walk;
import kakao.mission3healthcare_backend.activity.repository.WalkRepository;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.common.service.CommonService;
import lombok.RequiredArgsConstructor;

/**
 * 활동을 담당하는 서비스
 *
 * @author : parkjihyeok
 * @since : 2024/07/24
 */
@Service
@RequiredArgsConstructor
public class ActivityService {

	private final WalkRepository walkRepository;
	private final CommonService commonService;

	/**
	 * 활동 ID로 걷기 정보를 불러오는 메서드
	 *
	 * @param id 활동 ID
	 * @return 결과
	 */
	public WalkResponse findById(Long id) {
		Walk walk = getWalk(id);

		return new WalkResponse(walk.getMember().getUsername(),
				walk.getDistance(), walk.getAvgHeartRate(), walk.getWalkDate());
	}

	/**
	 * 회원 정보로 걷기 정보를 모두 불러오는 메서드
	 *
	 * @param username 회원ID
	 * @return 결과
	 */
	public List<WalkResponse> findByUsername(String username) {
		return walkRepository.findByUsername(username)
				.stream()
				.map(w -> new WalkResponse(username, w.getDistance(), w.getAvgHeartRate(), w.getWalkDate())).toList();
	}

	/**
	 * 회원 정보와 날짜로 걷기 정보를 불러오는 메서드
	 *
	 * @param username 회원 ID
	 * @param date 날짜
	 * @return 결과
	 */
	public List<WalkResponse> findByUsernameAndDate(String username, LocalDate date) {
		return walkRepository.findByUsernameAndDate(username, date)
				.stream()
				.map(w -> new WalkResponse(username, w.getDistance(), w.getAvgHeartRate(), w.getWalkDate())).toList();
	}

	/**
	 * 걷기 정보를 저장하는 메서드
	 *
	 * @param request 걷기 정보
	 */
	public void saveWalk(WalkRequest request) {
		Member member = commonService.findMember(request.getUsername(), true);
		Walk walk = new Walk(request.getDistance(), request.getAvgHeartRate(), request.getWalkDate(), member);
		walkRepository.save(walk);
	}

	/**
	 * 걷기 정보 수정 메서드
	 *
	 * @param request 걷기 정보 수정에 대한 정보
	 */
	public void updateWalk(WalkUpdateRequest request) {
		commonService.findMember(request.getUsername(), true);
		Walk walk = getWalk(request.getId());
		walk.updateWalk(request.getDistance(), request.getAvgHeartRate(), request.getWalkDate());
	}

	/**
	 * 걷기 정보 삭제 메서드
	 *
	 * @param request 삭제 정보
	 */
	public void deleteWalk(WalkDeleteRequest request) {
		commonService.findMember(request.getUsername(), true);
		walkRepository.deleteById(request.getId());
	}

	/**
	 * 걷기 정보를 불러오는 메서드
	 *
	 * @param id 걷기 ID
	 * @return 걷기 엔티티
	 */
	private Walk getWalk(Long id) {
		return walkRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 활동(걷기)정보를 찾을 수 없습니다."));
	}
}
