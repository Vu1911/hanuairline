package com.se2.hanuairline.repository;

import com.se2.hanuairline.model.TaxAndMarkup;
import com.se2.hanuairline.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxAndMarkupRepository extends JpaRepository<TaxAndMarkup, Long> {
    List<TaxAndMarkup> findAll();
}
