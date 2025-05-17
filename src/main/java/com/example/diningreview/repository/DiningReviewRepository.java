package com.example.diningreview.repository;

import com.example.diningreview.model.AdminStatus;
import com.example.diningreview.model.DiningReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningReviewRepository extends JpaRepository<DiningReview, Long> {

    // SELECT * FROM dining_reviews WHERE admin_status IS NULL ORDER BY id ASC;
    List<DiningReview> findByAdminStatusIsNullOrderByIdAsc();

    // SELECT * FROM dining_reviews WHERE restaurant_id = ? AND admin_status = ?;
    List<DiningReview> findAllByRestaurantIdAndAdminStatus(Long restaurantId, AdminStatus adminStatus);
}