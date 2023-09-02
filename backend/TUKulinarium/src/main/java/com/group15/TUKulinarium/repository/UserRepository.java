package com.group15.TUKulinarium.repository;

import com.group15.TUKulinarium.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(
            "SELECT u FROM User u WHERE lower(u.username) " +
                    "LIKE :#{#username == null || #username.isEmpty()? '%' : '%'+#username+'%'} " +
                    "AND lower(u.name) LIKE :#{#name == null || #name.isEmpty()? '%' : '%'+#name+'%'} " +
                    "AND lower(u.email) LIKE :#{#email == null || #email.isEmpty()? '%' : '%'+#email+'%'} "
    )
    Page<User> findPagedUsers(Pageable pageable, String username, String name, String email);
}
