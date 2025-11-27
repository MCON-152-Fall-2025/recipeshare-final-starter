package com.mcon152.recipeshare.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.mcon152.recipeshare.web.RecipeRequest;

@Entity
@DiscriminatorValue("BASIC")
public class BasicRecipe extends Recipe {
    public BasicRecipe() { super(); }

    public BasicRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }

    public BasicRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings, AppUser author) {
        super(id, title, description, ingredients, instructions, servings, author);
    }

    // Register prototype so RecipeRegistry.createFromRequest can dispatch to this subclass
    static {
        RecipeRegistry.register("BASIC", new BasicRecipe());
    }

    @Override
    protected Recipe createFromRequestInstance(RecipeRequest req) {
        BasicRecipe out = new BasicRecipe();
        populateCommonFields(out, req);
        return out;
    }
}
