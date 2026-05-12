package com.Croquetas.controller;

import com.Croquetas.model.Contest;
import com.Croquetas.model.Recipe;
import com.Croquetas.model.User;
import com.Croquetas.repository.ContestRepository;
import com.Croquetas.repository.RecipeRepository;
import com.Croquetas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRepository contestRepository;

    @GetMapping
    public String listPublicRecipes(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long chefId,
            @RequestParam(required = false) Long contestId,
            Model model) {

        List<Recipe> recipes;

        // Aplicar filtros
        if (chefId != null) {
            recipes = recipeRepository.findByIsPublicTrueAndChefIdOrderByTitleAsc(chefId);
        } else if (contestId != null) {
            recipes = recipeRepository.findByIsPublicTrueAndContestIdOrderByTitleAsc(contestId);
        } else {
            // Sin filtro, aplicar ordenación
            if ("chef".equals(sort)) {
                recipes = recipeRepository.findByIsPublicTrueOrderByChefFullNameAsc();
            } else if ("contest".equals(sort)) {
                recipes = recipeRepository.findByIsPublicTrueOrderByContestNameAsc();
            } else {
                recipes = recipeRepository.findByIsPublicTrueOrderByTitleAsc();
            }
        }

        // Listas para los desplegables
        List<User> chefs = userRepository.findChefsWithPublicRecipes();
        List<Contest> contests = contestRepository.findContestsWithPublicRecipes();

        model.addAttribute("recipes", recipes);
        model.addAttribute("chefs", chefs);
        model.addAttribute("contests", contests);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("selectedChefId", chefId);
        model.addAttribute("selectedContestId", contestId);

        return "recipes/list";
    }

    @GetMapping("/{id}")
    public String viewRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada: " + id));
        model.addAttribute("recipe", recipe);
        return "recipes/detail";
    }
}