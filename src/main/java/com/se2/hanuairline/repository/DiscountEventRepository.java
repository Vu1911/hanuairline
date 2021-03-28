package com.se2.hanuairline.repository;

import com.se2.hanuairline.model.DiscountEvent;
import com.se2.hanuairline.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountEventRepository extends JpaRepository<DiscountEvent, Long> {

}
