package kakao.mission3healthcare_backend.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kakao.mission3healthcare_backend.auth.domain.entity.Member;

/**
 * 회원 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUsername(String username);
}
