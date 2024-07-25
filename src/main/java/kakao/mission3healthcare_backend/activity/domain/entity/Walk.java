package kakao.mission3healthcare_backend.activity.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 걷기 Entity
 *
 * @author : parkjihyeok
 * @since : 2024/07/24
 */
@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("walk")
public class Walk extends Activity {

	@Column(nullable = false)
	private Double distance; // 거리
	@Column(name = "avg_heart_rate", nullable = false)
	private Double avgHeartRate; // 평균 심박수
	@Column(nullable = false)
	private LocalDate walkDate; // 활동 날짜

	public void updateWalk(Double distance, Double avgHeartRate, LocalDate walkDate) {
		this.distance = distance;
		this.avgHeartRate = avgHeartRate;
		this.walkDate = walkDate;
	}

	public Walk(Double distance, Double avgHeartRate, LocalDate walkDate, Member member) {
		super(member);
		this.distance = distance;
		this.avgHeartRate = avgHeartRate;
		this.walkDate = walkDate;
	}
}
