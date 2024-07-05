package kakao.mission3healthcare_backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kakao.mission3healthcare_backend.common.response.ApiSingleResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 컨트롤러 단의 예외처리를 담당
 *
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

	/**
	 * 예외 처리 메서드
	 *
	 * @param e 예상한 예외
	 * @return 처리결과
	 */
	@ExceptionHandler({UsernameNotFoundException.class, IllegalArgumentException.class})
	public ResponseEntity<ApiSingleResponse<?>> exception(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiSingleResponse<>("실패", "결과를 확인하고 다시시도해주세요.", e.getMessage()));
	}

	/**
	 * 예상치 못한 예외 발생시 처리
	 *
	 * @param e 예외
	 * @return 처리결과
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> unexpectedException(Exception e) {
		log.error("예상치 못한 예외가 발생했습니다. 예외내용: {}", e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiSingleResponse<>("실패", "관리자에게 문의해주세요", e.getStackTrace()));
	}
}
