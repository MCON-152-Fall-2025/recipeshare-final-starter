package com.mcon152.recipeshare.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.mcon152.recipeshare.web.RecipeRequest;

@Entity
@DiscriminatorValue("VEGETARIAN")
public class VegetarianRecipe extends Recipe {
    public VegetarianRecipe() { super(); }

    public VegetarianRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }

    static {
        RecipeRegistry.register("VEGETARIAN", new VegetarianRecipe());
    }

    @Override
    protected Recipe createFromRequestInstance(RecipeRequest req) {
        VegetarianRecipe out = new VegetarianRecipe();
        populateCommonFields(out, req);
        return out;
    }
}
