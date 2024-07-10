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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberInfoRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberJoinRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberUpdateRequest;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberInfoRepository;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;

/**
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("회원 서비스 로직 테스트")
class MemberServiceTest {

	@InjectMocks MemberService memberService;
	@Mock BCryptPasswordEncoder bCryptPasswordEncoder;
	@Mock MemberRepository memberRepository;
	@Mock MemberInfoRepository memberInfoRepository;

	@Test
	@DisplayName("회원가입 실패 테스트 (이미 존재하는 회원)")
	void joinTest1() {
	    // Given
		given(memberRepository.existsByUsername("testId")).willReturn(true);
		MemberJoinRequest request = new MemberJoinRequest("testId", "김철수", "1111");

	    // When

	    // Then
		assertThrows(IllegalArgumentException.class, () -> memberService.join(request));
		verify(memberRepository, never()).save(any());
	}

	@Test
	@DisplayName("회원가입 성공 테스트")
	void joinTest2() {
	    // Given
		MemberJoinRequest request = new MemberJoinRequest("testId", "김철수", "1111");

	    // When

	    // Then
		assertDoesNotThrow(() -> memberService.join(request));
		verify(memberRepository).save(any());
	}

	@Test
	@DisplayName("회원정보수정 실패 테스트 (회원정보를 찾을 수 없음)")
	void updateMemberTest1() {
	    // Given
		given(memberRepository.findByUsername("testId")).willReturn(Optional.empty());
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "박철수");

	    // When

	    // Then
		assertThrows(UsernameNotFoundException.class, () -> memberService.updateMember(request));
		verify(bCryptPasswordEncoder, never()).matches(any(), any());
	}

	@Test
	@DisplayName("회원정보수정 성공 테스트 (이름변경)")
	void updateMemberTest2() {
	    // Given
		Member member = Member.builder().username("testId").username("김철수").password("1111").build();
		given(memberRepository.findByUsername("testId")).willReturn(Optional.of(member));
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "박철수");

	    // When

	    // Then
		assertDoesNotThrow(() -> memberService.updateMember(request));
	}

	@Test
	@DisplayName("회원정보수정 실패 (비밀번호가 일치하지 않음)")
	void updateMemberTest3() {
	    // Given
		Member member = Member.builder().username("testId").username("김철수").password("1111").build();
		given(memberRepository.findByUsername("testId")).willReturn(Optional.of(member));
		given(bCryptPasswordEncoder.matches("3333", "1111")).willReturn(false);
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "3333", "2222");

	    // When

	    // Then
		assertThrows(IllegalArgumentException.class, () -> memberService.updateMember(request));
		verify(bCryptPasswordEncoder).matches(any(), any());
		verify(bCryptPasswordEncoder, never()).encode(any());
	}

	@Test
	@DisplayName("회원정보수정 성공 (비밀번호 변경)")
	void updateMemberTest4() {
		// Given
		Member member = Member.builder().username("testId").username("김철수").password("1111").build();
		given(memberRepository.findByUsername("testId")).willReturn(Optional.of(member));
		given(bCryptPasswordEncoder.matches("1111", "1111")).willReturn(true);
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "1111", "2222");

		// When

		// Then
		assertDoesNotThrow(() -> memberService.updateMember(request));
		verify(bCryptPasswordEncoder).matches(any(), any());
		verify(bCryptPasswordEncoder).encode("2222");
	}

	@Test
	@DisplayName("회원추가정보 입력 실패 테스트 (회원을 찾을 수 없음)")
	void addMemberInfoTest1() {
	    // Given
		given(memberRepository.findByUsername("testId")).willReturn(Optional.empty());
		MemberInfoRequest request = new MemberInfoRequest("testId", 73.1);

		// When

		// Then
		assertThrows(UsernameNotFoundException.class, () -> memberService.addMemberInfo(request));
		verify(memberInfoRepository, never()).save(any());
	}

	@Test
	@DisplayName("회원추가정보 입력 성공 테스트")
	void addMemberInfoTest2() {
		// Given
		Member member = Member.builder().username("testId").username("김철수").password("1111").build();
		given(memberRepository.findByUsername("testId")).willReturn(Optional.of(member));
		MemberInfoRequest request = new MemberInfoRequest("testId", 73.1);

		// When

		// Then
		assertDoesNotThrow(() -> memberService.addMemberInfo(request));
		verify(memberInfoRepository).save(any());
	}
}
