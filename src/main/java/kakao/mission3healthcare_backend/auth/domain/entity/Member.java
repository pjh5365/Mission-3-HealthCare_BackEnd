package kakao.mission3healthcare_backend.auth.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kakao.mission3healthcare_backend.auth.domain.UserRole;
import kakao.mission3healthcare_backend.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 Entity
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(updatable = false)
	private String username; // 회원 ID

	@Setter
	private String name; // 회원 이름
	@Setter
	private String password; // 비밀번호

	@Enumerated(value = EnumType.STRING)
	private UserRole userRole;

	@Builder
	public Member(String username, String name, String password, UserRole userRole) {
		this.username = username;
		this.name = name;
		this.password = password;
		this.userRole = userRole;
	}
}
