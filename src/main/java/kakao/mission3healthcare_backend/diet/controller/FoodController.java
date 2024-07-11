package kakao.mission3healthcare_backend.Diet.Controller;

import kakao.mission3healthcare_backend.Diet.Domain.Dto.RegFoodDto;
import kakao.mission3healthcare_backend.Diet.Service.FoodService;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class FoodController {
    @Autowired
    FoodService foodService;

    public FoodController(FoodService foodService){
        this.foodService = foodService;
    }

    /**
     * 음식 검색 api
     *
     * @return 처리결과
     */

    @GetMapping("/foods")
    public GetResult get_Food(){
        return new GetResult(foodService.getFood());
    }

    /**
     * 음식 등록 api
     *
     * @param regFoodDto 음식 등록에 필요한 정보 dto
     * @return 처리결과
     */
    @PostMapping("/foods")
    public void red_Food(@RequestBody RegFoodDto regFoodDto){
        foodService.NutrientRegistration(foodService.FoodRegistration(regFoodDto), regFoodDto);
    }

    @Data
    static class GetResult<T>{
        private T data;
        public GetResult(T foods){
            this.data = foods;
        }
    }
}


