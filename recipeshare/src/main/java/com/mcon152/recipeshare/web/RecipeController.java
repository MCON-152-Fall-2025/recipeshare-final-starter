package com.mcon152.recipeshare.web;

import com.mcon152.recipeshare.Recipe;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final List<Recipe> recipes = new ArrayList<>();

    private final AtomicLong counter = new AtomicLong();

    @PostMapping
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        recipe.setId(counter.incrementAndGet());
        recipes.add(recipe);
        return recipe;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipes;
    }
}
