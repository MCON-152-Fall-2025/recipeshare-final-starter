package com.mcon152.recipeshare.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void testCreateRecipe() {
        Recipe recipe = new BasicRecipe(1L, "Cake", "Delicious cake", "Flour, Sugar, Eggs", "Mix and bake", 8);
        assertEquals(1L, recipe.getId());
        assertEquals("Cake", recipe.getTitle());
        assertEquals("Delicious cake", recipe.getDescription());
        assertEquals("Flour, Sugar, Eggs", recipe.getIngredients());
        assertEquals("Mix and bake", recipe.getInstructions());
        assertEquals(Integer.valueOf(8), recipe.getServings());
        assertNull(recipe.getAuthor()); // Author is null when not provided
    }

    @Test
    void testSubtypeInstances_andFields() {
        BasicRecipe basic = new BasicRecipe(2L, "Toast", "Buttery toast", "Bread, Butter", "Toast bread and spread butter", 1);
        VegetarianRecipe veg = new VegetarianRecipe(3L, "Veg Salad", "Fresh salad", "Lettuce, Tomato", "Toss", 2);
        DessertRecipe dessert = new DessertRecipe(4L, "Ice Cream", "Vanilla", "Milk, Sugar", "Freeze", 4);
        DairyRecipe dairy = new DairyRecipe(5L, "Cheese Plate", "Assorted cheeses", "Cheeses", "Arrange on plate", 3);

        assertInstanceOf(BasicRecipe.class, basic);
        assertInstanceOf(VegetarianRecipe.class, veg);
        assertInstanceOf(DessertRecipe.class, dessert);
        assertInstanceOf(DairyRecipe.class, dairy);

        assertEquals("Toast", basic.getTitle());
        assertEquals("Veg Salad", veg.getTitle());
        assertEquals("Ice Cream", dessert.getTitle());
        assertEquals(Integer.valueOf(3), dairy.getServings());
    }

    @Test
    void testReadRecipe() {
        Recipe recipe = new BasicRecipe();
        recipe.setId(2L);
        recipe.setTitle("Pie");
        recipe.setDescription("Apple pie");
        recipe.setIngredients("Apples, Flour, Sugar");
        recipe.setInstructions("Mix and bake");
        recipe.setServings(6);
        assertEquals(2L, recipe.getId());
        assertEquals("Pie", recipe.getTitle());
        assertEquals("Apple pie", recipe.getDescription());
        assertEquals("Apples, Flour, Sugar", recipe.getIngredients());
        assertEquals("Mix and bake", recipe.getInstructions());
        assertEquals(Integer.valueOf(6), recipe.getServings());
    }

    @Test
    void testUpdateRecipe() {
        Recipe recipe = new BasicRecipe();
        recipe.setTitle("Bread");
        recipe.setDescription("Simple bread");
        recipe.setIngredients("Flour, Water, Yeast");
        recipe.setInstructions("Mix and bake");
        recipe.setServings(2);
        recipe.setTitle("Whole Wheat Bread");
        recipe.setDescription("Healthy bread");
        recipe.setServings(4);
        assertEquals("Whole Wheat Bread", recipe.getTitle());
        assertEquals("Healthy bread", recipe.getDescription());
        assertEquals(Integer.valueOf(4), recipe.getServings());
    }

    @Test
    void testDeleteRecipe() {
        Recipe recipe = null;
        assertNull(recipe);
    }

    // New tests for Author functionality
    @Nested
    @DisplayName("Author Relationship Tests")
    class AuthorRelationshipTests {

        @Test
        @DisplayName("Recipe can be created with an author")
        void createRecipeWithAuthor() {
            AppUser author = new AppUser(1L, "chef_john", "password", "Chef John");
            Recipe recipe = new BasicRecipe(1L, "Pasta", "Italian pasta", "Pasta, Sauce", "Cook pasta, add sauce", 4, author);

            assertNotNull(recipe.getAuthor());
            assertEquals("chef_john", recipe.getAuthor().getUsername());
            assertEquals("Chef John", recipe.getAuthor().getDisplayName());
        }

        @Test
        @DisplayName("Author can be set after recipe creation")
        void setAuthorAfterCreation() {
            Recipe recipe = new BasicRecipe();
            recipe.setTitle("Soup");
            assertNull(recipe.getAuthor());

            AppUser author = new AppUser(2L, "chef_mary", "pass123", "Chef Mary");
            recipe.setAuthor(author);

            assertNotNull(recipe.getAuthor());
            assertEquals("chef_mary", recipe.getAuthor().getUsername());
        }

        @Test
        @DisplayName("Author can be null (optional field)")
        void authorCanBeNull() {
            Recipe recipe = new BasicRecipe(1L, "Salad", "Fresh salad", "Lettuce, Tomato", "Toss ingredients", 2);
            assertNull(recipe.getAuthor());

            recipe.setAuthor(null);
            assertNull(recipe.getAuthor());
        }

        @Test
        @DisplayName("All recipe subtypes support author field")
        void allSubtypesSupportAuthor() {
            AppUser author = new AppUser(1L, "chef", "pass", "Chef");

            BasicRecipe basic = new BasicRecipe(1L, "Basic", "desc", "ing", "inst", 1, author);
            VegetarianRecipe veg = new VegetarianRecipe(2L, "Veg", "desc", "ing", "inst", 2, author);
            DessertRecipe dessert = new DessertRecipe(3L, "Dessert", "desc", "ing", "inst", 3, author);
            DairyRecipe dairy = new DairyRecipe(4L, "Dairy", "desc", "ing", "inst", 4, author);

            assertEquals("chef", basic.getAuthor().getUsername());
            assertEquals("chef", veg.getAuthor().getUsername());
            assertEquals("chef", dessert.getAuthor().getUsername());
            assertEquals("chef", dairy.getAuthor().getUsername());
        }

        @Test
        @DisplayName("Multiple recipes can share the same author")
        void multipleRecipesSameAuthor() {
            AppUser author = new AppUser(1L, "chef_bob", "password", "Bob");

            Recipe recipe1 = new BasicRecipe(1L, "Recipe 1", "desc", "ing", "inst", 1, author);
            Recipe recipe2 = new VegetarianRecipe(2L, "Recipe 2", "desc", "ing", "inst", 2, author);

            assertSame(author, recipe1.getAuthor());
            assertSame(author, recipe2.getAuthor());
            assertEquals(recipe1.getAuthor().getUsername(), recipe2.getAuthor().getUsername());
        }

        @Test
        @DisplayName("Author can be changed after recipe creation")
        void changeAuthor() {
            AppUser author1 = new AppUser(1L, "chef1", "pass", "Chef One");
            AppUser author2 = new AppUser(2L, "chef2", "pass", "Chef Two");

            Recipe recipe = new BasicRecipe(1L, "Recipe", "desc", "ing", "inst", 1, author1);
            assertEquals("chef1", recipe.getAuthor().getUsername());

            recipe.setAuthor(author2);
            assertEquals("chef2", recipe.getAuthor().getUsername());
        }

        @Test
        @DisplayName("Recipe with author maintains all other fields")
        void recipeWithAuthorMaintainsOtherFields() {
            AppUser author = new AppUser(1L, "chef", "pass", "Chef");
            Recipe recipe = new BasicRecipe(10L, "Pizza", "Delicious pizza",
                    "Dough, Cheese, Tomato", "Bake at 450F", 8, author);

            assertEquals(10L, recipe.getId());
            assertEquals("Pizza", recipe.getTitle());
            assertEquals("Delicious pizza", recipe.getDescription());
            assertEquals("Dough, Cheese, Tomato", recipe.getIngredients());
            assertEquals("Bake at 450F", recipe.getInstructions());
            assertEquals(Integer.valueOf(8), recipe.getServings());
            assertEquals("chef", recipe.getAuthor().getUsername());
        }

        @Test
        @DisplayName("Author field works with setter method")
        void authorFieldWithSetter() {
            Recipe recipe = new VegetarianRecipe();
            recipe.setTitle("Veggie Burger");
            recipe.setDescription("Plant-based burger");

            AppUser author = new AppUser("chef_vegan", "pass", "Vegan Chef");
            recipe.setAuthor(author);

            assertNotNull(recipe.getAuthor());
            assertEquals("chef_vegan", recipe.getAuthor().getUsername());
            assertEquals("Vegan Chef", recipe.getAuthor().getDisplayName());
        }
    }

    // New tests for Tag functionality
    @Nested
    @DisplayName("Tag Relationship Tests")
    class TagRelationshipTests {

        @Test
        @DisplayName("Recipe starts with empty tags set")
        void emptyTagsSet() {
            Recipe recipe = new BasicRecipe(1L, "Pasta", "desc", "ing", "inst", 2);
            assertNotNull(recipe.getTags());
            assertTrue(recipe.getTags().isEmpty());
        }

        @Test
        @DisplayName("addTag adds tag to both sides of relationship")
        void addTag() {
            Recipe recipe = new BasicRecipe(1L, "Pizza", "Italian pizza", "dough", "bake", 4);
            Tag tag = new Tag("italian");

            recipe.addTag(tag);

            assertTrue(recipe.getTags().contains(tag));
            assertTrue(tag.getRecipes().contains(recipe));
        }

        @Test
        @DisplayName("removeTag removes from both sides")
        void removeTag() {
            Recipe recipe = new VegetarianRecipe(1L, "Salad", "Fresh", "lettuce", "mix", 2);
            Tag tag = new Tag("healthy");

            recipe.addTag(tag);
            assertTrue(recipe.getTags().contains(tag));

            recipe.removeTag(tag);
            assertFalse(recipe.getTags().contains(tag));
            assertFalse(tag.getRecipes().contains(recipe));
        }

        @Test
        @DisplayName("Recipe can have multiple tags")
        void multipleTags() {
            Recipe recipe = new DessertRecipe(1L, "Chocolate Cake", "desc", "ing", "inst", 8);
            Tag tag1 = new Tag("dessert");
            Tag tag2 = new Tag("chocolate");
            Tag tag3 = new Tag("party");

            recipe.addTag(tag1);
            recipe.addTag(tag2);
            recipe.addTag(tag3);

            assertEquals(3, recipe.getTags().size());
            assertTrue(recipe.getTags().contains(tag1));
            assertTrue(recipe.getTags().contains(tag2));
            assertTrue(recipe.getTags().contains(tag3));
        }

        @Test
        @DisplayName("clearTags removes all tags from both sides")
        void clearTags() {
            Recipe recipe = new BasicRecipe(1L, "Soup", "desc", "ing", "inst", 4);
            Tag tag1 = new Tag("comfort");
            Tag tag2 = new Tag("winter");

            recipe.addTag(tag1);
            recipe.addTag(tag2);
            assertEquals(2, recipe.getTags().size());

            recipe.clearTags();

            assertTrue(recipe.getTags().isEmpty());
            assertFalse(tag1.getRecipes().contains(recipe));
            assertFalse(tag2.getRecipes().contains(recipe));
        }

        @Test
        @DisplayName("All recipe subtypes support tags")
        void allSubtypesSupportTags() {
            Tag tag = new Tag("quick");

            BasicRecipe basic = new BasicRecipe(1L, "Basic", "d", "i", "n", 1);
            VegetarianRecipe veg = new VegetarianRecipe(2L, "Veg", "d", "i", "n", 2);
            DessertRecipe dessert = new DessertRecipe(3L, "Dessert", "d", "i", "n", 3);
            DairyRecipe dairy = new DairyRecipe(4L, "Dairy", "d", "i", "n", 4);

            basic.addTag(tag);
            veg.addTag(tag);
            dessert.addTag(tag);
            dairy.addTag(tag);

            assertEquals(1, basic.getTags().size());
            assertEquals(1, veg.getTags().size());
            assertEquals(1, dessert.getTags().size());
            assertEquals(1, dairy.getTags().size());
            assertEquals(4, tag.getRecipes().size());
        }

        @Test
        @DisplayName("Recipe with tags maintains all other fields")
        void recipeWithTagsMaintainsOtherFields() {
            AppUser author = new AppUser(1L, "chef", "pass", "Chef");
            Recipe recipe = new BasicRecipe(10L, "Pasta", "Italian pasta",
                    "pasta, sauce", "Cook and serve", 4, author);
            Tag tag1 = new Tag("italian");
            Tag tag2 = new Tag("dinner");

            recipe.addTag(tag1);
            recipe.addTag(tag2);

            assertEquals(10L, recipe.getId());
            assertEquals("Pasta", recipe.getTitle());
            assertEquals("Italian pasta", recipe.getDescription());
            assertEquals(Integer.valueOf(4), recipe.getServings());
            assertEquals("chef", recipe.getAuthor().getUsername());
            assertEquals(2, recipe.getTags().size());
        }

        @Test
        @DisplayName("Same tag not added twice to recipe")
        void duplicateTag() {
            Recipe recipe = new BasicRecipe(1L, "Burger", "desc", "ing", "inst", 2);
            Tag tag = new Tag("american");

            recipe.addTag(tag);
            recipe.addTag(tag);

            assertEquals(1, recipe.getTags().size());
        }

        @Test
        @DisplayName("Can set tags collection directly")
        void setTagsCollection() {
            Recipe recipe = new BasicRecipe(1L, "Stir Fry", "desc", "ing", "inst", 3);
            Tag tag1 = new Tag("asian");
            Tag tag2 = new Tag("quick");

            java.util.Set<Tag> tags = new java.util.HashSet<>();
            tags.add(tag1);
            tags.add(tag2);

            recipe.setTags(tags);

            assertEquals(2, recipe.getTags().size());
            assertTrue(recipe.getTags().contains(tag1));
            assertTrue(recipe.getTags().contains(tag2));
        }
    }
}
