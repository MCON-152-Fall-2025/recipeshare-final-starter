package com.mcon152.recipeshare.domain;

import com.mcon152.recipeshare.web.RecipeRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of recipe prototypes used by the factory-method. Kept separate from Recipe
 * to preserve the Open/Closed principle (subclasses can register themselves without
 * modifying the base class).
 */
public final class RecipeRegistry {
    private RecipeRegistry() {}

    private static final Map<String, Recipe> RECIPE_CLASS_MAP = new HashMap<>();

    public static void register(String type, Recipe subclass) {
        if (type == null || subclass == null) return;
        RECIPE_CLASS_MAP.put(type.trim().toUpperCase(), subclass);
    }


    public static Recipe createFromRequest(RecipeRequest req) {
        String type = req != null && req.getType() != null ? req.getType().trim().toUpperCase() : "BASIC";
        Recipe subclass = RECIPE_CLASS_MAP.get(type);
        if (subclass == null) {
            subclass = RECIPE_CLASS_MAP.getOrDefault("BASIC", new BasicRecipe());
        }
        // delegate to prototype to create a fresh instance
        return subclass.createFromRequestInstance(req);
    }
}

