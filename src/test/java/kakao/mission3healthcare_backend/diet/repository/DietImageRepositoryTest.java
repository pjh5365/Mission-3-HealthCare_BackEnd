package kakao.mission3healthcare_backend.diet.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;
import kakao.mission3healthcare_backend.diet.domain.MealType;
import kakao.mission3healthcare_backend.diet.domain.entity.Diet;
import kakao.mission3healthcare_backend.diet.domain.entity.DietImage;

/**
 * @author : parkjihyeok
 * @since : 2024/07/15
 */
@SpringBootTest
@Transactional
@DisplayName("식단 이미지 Repository")
@WithMockUser
class DietImageRepositoryTest {

	@Autowired EntityManager em;
	@Autowired DietImageRepository dietImageRepository;
	@Autowired DietRepository dietRepository;
	@Autowired MemberRepository memberRepository;

	@Test
	@DisplayName("사진 저장 및 조회 테스트")
	void saveImageTest() {
		// Given
		Member member = Member.builder().username("test").password("test!").build();
		memberRepository.save(member);

		Diet d = Diet.builder().member(member).dietDate(LocalDate.of(2024, 1, 1)).mealType(MealType.BREAKFAST).build();
		dietRepository.save(d);

		// When
		DietImage image = DietImage.builder().diet(d).imageName("파일_저장명.txt").build();
		dietImageRepository.save(image);

		// Then
		assertEquals(image, dietImageRepository.findByDietId(d.getId()).get());
		assertTrue(dietImageRepository.existsByImageName("파일_저장명.txt"));
	}

	@Test
	@DisplayName("사진 수정 테스트")
	void updateImagesTest() {
		// Given
		Member member = Member.builder().username("test").password("test!").build();
		memberRepository.save(member);

		Diet d = Diet.builder().member(member).dietDate(LocalDate.of(2024, 1, 1)).mealType(MealType.BREAKFAST).build();
		dietRepository.save(d);

		DietImage image = DietImage.builder().diet(d).imageName("파일_저장명.txt").build();
		dietImageRepository.save(image);

		// When
		image.setImageName("새로운_파일_저장명.txt");

		em.flush();
		em.clear();

		// Then
		assertEquals("새로운_파일_저장명.txt", dietImageRepository.findByDietId(image.getId()).get().getImageName());
	}

	@Test
	@DisplayName("사진 삭제 테스트")
	void deleteImageTest() {
		// Given
		Member member = Member.builder().username("test").password("test!").build();
		memberRepository.save(member);

		Diet d = Diet.builder().member(member).dietDate(LocalDate.of(2024, 1, 1)).mealType(MealType.BREAKFAST).build();
		dietRepository.save(d);

		DietImage image = DietImage.builder().diet(d).imageName("파일_저장명.txt").build();
		dietImageRepository.save(image);

		// When
		dietImageRepository.deleteByImageName("파일_저장명.txt");

		// Then
		assertEquals(Optional.empty(), dietImageRepository.findByDietId(d.getId()));
	}
}
