package kakao.mission3healthcare_backend.diet.domain.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 음식 추가를 위한 요청 클래스
 */
@Getter
@AllArgsConstructor
public class SaveFoodRequest {

	String foodName;
	List<NutrientRequest> nutrientRequests;
}
