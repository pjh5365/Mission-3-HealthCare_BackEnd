package kakao.mission3healthcare_backend.diet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kakao.mission3healthcare_backend.diet.domain.entity.Diet;

/**
 * 식단 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/07/11
 */
public interface DietRepository extends JpaRepository<Diet, Long> {

	@Query("select d from Diet d "
			+ "join fetch d.member m "
			+ "where m.username = :username")
	List<Diet> findByUsername(String username);

	@Query("select d from Diet d "
			+ "where d.member.username = :username and d.dietDate = :date")
	List<Diet> findByUsernameAndDate(@Param("username") String username, @Param("date") LocalDate date);
}
