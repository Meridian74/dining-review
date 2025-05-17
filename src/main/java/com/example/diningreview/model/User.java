package com.example.diningreview.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(name="username", unique = true)
    private String username;

    @NotBlank
    @Column(name="city")
    private String city;

    @NotBlank
    @Column(name="state")
    private String state;

    @NotBlank
    @Column(name="zip_code")
    private String zipCode;

    @Column(name="peanut_allergy")
    private Boolean interestedPeanutAllergy;

    @Column(name="egg_allergy")
    private Boolean interestedEggAllergy;

    @Column(name="dairy_allergy")
    private Boolean interestedDairyAllergy;

}