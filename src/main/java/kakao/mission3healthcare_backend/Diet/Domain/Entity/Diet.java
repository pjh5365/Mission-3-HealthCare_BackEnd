package kakao.mission3healthcare_backend.Diet.Domain.Entity;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Diet{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long diet_id;

    @Column
    private long member_id;

//    @Column
//    private type meal_Type;

    @OneToMany(cascade = CascadeType.ALL)
    private ArrayList<Food> foods;
}
