package com.group15.TUKulinarium.repository;

import com.group15.TUKulinarium.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(
            "SELECT r FROM Recipe r WHERE lower(r.name) " +
            "LIKE :#{#name == null || #name.isEmpty()? '%' : '%'+#name+'%'} " +
            "AND lower(r.user.username) LIKE :#{#username == null || #username.isEmpty()? '%' : '%'+#username+'%'} " +
            "AND lower(r.category.name) LIKE :#{#categoryName == null || #categoryName.isEmpty()? '%' : '%'+#categoryName+'%'} " +
            "AND r.cookingTime <= :cookingTime " +
            "AND r.approved = true "
    )
    Page<Recipe> findPagedRecipes(Pageable pageable, String name, int cookingTime, String username, String categoryName);

    @Query(
            "SELECT r FROM Recipe r WHERE r.approved = false "
    )
    Page<Recipe> findPagedPendingApproval(Pageable pageable);
}
