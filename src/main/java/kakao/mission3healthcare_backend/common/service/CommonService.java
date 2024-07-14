package kakao.mission3healthcare_backend.common.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

/**
 * 공통으로 사용할 서비스단의 메서드를 모아놓은 클래스
 *
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@Service
@RequiredArgsConstructor
public class CommonService {

	private final MemberRepository memberRepository;

	/**
	 * 회원아이디로 회원정보를 찾아오는 메서드
	 *
	 * @param username    회원아이디
	 * @param checkAuth 요청자의 권한확인 요청 (true -> 권한확인, false -> 권한확인 X)
	 * @return 해당하는 회원정보가 있으면 회원정보를, 없다면 null 리턴
	 */
	public Member findMember(String username, boolean checkAuth) {

		if (checkAuth) { // 권한확인 요청이 들어오면
			checkAuth(username);
		}
		return memberRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));
	}

	/**
	 * 요청자가 접근 권한이 있는지 확인하는 메서드
	 *
	 * @param username 요청자가 가지고 있어야하는 회원ID
	 */
	public void checkAuth(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				if (!username.equals(((UserDetails)principal).getUsername())) { // 요청자가 권한이 없다면
					throw new IllegalAccessError("요청자의 권한을 확인할 수 없습니다.");
				}
			} else { // 시큐리티에서 권한을 꺼낼 수 없다면
				throw new IllegalAccessError("요청자의 권한을 확인할 수 없습니다.");
			}
		}
	}
}
