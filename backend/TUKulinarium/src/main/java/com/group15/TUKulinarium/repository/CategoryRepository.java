package com.group15.TUKulinarium.repository;

import com.group15.TUKulinarium.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    @Query(
            "SELECT c FROM Category c WHERE lower(c.name) " +
            "LIKE :#{#name == null || #name.isEmpty()? '%' : '%'+#name+'%'}"
    )
    Page<Category> findPagedCategories(Pageable pageable, String name);
}
