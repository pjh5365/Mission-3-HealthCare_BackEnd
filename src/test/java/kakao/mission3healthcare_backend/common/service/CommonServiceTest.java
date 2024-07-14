package kakao.mission3healthcare_backend.common.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;

/**
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("공통으로 사용되는 서비스 메서드 모음")
class CommonServiceTest {

	@InjectMocks CommonService commonService;
	@Mock MemberRepository memberRepository;
	@Mock SecurityContext securityContext;
	@Mock Authentication authentication;
	@Mock UserDetails userDetails;

	@BeforeEach
	void setUp() {
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	@DisplayName("회원 검색 테스트 - 실패")
	void findUserAccountFailTest() {
		// Given
		given(memberRepository.findByUsername("testId")).willReturn(Optional.empty());

		// When

		// Then
		assertThrows(UsernameNotFoundException.class, () -> commonService.findMember("testId", false));
	}

	@Test
	@DisplayName("회원 검색 테스트 - 성공")
	void findUserAccountSuccessTest() {
		// Given
		Member member = Member.builder().username("testId").build();
		given(memberRepository.findByUsername("testId")).willReturn(Optional.of(member));

		// When
		Member result = commonService.findMember("testId", false);

		// Then
		assertEquals(member, result);
	}

	@Test
	@DisplayName("요청자의 권한이 있는지 테스트 - 실패")
	void checkAuthTest1() {
		// Given
		given(securityContext.getAuthentication()).willReturn(authentication);
		given(authentication.isAuthenticated()).willReturn(true);
		given(authentication.getPrincipal()).willReturn(userDetails);
		given(userDetails.getUsername()).willReturn("testId");

		// When

		// Then
		assertThrows(IllegalAccessError.class, () -> commonService.checkAuth("testI"));
	}

	@Test
	@DisplayName("요청자가 권한이 있는지 테스트 - 성공")
	void checkAuthTest2() {
		// Given
		given(securityContext.getAuthentication()).willReturn(authentication);
		given(authentication.isAuthenticated()).willReturn(true);
		given(authentication.getPrincipal()).willReturn(userDetails);
		given(userDetails.getUsername()).willReturn("testId");

		// When

		// Then
		assertDoesNotThrow(() -> commonService.checkAuth("testId"));
	}
}
