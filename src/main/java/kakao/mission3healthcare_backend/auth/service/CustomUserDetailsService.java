package kakao.mission3healthcare_backend.auth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kakao.mission3healthcare_backend.auth.domain.CustomUserDetails;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

/**
 * UserDetailsService 구현 클래스
 * 사용자의 로그인 과정을 처리한다.
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Member> member = memberRepository.findByUsername(username);
		if (member.isEmpty()) {
			throw new UsernameNotFoundException("해당 사용자 정보를 찾을 수 없습니다.");
		}
		return new CustomUserDetails(member.get());
	}
}
