package kakao.mission3healthcare_backend.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kakao.mission3healthcare_backend.auth.domain.entity.MemberInfo;

/**
 * 회원 상세정보 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/07/04
 */
public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
}
