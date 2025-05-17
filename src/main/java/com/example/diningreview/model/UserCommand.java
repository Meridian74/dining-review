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
public class UserCommand {

    @NotNull
    @NotBlank
    private String username;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String zipCode;

    private Boolean interestedPeanutAllergy;

    private Boolean interestedEggAllergy;

    private Boolean interestedDairyAllergy;

}