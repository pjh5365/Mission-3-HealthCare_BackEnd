package kakao.mission3healthcare_backend.diet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;
import kakao.mission3healthcare_backend.common.service.CommonService;
import kakao.mission3healthcare_backend.diet.domain.MealType;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietDeleteRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietUpdateRequest;
import kakao.mission3healthcare_backend.diet.domain.entity.Diet;
import kakao.mission3healthcare_backend.diet.repository.DietRepository;
import kakao.mission3healthcare_backend.diet.repository.FoodRepository;

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
	@Mock FoodRepository foodRepository;

	@Test
	@DisplayName("식단 추가 테스트 (실패 - 회원정보를 찾을 수 없음)")
	void addDietFail() {
	    // Given
		DietRequest dietRequest = new DietRequest("testId", MealType.BREAKFAST, List.of("김치", "라면"));
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
		DietRequest dietRequest = new DietRequest("testId", MealType.BREAKFAST, List.of("김치", "라면"));
		Member member = Member.builder().username("testId").build();
		given(commonService.findMember("testId", true)).willReturn(member);

	    // When
		assertDoesNotThrow(() -> dietService.addDiet(dietRequest));

	    // Then
		verify(commonService).findMember("testId", true);
		verify(dietRepository).save(any());
		verify(foodRepository, times(2)).save(any());
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
		Diet diet = new Diet(10L, member, MealType.BREAKFAST);
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
		Diet diet = new Diet(10L, member, MealType.BREAKFAST);
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
}
