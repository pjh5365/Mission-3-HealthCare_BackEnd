package kakao.mission3healthcare_backend.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;

/**
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("로그인 과정이 정상적으로 동작하는지 테스트")
class CustomUserDetailsServiceTest {

	@InjectMocks CustomUserDetailsService customUserDetailsService;
	@Mock MemberRepository memberRepository;

	@Test
	@DisplayName("회원정보를 찾아 로그인에 성공하는 테스트")
	void findUserTest() {
	    // Given
		Member member = Member.builder().username("testId").name("김철수").password("111").build();
		given(memberRepository.findByUsername("testId")).willReturn(Optional.of(member));

	    // When
		UserDetails result = customUserDetailsService.loadUserByUsername("testId");

		// Then
		assertEquals("testId", result.getUsername());
		assertEquals("111", result.getPassword());
	}

	@Test
	@DisplayName("회원정보를 찾지못해 로그인에 실패하는 테스트")
	void findUserFailTest() {
	    // Given

	    // When
		assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("testId"));

	    // Then
	}
}
