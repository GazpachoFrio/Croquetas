package com.Croquetas.controller;

import com.Croquetas.model.Contest;
import com.Croquetas.model.ContestRegistration;
import com.Croquetas.model.Recipe;
import com.Croquetas.model.User;
import com.Croquetas.repository.ContestRegistrationRepository;
import com.Croquetas.repository.ContestRepository;
import com.Croquetas.repository.RecipeRepository;
import com.Croquetas.repository.UserRepository;
import com.Croquetas.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chef")
public class ChefController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ContestRegistrationRepository registrationRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/{username}")
    public String publicProfile(@PathVariable String username,
                                Authentication auth,
                                Model model) {
        User chef = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Chef not found: " + username));
        List<Recipe> recipes = recipeRepository.findByChef(chef);
        List<ContestRegistration> participations = registrationRepository.findByChef(chef);

        model.addAttribute("chef", chef);
        model.addAttribute("recipes", recipes);
        model.addAttribute("participations", participations);

        // Check if the logged-in user is viewing their own profile
        boolean isOwner = false;
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            User loggedUser = (User) auth.getPrincipal();
            isOwner = loggedUser.getUsername().equals(username);
        }
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            User loggedUser = (User) auth.getPrincipal();
            isAdmin = loggedUser.getRole().equals("ROLE_ADMIN");
        }
        model.addAttribute("isAdmin", isAdmin);

        if (isOwner) {
            // Active contests (not finished) where chef hasn't uploaded yet
            List<Contest> activeContests = contestRepository.findByFinishedFalse();
            List<Contest> availableContests = activeContests.stream()
                    .filter(c -> participations.stream().noneMatch(p -> p.getContest().equals(c)))
                    .toList();
            model.addAttribute("availableContests", availableContests);

            // Past contests (finished) where chef hasn't uploaded yet
            List<Contest> finishedContests = contestRepository.findByFinishedTrue();
            List<Contest> pastContests = finishedContests.stream()
                    .filter(c -> participations.stream().noneMatch(p -> p.getContest().equals(c)))
                    .toList();
            model.addAttribute("pastContests", pastContests);
        }

        model.addAttribute("isOwner", isOwner);
        return "chef/profile";
    }
    

    @GetMapping("/{username}/contest/{contestId}/upload")
    public String showUploadForm(@PathVariable String username,
                                 @PathVariable Long contestId,
                                 Authentication auth,
                                 Model model) {
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return "redirect:/login";
        }
        User loggedUser = (User) auth.getPrincipal();
        if (!loggedUser.getUsername().equals(username)) {
            return "redirect:/chef/" + username;
        }

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contest ID: " + contestId));
        model.addAttribute("contest", contest);
        model.addAttribute("username", username);
        return "chef/upload-recipe";
    }

    @PostMapping("/{username}/contest/{contestId}/upload")
    public String uploadRecipe(@PathVariable String username,
                               @PathVariable Long contestId,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam("photo") MultipartFile photo,
                               Authentication auth) throws IOException {
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return "redirect:/login";
        }
        User loggedUser = (User) auth.getPrincipal();
        if (!loggedUser.getUsername().equals(username)) {
            return "redirect:/chef/" + username;
        }

        User chef = loggedUser;
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contest ID: " + contestId));

        // Prevent duplicate submission
        if (registrationRepository.findByContestAndChef(contest, chef).isPresent()) {
            return "redirect:/chef/" + username + "?error=alreadyRegistered";
        }

        // Save photo and create recipe
        String photoUrl = fileUploadService.saveRecipePhoto(photo);
        Recipe recipe = new Recipe(title, description, photoUrl, chef, contest);
        
        // For finished contests, recipe becomes public immediately; for active contests, hidden
        boolean isFinished = contest.isFinished();
        recipe.setPublic(isFinished);
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Assign contestant number: 0 for past contests, sequential for active contests
        int assignedNumber;
        if (isFinished) {
            assignedNumber = 0;   // indicates not competing, just historical record
        } else {
            List<ContestRegistration> existing = registrationRepository.findByContestOrderByAssignedNumberAsc(contest);
            assignedNumber = existing.stream()
                    .mapToInt(ContestRegistration::getAssignedNumber)
                    .max()
                    .orElse(0) + 1;
        }

        ContestRegistration registration = new ContestRegistration(contest, chef, assignedNumber, savedRecipe);
        registrationRepository.save(registration);

        return "redirect:/chef/" + username + "?uploadSuccess";
    }
}