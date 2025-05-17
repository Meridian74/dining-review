package com.example.diningreview.controller;


import com.example.diningreview.model.AdminStatus;
import com.example.diningreview.model.DiningReview;
import com.example.diningreview.model.Restaurant;
import com.example.diningreview.model.RestaurantCommand;
import com.example.diningreview.repository.DiningReviewRepository;
import com.example.diningreview.repository.RestaurantRepository;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantController {

    private RestaurantRepository restaurantRepository;
    private DiningReviewRepository diningReviewRepository;


    @GetMapping("/")
    public List<Restaurant> getAllRestaurant() {
        return this.restaurantRepository.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant addNewRestaurant(@Valid @RequestBody RestaurantCommand restaurantCommand) {
        Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findByNameAndZipCode(restaurantCommand.getName(),
                restaurantCommand.getZipCode());
        if (optionalRestaurant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This restaurant with name (" +
                    restaurantCommand.getName() + ") is existed at this zipcode: " + restaurantCommand.getZipCode());
        }

        Restaurant newRestaurant = convertRestaurantCommandToRestaurant(restaurantCommand);
        return this.restaurantRepository.save(newRestaurant);
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(id);
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid restaurant id: " + id);
        }

        return optionalRestaurant.get();
    }

    @GetMapping("/rated-restaurant/all")
    public List<Restaurant> getAllRatedRestaurantsOrderByAverageScore() {
        return this.restaurantRepository.findAllByAverageScoreGreaterThanOrderByAverageScoreDesc(0.0);
    }

    @GetMapping("/rated-restaurant")
    public List<Restaurant> getAllRatedRestaurantsByZipCodeOrderByAverageScore(@PathParam("zipcode") String zipCode) {
        return this.restaurantRepository.findAllByZipCodeAndAverageScoreGreaterThanOrderByAverageScoreDesc(zipCode, 0.0);
    }


    public void maintainRestaurantScore(DiningReview diningReview, Long userId) {
        Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(diningReview.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect restaurant id: " +
                    diningReview.getRestaurantId());
        }

        Restaurant restaurant = updateRestaurantScores(diningReview, userId, optionalRestaurant.get());
        this.restaurantRepository.save(restaurant);
    }

    private Restaurant convertRestaurantCommandToRestaurant(RestaurantCommand command) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(command.getName());
        restaurant.setZipCode(command.getZipCode());
        restaurant.setAddress(command.getAddress());
        restaurant.setPeanutAllergyScore(new HashMap<>());
        restaurant.setEggAllergyScore(new HashMap<>());
        restaurant.setDairyAllergyScore(new HashMap<>());
        restaurant.setAverageScore(0.0);
        return restaurant;
    }

    private static Restaurant updateRestaurantScores(DiningReview diningReview, Long userId, Restaurant restaurant) {
        if (diningReview.getAdminStatus() == AdminStatus.APPROVED) {
            if (diningReview.getPeanutScore() != null) {
                restaurant.addPeanutAlleryScore(userId, diningReview.getPeanutScore());
            }
            if (diningReview.getEggScore() != null) {
                restaurant.addEggAlleryScore(userId, diningReview.getEggScore());
            }
            if (diningReview.getDairyScore() != null) {
                restaurant.addDairyAlleryScore(userId, diningReview.getDairyScore());
            }
        } else {
            if (restaurant.getPeanutAllergyScore() != null) {
                restaurant.removePeanutAllergyScore(userId);
            }
            if (restaurant.getEggAllergyScore() != null) {
                restaurant.removeEggAllergyScore(userId);
            }
            if (restaurant.getDairyAllergyScore() != null) {
                restaurant.removeDairyAllergyScore(userId);
            }
        }

        return restaurant;
    }

}
