package com.example.diningreview.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="DINING_REVIEWS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiningReview {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @NotNull
    @Column(name="restaurant_id", nullable = false)
    private Long restaurantId;

    @Min(1)
    @Max(5)
    @Column(name="peanut_score")
    private Integer peanutScore;

    @Min(1)
    @Max(5)
    @Column(name="egg_score")
    private Integer eggScore;

    @Min(1)
    @Max(5)
    @Column(name="dairy_score")
    private Integer dairyScore;

    @Column(name="commentary")
    private String commentary;

    @Column(name="admin_status")
    private AdminStatus adminStatus;

}