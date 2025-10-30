package com.mcon152.recipeshare;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VEGETARIAN")
public class VegetarianRecipe extends Recipe {
    public VegetarianRecipe() { super(); }

    public VegetarianRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }
}


