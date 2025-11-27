package com.mcon152.recipeshare.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.mcon152.recipeshare.web.RecipeRequest;

@Entity
@DiscriminatorValue("DESSERT")
public class DessertRecipe extends Recipe {
    public DessertRecipe() { super(); }

    public DessertRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }

    static {
        RecipeRegistry.register("DESSERT", new DessertRecipe());
    }

    @Override
    protected Recipe createFromRequestInstance(RecipeRequest req) {
        DessertRecipe out = new DessertRecipe();
        populateCommonFields(out, req);
        return out;
    }
}
