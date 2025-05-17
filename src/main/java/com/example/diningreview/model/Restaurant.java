package com.example.diningreview.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name="RESTAURANTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @NotBlank
    @Column(name="zip_code", nullable = false)
    private String zipCode;

    @NotNull
    @NotBlank
    @Column(name="address", nullable = false)
    private String address;

    // every score stored with user_id (key) and user's peanut allergy score (value)
    @ElementCollection
    @CollectionTable(name = "restaurant_peanut_scores", joinColumns = @JoinColumn(name = "restaurant_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "score")
    private Map<Long, Integer> peanutAllergyScore = new HashMap<>();

    // every score stored with user_id (key) and user's egg allergy score (value)
    @ElementCollection
    @CollectionTable(name = "restaurant_egg_scores", joinColumns = @JoinColumn(name = "restaurant_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "score")
    private Map<Long, Integer> eggAllergyScore = new HashMap<>();

    // every score stored with user_id (key) and user's dairy allergy score (value)
    @ElementCollection
    @CollectionTable(name = "restaurant_dairy_scores", joinColumns = @JoinColumn(name = "restaurant_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "score")
    private Map<Long, Integer> dairyAllergyScore = new HashMap<>();


    @Column(name="average_score")
    private Double averageScore;


    public void addPeanutAlleryScore(Long userId, Integer peanutScore) {
        peanutAllergyScore.put(userId, peanutScore);
        maintainAverageScore();
    }

    public void addEggAlleryScore(Long userId, Integer eggScore) {
        eggAllergyScore.put(userId, eggScore);
        maintainAverageScore();
    }

    public void addDairyAlleryScore(Long userId, Integer dairyScore) {
        dairyAllergyScore.put(userId, dairyScore);
        maintainAverageScore();
    }

    public void removePeanutAllergyScore(long userId) {
        peanutAllergyScore.remove(userId);
        maintainAverageScore();
    }

    public void removeEggAllergyScore(long userId) {
        eggAllergyScore.remove(userId);
        maintainAverageScore();
    }

    public void removeDairyAllergyScore(long userId) {
        dairyAllergyScore.remove(userId);
        maintainAverageScore();
    }

    public void maintainAverageScore() {
        long sum = 0L;
        for (Map.Entry<Long, Integer> entry : peanutAllergyScore.entrySet()) {
            sum += entry.getValue();
        }
        for (Map.Entry<Long, Integer> entry : eggAllergyScore.entrySet()) {
            sum += entry.getValue();
        }
        for (Map.Entry<Long, Integer> entry : dairyAllergyScore.entrySet()) {
            sum += entry.getValue();
        }
        long allSize = (long) peanutAllergyScore.entrySet().size() + eggAllergyScore.entrySet().size() +
                dairyAllergyScore.entrySet().size();

        if (allSize > 0) {
            this.setAverageScore((double) sum / allSize);
        }
    }

}