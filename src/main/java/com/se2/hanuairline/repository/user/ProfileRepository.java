package com.se2.hanuairline.repository.user;

import com.se2.hanuairline.model.PriceByClass;
import com.se2.hanuairline.model.user.Profile;
import com.se2.hanuairline.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, CrudRepository<Profile,Long> {

    List<Profile> findAll();
   Optional<Profile> findById(Long id);
   Optional<Profile> findByUser_Id(Long userId);
   void deleteByUser_Id(Long id);
}
