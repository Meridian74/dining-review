package com.example.diningreview.model;

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
public class RestaurantCommand {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String zipCode;

    @NotNull
    @NotBlank
    private String address;

}