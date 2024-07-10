package kakao.mission3healthcare_backend.auth.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kakao.mission3healthcare_backend.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 상세정보
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_info_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "member_id", updatable = false)
	private Member member;

	@Setter
	@Column(name = "goal_weight")
	private double goalWeight; // 목표 체중

	@Builder
	public MemberInfo(Member member, double goalWeight) {
		this.member = member;
		this.goalWeight = goalWeight;
	}
}
