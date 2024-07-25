package kakao.mission3healthcare_backend.activity.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkDeleteRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkUpdateRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.response.WalkResponse;
import kakao.mission3healthcare_backend.activity.domain.entity.Walk;
import kakao.mission3healthcare_backend.activity.repository.WalkRepository;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.common.service.CommonService;

/**
 * @author : parkjihyeok
 * @since : 2024/07/24
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("활동 서비스 테스트")
class ActivityServiceTest {

	@InjectMocks ActivityService activityService;
	@Mock CommonService commonService;
	@Mock WalkRepository walkRepository;

	@Test
	@DisplayName("걷기 추가 테스트 (실패 - 회원정보를 찾을 수 없음)")
	void saveWalkFail() {
		// Given
		WalkRequest request = new WalkRequest("testId", 3.4, 121.3, LocalDate.of(2024, 1, 1));
		given(commonService.findMember("testId", true)).willThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

		// When
		assertThrows(UsernameNotFoundException.class, () -> activityService.saveWalk(request));

		// Then
		verify(commonService).findMember("testId", true);
		verify(walkRepository, never()).save(any());
	}

	@Test
	@DisplayName("걷기 추가 테스트 (성공)")
	void saveWalk() {
		// Given
		WalkRequest request = new WalkRequest("testId", 3.4, 121.3, LocalDate.of(2024, 1, 1));
		Member member = Member.builder().username("testId").build();
		given(commonService.findMember("testId", true)).willReturn(member);

		// When
		assertDoesNotThrow(() -> activityService.saveWalk(request));

		// Then
		verify(commonService).findMember("testId", true);
		verify(walkRepository).save(any());
	}

	@Test
	@DisplayName("걷기 수정 테스트 (실패)")
	void updateWalkFail() {
		// Given
		WalkUpdateRequest request = new WalkUpdateRequest(100L, "testId", 3.4, 121.3, LocalDate.of(2024, 1, 1));
		given(commonService.findMember("testId", true)).willThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

		// When
		assertThrows(UsernameNotFoundException.class, () -> activityService.updateWalk(request));

		// Then
		verify(commonService).findMember("testId", true);
	}

	@Test
	@DisplayName("걷기 수정 테스트 (성공)")
	void updateWalk() {
		// Given
		WalkUpdateRequest request = new WalkUpdateRequest(100L, "testId", 3.4, 121.3, LocalDate.of(2024, 1, 10));
		Member member = Member.builder().username("testId").build();
		Walk walk = new Walk(3.0, 110.0, LocalDate.of(2024, 1, 1), member);
		given(commonService.findMember("testId", true)).willReturn(member);
		given(walkRepository.findById(100L)).willReturn(Optional.of(walk));

		// When
		assertDoesNotThrow(() -> activityService.updateWalk(request));

		// Then
		verify(commonService).findMember("testId", true);
		verify(walkRepository).findById(100L);
	}

	@Test
	@DisplayName("걷기 삭제 테스트 (실패)")
	void deleteWalkFail() {
		// Given
		WalkDeleteRequest request = new WalkDeleteRequest(10L, "testId");
		given(commonService.findMember("testId", true)).willThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

		// When
		assertThrows(UsernameNotFoundException.class, () -> activityService.deleteWalk(request));

		// Then
		verify(commonService).findMember("testId", true);
	}

	@Test
	@DisplayName("걷기 삭제 테스트 (성공)")
	void deleteWalk() {
		// Given
		WalkDeleteRequest request = new WalkDeleteRequest(10L, "testId");
		Member member = Member.builder().username("testId").build();
		given(commonService.findMember("testId", true)).willReturn(member);

		// When
		assertDoesNotThrow(() -> activityService.deleteWalk(request));

		// Then
		verify(commonService).findMember("testId", true);
		verify(walkRepository).deleteById(10L);
	}

	@Test
	@DisplayName("걷기 단건조회 테스트")
	void findById() {
		// Given
		Member member = Member.builder().username("testId").build();
		Walk walk = new Walk(3.0, 110.0, LocalDate.of(2024, 1, 1), member);

		given(walkRepository.findById(10L)).willReturn(Optional.of(walk));

		// When
		WalkResponse result = activityService.findById(10L);

		// Then
		assertEquals(3.0, result.getDistance());
		assertEquals(110.0, result.getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 1, 1), result.getWalkDate());
		assertEquals("testId", result.getUsername());
	}

	@Test
	@DisplayName("한 회원의 모든 식단 정보 반환 테스트")
	void getDietsTest() {
		// Given
		Member member = Member.builder().username("testId").build();
		Walk walk1 = new Walk(1.1, 110.0, LocalDate.of(2024, 1, 1), member);
		Walk walk2 = new Walk(2.2, 120.0, LocalDate.of(2024, 2, 1), member);
		Walk walk3 = new Walk(3.3, 130.0, LocalDate.of(2024, 3, 1), member);
		Walk walk4 = new Walk(4.4, 140.0, LocalDate.of(2024, 4, 1), member);
		Walk walk5 = new Walk(5.5, 150.0, LocalDate.of(2024, 5, 1), member);

		given(walkRepository.findByUsername("testId")).willReturn(List.of(walk1, walk2, walk3, walk4, walk5));

		// When
		List<WalkResponse> result = activityService.findByUsername("testId");

		// Then
		assertEquals(5, result.size());
		assertEquals(1.1, result.get(0).getDistance());
		assertEquals(110.0, result.get(0).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getWalkDate());
		assertEquals("testId", result.get(0).getUsername());

		assertEquals(2.2, result.get(1).getDistance());
		assertEquals(120.0, result.get(1).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 2, 1), result.get(1).getWalkDate());
		assertEquals("testId", result.get(1).getUsername());

		assertEquals(3.3, result.get(2).getDistance());
		assertEquals(130.0, result.get(2).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 3, 1), result.get(2).getWalkDate());
		assertEquals("testId", result.get(2).getUsername());

		assertEquals(4.4, result.get(3).getDistance());
		assertEquals(140.0, result.get(3).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 4, 1), result.get(3).getWalkDate());
		assertEquals("testId", result.get(3).getUsername());

		assertEquals(5.5, result.get(4).getDistance());
		assertEquals(150.0, result.get(4).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 5, 1), result.get(4).getWalkDate());
		assertEquals("testId", result.get(4).getUsername());
	}

	@Test
	@DisplayName("한 회원의 정보와 날짜 정보로 걷기 정보 불러오기")
	void findByUsernameAndDate() {
	    // Given
		Member member = Member.builder().username("testId").build();
		Walk walk1 = new Walk(1.1, 110.0, LocalDate.of(2024, 1, 1), member);
		Walk walk2 = new Walk(2.2, 120.0, LocalDate.of(2024, 1, 1), member);
		Walk walk3 = new Walk(3.3, 130.0, LocalDate.of(2024, 1, 1), member);
		Walk walk4 = new Walk(4.4, 140.0, LocalDate.of(2024, 1, 1), member);
		Walk walk5 = new Walk(5.5, 150.0, LocalDate.of(2024, 1, 1), member);

		given(walkRepository.findByUsernameAndDate("testId", LocalDate.of(2024, 1, 1))).willReturn(List.of(walk1, walk2, walk3, walk4, walk5));


		// When
		List<WalkResponse> result = activityService.findByUsernameAndDate("testId", LocalDate.of(2024, 1, 1));

	    // Then
		assertEquals(5, result.size());
		assertEquals(1.1, result.get(0).getDistance());
		assertEquals(110.0, result.get(0).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getWalkDate());
		assertEquals("testId", result.get(0).getUsername());

		assertEquals(2.2, result.get(1).getDistance());
		assertEquals(120.0, result.get(1).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 1, 1), result.get(1).getWalkDate());
		assertEquals("testId", result.get(1).getUsername());

		assertEquals(3.3, result.get(2).getDistance());
		assertEquals(130.0, result.get(2).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 1, 1), result.get(2).getWalkDate());
		assertEquals("testId", result.get(2).getUsername());

		assertEquals(4.4, result.get(3).getDistance());
		assertEquals(140.0, result.get(3).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 1, 1), result.get(3).getWalkDate());
		assertEquals("testId", result.get(3).getUsername());

		assertEquals(5.5, result.get(4).getDistance());
		assertEquals(150.0, result.get(4).getAvgHeartRate());
		assertEquals(LocalDate.of(2024, 1, 1), result.get(4).getWalkDate());
		assertEquals("testId", result.get(4).getUsername());
	}
}
