package com.mcon152.recipeshare;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BASIC")
public class BasicRecipe extends Recipe {
    public BasicRecipe() { super(); }

    public BasicRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }
}


