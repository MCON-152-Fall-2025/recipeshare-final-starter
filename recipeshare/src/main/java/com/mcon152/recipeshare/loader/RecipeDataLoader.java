package com.mcon152.recipeshare.loader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcon152.recipeshare.domain.*;
import com.mcon152.recipeshare.repository.AppUserRepository;
import com.mcon152.recipeshare.repository.RecipeRepository;
import com.mcon152.recipeshare.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

/**
 * Loads sample recipes from JSON file into the database on application startup.
 * Runs after UserDataLoader to ensure user references are valid.
 */
@Component
@Order(2)
public class RecipeDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RecipeDataLoader.class);
    private final RecipeRepository recipeRepository;
    private final AppUserRepository userRepository;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    // Cache to store tags during loading to avoid duplicate creation
    private final Map<String, Tag> tagCache = new HashMap<>();

    public RecipeDataLoader(RecipeRepository recipeRepository,
                           AppUserRepository userRepository,
                           TagRepository tagRepository,
                           ObjectMapper objectMapper) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        if (recipeRepository.count() > 0) {
            logger.info("Recipes already exist in database. Skipping data load.");
            return;
        }

        logger.info("Loading sample recipes from JSON...");

        try {
            // Clear cache before loading
            tagCache.clear();

            // Load existing tags into cache first
            tagRepository.findAll().forEach(tag -> tagCache.put(tag.getName(), tag));

            List<Recipe> recipes = loadRecipesFromJson();
            recipeRepository.saveAll(recipes);
            logger.info("Successfully loaded {} recipes", recipes.size());

            recipes.forEach(recipe ->
                logger.debug("Loaded recipe: {} (type: {}, tags: {})",
                    recipe.getTitle(),
                    recipe.getRecipeType(),
                    recipe.getTags().size())
            );
        } catch (Exception e) {
            logger.error("Failed to load recipes from JSON: {}", e.getMessage(), e);
        }
    }

    private List<Recipe> loadRecipesFromJson() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/recipes.json");
        List<Recipe> recipes = new ArrayList<>();

        try (InputStream inputStream = resource.getInputStream()) {
            JsonNode rootNode = objectMapper.readTree(inputStream);

            if (rootNode.isArray()) {
                for (JsonNode recipeNode : rootNode) {
                    Recipe recipe = parseRecipe(recipeNode);
                    if (recipe != null) {
                        recipes.add(recipe);
                    }
                }
            }
        }

        return recipes;
    }

    private Recipe parseRecipe(JsonNode node) {
        try {
            String type = node.has("type") ? node.get("type").asText().toUpperCase() : "BASIC";
            String title = node.get("title").asText();
            String description = node.has("description") ? node.get("description").asText() : "";
            String ingredients = node.get("ingredients").asText();
            String instructions = node.get("instructions").asText();
            Integer servings = node.has("servings") ? node.get("servings").asInt() : 1;

            // Create recipe based on type
            Recipe recipe = createRecipeByType(type, title, description, ingredients, instructions, servings);

            // Set author if provided
            if (node.has("authorUsername")) {
                String authorUsername = node.get("authorUsername").asText();
                userRepository.findByUsername(authorUsername).ifPresent(recipe::setAuthor);
            }

            // Add tags if provided
            if (node.has("tags")) {
                JsonNode tagsNode = node.get("tags");
                if (tagsNode.isArray()) {
                    for (JsonNode tagNode : tagsNode) {
                        String tagName = tagNode.asText();
                        Tag tag = findOrCreateTag(tagName);
                        recipe.addTag(tag);
                    }
                }
            }

            return recipe;

        } catch (Exception e) {
            logger.error("Failed to parse recipe: {}", e.getMessage());
            return null;
        }
    }

    private Recipe createRecipeByType(String type, String title, String description,
                                     String ingredients, String instructions, Integer servings) {
        return switch (type) {
            case "VEGETARIAN" -> new VegetarianRecipe(null, title, description, ingredients, instructions, servings);
            case "DESSERT" -> new DessertRecipe(null, title, description, ingredients, instructions, servings);
            case "DAIRY" -> new DairyRecipe(null, title, description, ingredients, instructions, servings);
            default -> new BasicRecipe(null, title, description, ingredients, instructions, servings);
        };
    }

    private Tag findOrCreateTag(String tagName) {
        // Check cache first
        if (tagCache.containsKey(tagName)) {
            return tagCache.get(tagName);
        }

        // Check database
        Optional<Tag> existingTag = tagRepository.findByName(tagName);
        if (existingTag.isPresent()) {
            Tag tag = existingTag.get();
            tagCache.put(tagName, tag);
            return tag;
        }

        // Create new tag and add to cache
        Tag newTag = new Tag(tagName);
        tagCache.put(tagName, newTag);
        return newTag;
    }
}
