package com.mcon152.recipeshare.domain;

import jakarta.persistence.*;

import com.mcon152.recipeshare.web.RecipeRequest;

@Entity
@Table(name = "recipes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "recipe_type", discriminatorType = DiscriminatorType.STRING, columnDefinition = "VARCHAR(31) DEFAULT 'BASIC'")
public abstract class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(length = 2000)
    private String ingredients;

    @Column(length = 4000)
    private String instructions;

    private Integer servings; // New field for number of servings

    // Map discriminator column as a read-only field so DDL/copy operations include it; make it nullable and
    // give a default so schema updates that INSERT without the column won't violate NOT NULL.
    @Column(name = "recipe_type", insertable = false, updatable = false, nullable = true,
            columnDefinition = "VARCHAR(31) DEFAULT 'BASIC'")
    private String recipeType;

    /**
     * Instance factory method that concrete subclasses must implement to produce instances of their type.
     */
    protected abstract Recipe createFromRequestInstance(RecipeRequest req);

    /**
     * Copies the common fields from the request to the target recipe instance. Subclasses can override
     * this if they need to customize how common fields are populated.
     */
    protected void populateCommonFields(Recipe target, RecipeRequest req) {
        if (target == null) return;
        target.setId(null);
        if (req != null) {
            target.setTitle(req.getTitle());
            target.setDescription(req.getDescription());
            target.setIngredients(req.getIngredients());
            target.setInstructions(req.getInstructions());
            target.setServings(req.getServings());
        }
    }

    // Constructors
    public Recipe() {}

    public Recipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.servings = servings;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }

    // Read-only access to discriminator value
    public String getRecipeType() { return recipeType; }
}
