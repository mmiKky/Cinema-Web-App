package com.cinema.cinemawebapp.user;

import com.cinema.cinemawebapp.user.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select password from User where email = :email")
    String findPasswordByEmail(@Param("email") String email);
}
