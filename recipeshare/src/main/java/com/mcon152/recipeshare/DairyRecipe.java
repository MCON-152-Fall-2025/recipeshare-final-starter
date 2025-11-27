package com.mcon152.recipeshare;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DAIRY")
public class DairyRecipe extends Recipe {
    public DairyRecipe() { super(); }

    public DairyRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }
}


