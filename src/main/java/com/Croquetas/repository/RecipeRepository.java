package com.Croquetas.repository;

import com.Croquetas.model.Recipe;
import com.Croquetas.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	List<Recipe> findByChef(User chef);
	List<Recipe> findByIsPublicTrueOrderByTitleAsc();

	List<Recipe> findByIsPublicTrueOrderByChefFullNameAsc();
	List<Recipe> findByIsPublicTrueOrderByContestNameAsc();   // assuming Contest has name
	List<Recipe> findByIsPublicTrueAndChefIdOrderByTitleAsc(Long chefId);
	List<Recipe> findByIsPublicTrueAndContestIdOrderByTitleAsc(Long contestId);

}