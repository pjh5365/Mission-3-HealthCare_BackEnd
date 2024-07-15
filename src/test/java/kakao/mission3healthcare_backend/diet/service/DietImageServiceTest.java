package kakao.mission3healthcare_backend.diet.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

/**
 * @author : parkjihyeok
 * @since : 2024/07/15
 */
@DisplayName("이미지 서비스 테스트")
class DietImageServiceTest {

	String IMAGE_PATH = "";
	DietImageService dietImageService = new DietImageService();

	@Test
	@DisplayName("이미지 저장 테스트")
	void saveImageTest() {
	    // Given
		MockMultipartFile file = new MockMultipartFile("test", "Test.png", "image/png", "사진내용".getBytes());

		// When
		String fileName = dietImageService.saveFile(file, IMAGE_PATH);

		// Then
		File saved = new File(IMAGE_PATH + fileName);
		Path result = Paths.get(IMAGE_PATH + fileName);

		assertAll(
				() -> assertArrayEquals(file.getBytes(), Files.readAllBytes(result)),
				() -> assertEquals(fileName, saved.getName()),
				() -> assertTrue(saved.exists()),
				() -> assertTrue(saved.canRead())
		);

		saved.delete(); // 파일 삭제
	}

	@Test
	@DisplayName("이미지 삭제 테스트")
	void deleteImageTest() {
		// Given
		MockMultipartFile file = new MockMultipartFile("test", "Test.png", "image/png", "사진내용".getBytes());
		String fileName = dietImageService.saveFile(file, IMAGE_PATH);

		// When
		dietImageService.deleteImage(fileName, IMAGE_PATH);

		// Then
		Path result = Paths.get(IMAGE_PATH + fileName);
		// 파일이 존재하지 않으므로 NoSuchFileException 예외를 던져야한다.
		assertThrows(NoSuchFileException.class, () -> Files.readAllBytes(result));
	}
}
