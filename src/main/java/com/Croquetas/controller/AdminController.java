package com.Croquetas.controller;

import com.Croquetas.model.Contest;
import com.Croquetas.model.ContestRegistration;
import com.Croquetas.model.Recipe;
import com.Croquetas.model.User;
import com.Croquetas.repository.ContestRegistrationRepository;
import com.Croquetas.repository.ContestRepository;
import com.Croquetas.repository.RecipeRepository;
import com.Croquetas.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserRepository userRepository;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ContestRegistrationRepository registrationRepository;

    @Autowired
    private RecipeRepository recipeRepository;
    // Admin dashboard: show create form and list of contests
    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("contest", new Contest());
        model.addAttribute("contests", contestRepository.findAll());
        model.addAttribute("pendingUsers", userRepository.findByApprovedFalse());
        model.addAttribute("pendingUsers", userRepository.findPendingUsers());
        return "/admin";
    }
    @PostMapping("/approve-user/{id}")
    public String approveUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setApproved(true);
        userRepository.save(user);
        return "redirect:/admin";
    }
    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
    // Create new contest
    @PostMapping("/contests/create")
    public String createContest(@RequestParam String name, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        Contest contest = new Contest(name, startDate, endDate);
        contest.setFinished(false);
        contestRepository.save(contest);
        return "redirect:/admin";
    }

    // Mark contest as finished
    @PostMapping("/contests/{id}/finish")
    public String finishContest(@PathVariable Long id) {
        Contest contest = contestRepository.findById(id).orElseThrow();
        contest.setFinished(true);
        contestRepository.save(contest);
        List<ContestRegistration> registrations = registrationRepository.findByContest(contest);
        for (ContestRegistration reg : registrations) {
            Recipe recipe = reg.getRecipe();
            recipe.setPublic(true);
            recipeRepository.save(recipe);
        }
        // TODO: later we will make all recipes of this contest public
        return "redirect:/admin";
    }
   
}