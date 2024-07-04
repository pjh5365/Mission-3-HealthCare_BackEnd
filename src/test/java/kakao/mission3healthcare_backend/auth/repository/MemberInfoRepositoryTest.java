package kakao.mission3healthcare_backend.auth.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.domain.entity.MemberInfo;

/**
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@SpringBootTest
@Transactional
@DisplayName("회원 상세정보 테스트")
class MemberInfoRepositoryTest {

	@Autowired EntityManager em;
	@Autowired MemberRepository memberRepository;
	@Autowired MemberInfoRepository memberInfoRepository;


	@Test
	@DisplayName("회원 상세정보 저장 테스트")
	@WithMockUser("test")
	void saveMemberInfoTest() {
	    // Given
		Member member = Member.builder().username("testId").name("김철수").password("1111").build();
		memberRepository.save(member);

	    // When
		MemberInfo memberInfo = MemberInfo.builder().member(member).goalWeight(80).build();
		memberInfoRepository.save(memberInfo);

		MemberInfo result = memberInfoRepository.findById(memberInfo.getId()).get();

		// Then
		assertEquals(member.getId(), result.getMember().getId());
		assertEquals(memberInfo, result);
	}

	@Test
	@DisplayName("회원 상세정보 수정 테스트")
	@WithMockUser("test")
	void updateMemberInfoTest() {
	    // Given
		Member member = Member.builder().username("testUser").name("김철수").password("1111").build();
		memberRepository.save(member);

		MemberInfo memberInfo = MemberInfo.builder().member(member).goalWeight(80).build();
		memberInfoRepository.save(memberInfo);

		// When
		MemberInfo beforeMemberInfo = memberInfoRepository.findById(memberInfo.getId()).get();
		beforeMemberInfo.setGoalWeight(75);

		em.flush();
		em.clear();

		MemberInfo result = memberInfoRepository.findById(memberInfo.getId()).get();

		// Then
		assertEquals(member.getId(), result.getMember().getId());
		assertEquals(75, result.getGoalWeight());
	}

	@Test
	@DisplayName("회원 상세정보 삭제 테스트")
	@WithMockUser("test")
	void deleteMemberInfoTest() {
		// Given
		Member member = Member.builder().username("testUser").name("김철수").password("1111").build();
		memberRepository.save(member);

		MemberInfo memberInfo = MemberInfo.builder().member(member).goalWeight(80).build();
		memberInfoRepository.save(memberInfo);

		// When
		memberInfoRepository.delete(memberInfo);

		// Then
		assertEquals(Optional.empty(), memberInfoRepository.findById(memberInfo.getId()));
	}
}
