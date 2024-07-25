package kakao.mission3healthcare_backend.activity.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import kakao.mission3healthcare_backend.activity.domain.entity.Walk;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;

/**
 * @author : parkjihyeok
 * @since : 2024/07/24
 */
@SpringBootTest
@Transactional
@DisplayName("활동(걷기) Repository")
@WithMockUser
class WalkRepositoryTest {

	@Autowired WalkRepository walkRepository;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;

	@Test
	@DisplayName("활동(걷기) 저장 테스트")
	void saveTest() {
		// Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);

		Walk walk = new Walk(1.4, 141.2, LocalDate.of(2024, 1, 1), member);

		// When
		walkRepository.save(walk);
		List<Walk> results = walkRepository.findByUsername("testId");
		Walk getWalk = results.get(0);
		Walk result = walkRepository.findById(walk.getId()).get();

		// Then
		assertEquals(1, results.size());
		assertEquals(walk, getWalk);
		assertEquals(walk, result);
	}

	@Test
	@DisplayName("사용자의 활동(걷기)정보를 불러오는 테스트")
	void findByUsernameTest() {
		// Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);

		Walk walk1 = new Walk(1.4, 141.2, LocalDate.of(2024, 1, 1), member);
		Walk walk2 = new Walk(2.4, 142.2, LocalDate.of(2024, 1, 2), member);
		Walk walk3 = new Walk(3.4, 143.2, LocalDate.of(2024, 1, 3), member);
		Walk walk4 = new Walk(4.4, 144.2, LocalDate.of(2024, 1, 4), member);
		Walk walk5 = new Walk(5.4, 145.2, LocalDate.of(2024, 1, 5), member);

		walkRepository.save(walk1);
		walkRepository.save(walk2);
		walkRepository.save(walk3);
		walkRepository.save(walk4);
		walkRepository.save(walk5);

		// When
		List<Walk> result = walkRepository.findByUsername("testId");

		// Then
		assertEquals(5, result.size());
		assertEquals(3.4, result.get(2).getDistance());
	}

	@Test
	@DisplayName("회원ID와 날짜로 활동(걷기) 조회")
	void DietRepositoryTest() {
		// Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);

		Walk walk1 = new Walk(1.4, 141.2, LocalDate.of(2024, 1, 1), member);
		Walk walk2 = new Walk(2.4, 142.2, LocalDate.of(2024, 1, 1), member);
		Walk walk3 = new Walk(3.4, 143.2, LocalDate.of(2024, 1, 1), member);
		Walk walk4 = new Walk(4.4, 144.2, LocalDate.of(2024, 1, 1), member);
		Walk walk5 = new Walk(5.4, 145.2, LocalDate.of(2024, 1, 1), member);

		walkRepository.save(walk1);
		walkRepository.save(walk2);
		walkRepository.save(walk3);
		walkRepository.save(walk4);
		walkRepository.save(walk5);

		// When
		List<Walk> result = walkRepository.findByUsernameAndDate("testId", LocalDate.of(2024, 1, 1));

		// Then
		assertEquals(5, result.size());
		assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getWalkDate());
		assertEquals(141.2, result.get(0).getAvgHeartRate());
		assertEquals(142.2, result.get(1).getAvgHeartRate());
		assertEquals(143.2, result.get(2).getAvgHeartRate());
		assertEquals(144.2, result.get(3).getAvgHeartRate());
		assertEquals(145.2, result.get(4).getAvgHeartRate());
	}

	@Test
	@DisplayName("활동(걷기) 수정 테스트")
	void updateTest() {
		// Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);
		Walk walk = new Walk(1.4, 141.2, LocalDate.of(2024, 1, 1), member);
		walkRepository.save(walk);

		// When
		Walk getWalk = walkRepository.findById(walk.getId()).get();
		getWalk.updateWalk(10.0, 20.0, LocalDate.of(2025, 1, 1));

		em.flush();
		em.clear();

		Walk result = walkRepository.findById(getWalk.getId()).get();

		// Then
		assertEquals(10.0, result.getDistance());
		assertEquals(20.0, result.getAvgHeartRate());
		assertEquals(LocalDate.of(2025, 1, 1), result.getWalkDate());
	}

	@Test
	@DisplayName("활동(걷기) 삭제 테스트")
	void deleteTest() {
		// Given
		Member member = Member.builder().username("testId").build();
		memberRepository.save(member);

		Walk walk = new Walk(1.4, 141.2, LocalDate.of(2024, 1, 1), member);
		walkRepository.save(walk);

		// When
		walkRepository.deleteById(walk.getId());

		// Then
		assertEquals(Optional.empty(), walkRepository.findById(walk.getId()));
	}
}
