package kakao.mission3healthcare_backend.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kakao.mission3healthcare_backend.auth.domain.UserRole;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberInfoRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberJoinRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberUpdateRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.response.MemberInfoResponse;
import kakao.mission3healthcare_backend.auth.domain.dto.response.MemberResponse;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.domain.entity.MemberInfo;
import kakao.mission3healthcare_backend.auth.repository.MemberInfoRepository;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 Service
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MemberRepository memberRepository;
	private final MemberInfoRepository memberInfoRepository;

	/**
	 * 회원가입 메서드
	 *
	 * @param request 회원가입에 필요한 정보
	 */
	public MemberResponse join(MemberJoinRequest request) {
		// 해당 ID로 가입된 회원이 이미 존재한다면
		if (memberRepository.existsByUsername(request.getUsername())) {
			log.info("이미 존재하는 ID로 회원가입 요청 username: {}", request.getUsername());
			throw new IllegalArgumentException("이미 존재하는 ID입니다.");
		}

		Member member = Member.builder()
				.username(request.getUsername())
				.name(request.getName())
				.password(bCryptPasswordEncoder.encode(request.getPassword()))
				.userRole(UserRole.ROLE_USER)
				.build();

		memberRepository.save(member);
		log.info("username: [{}] 의 새로운 회원이 회원가입에 성공했습니다.", request.getUsername());

		return new MemberResponse(member.getUsername(), member.getName());
	}

	/**
	 * 회원정보수정 메서드
	 *
	 * @param request 회원정보수정에 필요한 정보
	 */
	public MemberResponse updateMember(MemberUpdateRequest request) {
		Member member = findMember(request.getUsername());

		// 이름 변경이라면
		if (request.getName() != null) {
			member.setName(request.getName());
		} else { // 비밀번호 변경이라면
			// 예전 비밀번호가 일치한다면
			if (bCryptPasswordEncoder.matches(request.getBeforePassword(), member.getPassword())) {
				log.info("[{}] 의 비밀번호 변경완료", request.getUsername());
				member.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
			} else {
				log.info("[{}] 의 비밀번호가 일치하지 않습니다.", request.getUsername());
				throw new IllegalArgumentException("비밀번호가 일치하지 않습니다!");
			}
		}

		return new MemberResponse(member.getUsername(), member.getName());
	}

	/**
	 * 회원의 추가 정보를 입력하는 메서드
	 *
	 * @param request 회원의 추가정보입력에 필요한 정보
	 */
	public MemberInfoResponse addMemberInfo(MemberInfoRequest request) {
		Member member = findMember(request.getUsername());

		MemberInfo memberInfo = MemberInfo.builder()
				.member(member)
				.goalWeight(request.getGoalWeight())
				.build();

		memberInfoRepository.save(memberInfo);
		log.info("[{}] (이/가) 목표체중을 {}kg 으로 설정하였습니다.", request.getUsername(), request.getGoalWeight());

		return new MemberInfoResponse(member.getUsername(), memberInfo.getGoalWeight());
	}

	/**
	 * 가입된 회원을 가져오는 메서드
	 *
	 * @param username 회원ID
	 * @return 가입된 회원 객체
	 */
	private Member findMember(String username) {
		return memberRepository.findByUsername(username)
				.orElseThrow(() -> {
					log.info("[{}] 의 회원정보를 찾을 수 없습니다.", username);
					return new UsernameNotFoundException("회원 정보를 찾을 수 없습니다.");
				});
	}
}
