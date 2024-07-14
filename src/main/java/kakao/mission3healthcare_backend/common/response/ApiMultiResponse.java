package kakao.mission3healthcare_backend.common.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 서비스가 반환하는 다중 객체(List)를 담아 클라이언트에 보내기 위한 객체
 *
 * @author : parkjihyeok
 * @since : 2024/07/11
 */
@Getter
@AllArgsConstructor
public class ApiMultiResponse<T> {

	private String status; // 응답 코드에 대한 정보
	private String message; // 응답 메시지에 대한 정보
	private List<T> data; // 응답객체를 공통으로 사용하기 위해 제네릭으로 받아 처리하기
}
