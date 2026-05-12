package com.Croquetas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contest_registrations")
public class ContestRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "chef_id", nullable = false)
    private User chef;

    private int assignedNumber;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public User getChef() {
		return chef;
	}

	public void setChef(User chef) {
		this.chef = chef;
	}

	public int getAssignedNumber() {
		return assignedNumber;
	}

	public void setAssignedNumber(int assignedNumber) {
		this.assignedNumber = assignedNumber;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public ContestRegistration(Contest contest, User chef, int assignedNumber, Recipe recipe) {
		super();
		this.contest = contest;
		this.chef = chef;
		this.assignedNumber = assignedNumber;
		this.recipe = recipe;
	}

	public ContestRegistration() {
	}
}
