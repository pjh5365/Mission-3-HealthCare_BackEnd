package kakao.mission3healthcare_backend.Diet.Domain.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Nutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long nutrient_id;

    @Column
    private long food_id;

    @Column
    private long amount;
}
