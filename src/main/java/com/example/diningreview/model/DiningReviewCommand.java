package com.example.diningreview.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiningReviewCommand {

    @NotNull
    @NotBlank
    private String senderName;

    @NotNull
    private Long restaurantId;

    @Min(1)
    @Max(5)
    private Integer peanutScore;

    @Min(1)
    @Max(5)
    private Integer eggScore;

    @Min(1)
    @Max(5)
    private Integer dairyScore;

    @NotBlank
    private String commentary;

}