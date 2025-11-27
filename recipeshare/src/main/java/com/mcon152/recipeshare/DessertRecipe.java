package com.mcon152.recipeshare;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DESSERT")
public class DessertRecipe extends Recipe {
    public DessertRecipe() { super(); }

    public DessertRecipe(Long id, String title, String description, String ingredients, String instructions, Integer servings) {
        super(id, title, description, ingredients, instructions, servings);
    }
}


