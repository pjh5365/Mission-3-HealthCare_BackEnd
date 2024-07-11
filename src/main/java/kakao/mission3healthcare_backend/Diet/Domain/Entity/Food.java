package kakao.mission3healthcare_backend.Diet.Domain.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long food_id;

    @Column
    private long diet_id;

    @Column
    private String foodName;

    @OneToOne(cascade = CascadeType.ALL)  // cascade 속성 설정
    private Nutrient nutrient;

    public Food() {

    }

    public void addNutrient(Nutrient nutrient){
        this.nutrient = nutrient;
    }
}

