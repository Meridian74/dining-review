package com.example.diningreview.repository;

import com.example.diningreview.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByNameAndZipCode(String name, String zipCode);

    @Query("""
        SELECT r FROM Restaurant r
        WHERE
            SIZE(r.peanutAllergyScore) > 0 OR
            SIZE(r.eggAllergyScore) > 0 OR
            SIZE(r.dairyAllergyScore) > 0
        ORDER BY r.averageScore DESC
    """)
    List<Restaurant> findAllWithAnyAllergyScoreOrderByAverageScoreDesc();

    // more simplifier query :))
    List<Restaurant> findAllByAverageScoreGreaterThanOrderByAverageScoreDesc(Double minValue);

    // with ZipCode
    List<Restaurant> findAllByZipCodeAndAverageScoreGreaterThanOrderByAverageScoreDesc(String zipCode, Double minValue);

}