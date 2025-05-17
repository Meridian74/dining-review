package com.example.diningreview.controller;

import com.example.diningreview.model.*;
import com.example.diningreview.repository.DiningReviewRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/dining-reviews")
@AllArgsConstructor
public class DiningReviewController {

    private DiningReviewRepository diningReviewRepository;
    private UserController userController;
    private RestaurantController restaurantController;


    @GetMapping("/")
    public List<DiningReview> getAllDiningReview() {
        return this.diningReviewRepository.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DiningReview createNewReview(@NotNull @Valid @RequestBody DiningReviewCommand diningReviewCommand) {
        if (!isSenderNameValid(diningReviewCommand.getSenderName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given sender name isn't a valid username: " +
                    diningReviewCommand.getSenderName());
        }

        DiningReview newDiningReview = convertReviewCommandToDiningReview(diningReviewCommand);
        return this.diningReviewRepository.save(newDiningReview);
    }

    @GetMapping("/admin/pending-reviews")
    public List<DiningReview> getAllPendingReviewsForAdminUser() {
        List<DiningReview> reviews = this.diningReviewRepository.findByAdminStatusIsNullOrderByIdAsc();
        if (reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "There are currently no reviews awaiting administration.");
        }

        return reviews;
    }

    @PostMapping("/admin/pending-reviews/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DiningReview updatePendingReview(@PathVariable("id") Long id,
                                            @Valid @RequestBody AdminReviewAction adminAction) {

        Optional<DiningReview> optionalDiningReview = this.diningReviewRepository.findById(id);
        if (optionalDiningReview.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect dining-review id: " + id);
        }

        DiningReview diningReview = optionalDiningReview.get();
        Optional<User> optionalUser = this.userController.tryTofindUserByUserName(diningReview.getSenderName());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect username as the sender in the review: " +
                    diningReview.getSenderName());
        }
        Long userId = optionalUser.get().getId();

        diningReview.setAdminStatus(adminAction.getAdminStatus());
        this.restaurantController.maintainRestaurantScore(diningReview, userId);

        return this.diningReviewRepository.save(diningReview);
    }

    // for background processes
    public List<DiningReview> getAllApprovedDiningReviewByRestaurantId(Long restaurantId) {
        return this.diningReviewRepository.findAllByRestaurantIdAndAdminStatus(restaurantId, AdminStatus.APPROVED);
    }


    private boolean isSenderNameValid(String senderName) {
        Optional<User> optionalUser = this.userController.tryTofindUserByUserName(senderName);
        return optionalUser.isPresent();
    }

    private DiningReview convertReviewCommandToDiningReview(DiningReviewCommand command) {
        DiningReview diningReview = new DiningReview();

        diningReview.setSenderName(command.getSenderName());
        diningReview.setRestaurantId(command.getRestaurantId());
        diningReview.setPeanutScore(command.getPeanutScore());
        diningReview.setEggScore(command.getEggScore());
        diningReview.setDairyScore(command.getDairyScore());
        diningReview.setCommentary(command.getCommentary());

        diningReview.setAdminStatus(null);

        return diningReview;
    }

}