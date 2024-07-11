package kakao.mission3healthcare_backend.Diet.Repository;

import kakao.mission3healthcare_backend.Diet.Domain.Entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    ArrayList<Food> findByFoodName(String food_name);
}

