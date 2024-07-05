package kakao.mission3healthcare_backend.auth.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;

/**
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@DataJpaTest
@Transactional
@DisplayName("회원 Repository 테스트")
class MemberRepositoryTest {

	@Autowired EntityManager em;
	@Autowired MemberRepository memberRepository;

	@Test
	@DisplayName("회원 저장 테스트")
	void saveMemberTest() {
	    // Given

		// When
		Member member = Member.builder().username("testId").name("김철수").password("1111").build();
		memberRepository.save(member);

		Member result = memberRepository.findById(member.getId()).get();

		// Then
		assertEquals(result, member);
	}

	@Test
	@DisplayName("회원 수정 테스트")
	void updateMemberTest() {
	    // Given
		Member member = Member.builder().username("testId").name("김철수").password("1111").build();
		memberRepository.save(member);

	    // When
		Member beforeUpdate = memberRepository.findById(member.getId()).get();
		beforeUpdate.setName("수정후이름");
		beforeUpdate.setPassword("2222");

		em.flush();
		em.clear();

		Member result = memberRepository.findById(member.getId()).get();

		// Then
		assertEquals("수정후이름", result.getName());
		assertEquals("2222", result.getPassword());
	}

	@Test
	@DisplayName("회원 삭제 테스트")
	void deleteMemberTest() {
	    // Given
		Member member = Member.builder().username("testId").name("김철수").password("1111").build();
		memberRepository.save(member);

	    // When
		memberRepository.deleteById(member.getId());

	    // Then
		assertEquals(Optional.empty(), memberRepository.findById(member.getId()));
	}

	@Test
	@DisplayName("회원정보가 존재할때 true를 출력하는지 확인하는 테스트")
	void existByUsernameTest1() {
	    // Given
		Member member = Member.builder().username("testId").name("김철수").password("1111").build();
		memberRepository.save(member);

	    // When
		boolean result = memberRepository.existsByUsername("testId");

		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("회원정보가 없을때 false를 출력하는지 확인하는 테스트")
	void existByUsernameTest2() {
		// Given

		// When
		boolean result = memberRepository.existsByUsername("testId");

		// Then
		assertFalse(result);
	}
}
