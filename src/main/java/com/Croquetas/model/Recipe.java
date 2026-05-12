package com.Croquetas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String photoUrl;
    private boolean isPublic = false;   // hidden until contest finished
    private int bestVotes = 0;
    public int getSexierVotes() {
		return sexierVotes;
	}

	public void setSexierVotes(int sexierVotes) {
		this.sexierVotes = sexierVotes;
	}

	public int getWorstVotes() {
		return worstVotes;
	}

	public void setWorstVotes(int worstVotes) {
		this.worstVotes = worstVotes;
	}

	public int getCreativityVotes() {
		return creativityVotes;
	}

	public void setCreativityVotes(int creativityVotes) {
		this.creativityVotes = creativityVotes;
	}

	private int sexierVotes = 0;
    private int worstVotes = 0;
    private int creativityVotes = 0;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private User chef;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public User getChef() {
		return chef;
	}

	public void setChef(User chef) {
		this.chef = chef;
	}

	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public Recipe(String title, String description, String photoUrl, User chef, Contest contest) {
		super();
		this.title = title;
		this.description = description;
		this.photoUrl = photoUrl;
		this.chef = chef;
		this.contest = contest;
	}

	public int getBestVotes() {
		return bestVotes;
	}

	public void setBestVotes(int bestVotes) {
		this.bestVotes = bestVotes;
	}
	public Recipe() {
	}
	

}