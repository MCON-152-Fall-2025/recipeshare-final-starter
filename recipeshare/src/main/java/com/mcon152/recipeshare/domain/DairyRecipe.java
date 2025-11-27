package com.mcon152.recipeshare.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.mcon152.recipeshare.web.RecipeRequest;

@Entity
@DiscriminatorValue("DAIRY")
public class DairyRecipe extends Recipe {
    public DairyRecipe() { super(); }

    public DairyRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }

    static {
        RecipeRegistry.register("DAIRY", new DairyRecipe());
    }

    @Override
    protected Recipe createFromRequestInstance(RecipeRequest req) {
        DairyRecipe out = new DairyRecipe();
        populateCommonFields(out, req);
        return out;
    }
}
