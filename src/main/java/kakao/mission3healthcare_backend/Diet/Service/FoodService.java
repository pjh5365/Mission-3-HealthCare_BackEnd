package kakao.mission3healthcare_backend.Diet.Service;

import kakao.mission3healthcare_backend.Diet.Domain.Dto.RegFoodDto;
import kakao.mission3healthcare_backend.Diet.Domain.Entity.Food;
import kakao.mission3healthcare_backend.Diet.Domain.Entity.Nutrient;
import kakao.mission3healthcare_backend.Diet.Repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FoodService {
    @Autowired
    FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository){
        this.foodRepository = foodRepository;
    }

    public ArrayList<Food> getFood(){
        return (ArrayList<Food>) foodRepository.findAll();
    }

    public long FoodRegistration(RegFoodDto regFoodDto){
        Food food = Food.builder().
                foodName(regFoodDto.getFood_name())
                .diet_id(1)
                .build();

        this.foodRepository.save(food);

        return food.getFood_id();
    }

    public void NutrientRegistration( long food_id, RegFoodDto regFoodDto){
        Optional<Food> food = foodRepository.findById((int) food_id);

        if(food.isPresent()){
            System.out.println("음식 검색 완료");

        }else{
            throw new IllegalArgumentException("잘못된 음식 아이디 입니다.");
        }

        Food tmpFood = food.get();

        Nutrient nutrient = Nutrient.builder()
                .amount(regFoodDto.getAmount())
                .food_id(food_id)
                .build();

        tmpFood.addNutrient(nutrient);

        this.foodRepository.save(tmpFood);
    }
}
