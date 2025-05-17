package com.example.diningreview.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReviewAction {

    @NotNull
    private AdminStatus adminStatus;

}