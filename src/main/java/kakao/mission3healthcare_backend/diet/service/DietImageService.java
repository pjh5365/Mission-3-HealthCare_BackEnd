package kakao.mission3healthcare_backend.diet.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 식단 사진을 처리할 서비스
 *
 * @author : parkjihyeok
 * @since : 2024/07/15
 */
@Slf4j
@Service
public class DietImageService {

	/**
	 * 이미지 저장 메서드
	 *
	 * @param image 전달받은 이미지
	 * @param PATH 이미지 저장경로
	 * @return 저장에 성공한 이미지 파일명 리턴
	 */
	public String saveFile(MultipartFile image, String PATH) {
		String uuid = UUID.randomUUID().toString();
		String imageName = uuid + ":" + image.getOriginalFilename();
		String imagePath = PATH + imageName;
		File imageDestination = new File(imagePath);

		try {
			image.transferTo(imageDestination);
		} catch (IOException e) {
			log.error("이미지 업로드 실패 : {}", e.getMessage());
			return null;
		}
		return imageName;

	}

	/**
	 * 이미지 삭제 메서드
	 *
	 * @param imageName 삭제할 이미지 파일명
	 * @param PATH 이미지 저장경로
	 */
	public void deleteImage(String imageName, String PATH) {
		if (!new File(PATH + imageName).delete()) {
			log.error("이미지 삭제 실패 imageName = {}", imageName);
		}
	}
}
