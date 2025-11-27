package com.mcon152.recipeshare.service;

import com.mcon152.recipeshare.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Recipe addRecipe(Recipe recipe);
    List<Recipe> getAllRecipes();
    Optional<Recipe> getRecipeById(long id);
    boolean deleteRecipe(long id);
    Optional<Recipe> updateRecipe(long id, Recipe updatedRecipe);
    Optional<Recipe> patchRecipe(long id, Recipe partialRecipe);
}

