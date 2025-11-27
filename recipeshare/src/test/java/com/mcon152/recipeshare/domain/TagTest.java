package com.mcon152.recipeshare.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tag Domain Tests")
class TagTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor creates empty Tag")
        void defaultConstructor() {
            Tag tag = new Tag();
            assertNotNull(tag);
            assertNull(tag.getId());
            assertNull(tag.getName());
            assertNull(tag.getDescription());
            assertNotNull(tag.getRecipes());
            assertTrue(tag.getRecipes().isEmpty());
        }

        @Test
        @DisplayName("Constructor with name only")
        void constructorWithName() {
            Tag tag = new Tag("vegetarian");
            assertEquals("vegetarian", tag.getName());
            assertNull(tag.getDescription());
            assertNull(tag.getId());
        }

        @Test
        @DisplayName("Constructor with name and description")
        void constructorWithNameAndDescription() {
            Tag tag = new Tag("italian", "Italian cuisine");
            assertEquals("italian", tag.getName());
            assertEquals("Italian cuisine", tag.getDescription());
            assertNull(tag.getId());
        }

        @Test
        @DisplayName("Constructor with all fields")
        void constructorWithAllFields() {
            Tag tag = new Tag(1L, "dessert", "Sweet treats");
            assertEquals(1L, tag.getId());
            assertEquals("dessert", tag.getName());
            assertEquals("Sweet treats", tag.getDescription());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("setName and getName work correctly")
        void nameGetterSetter() {
            Tag tag = new Tag();
            tag.setName("spicy");
            assertEquals("spicy", tag.getName());
        }

        @Test
        @DisplayName("setDescription and getDescription work correctly")
        void descriptionGetterSetter() {
            Tag tag = new Tag();
            tag.setDescription("Hot and spicy dishes");
            assertEquals("Hot and spicy dishes", tag.getDescription());
        }

        @Test
        @DisplayName("setId inherited from BaseEntity works correctly")
        void idGetterSetter() {
            Tag tag = new Tag();
            tag.setId(100L);
            assertEquals(100L, tag.getId());
        }

        @Test
        @DisplayName("Can update all fields after creation")
        void updateAllFields() {
            Tag tag = new Tag("old_name", "old description");

            tag.setId(5L);
            tag.setName("new_name");
            tag.setDescription("new description");

            assertEquals(5L, tag.getId());
            assertEquals("new_name", tag.getName());
            assertEquals("new description", tag.getDescription());
        }
    }

    @Nested
    @DisplayName("Recipe Relationship Tests")
    class RecipeRelationshipTests {

        @Test
        @DisplayName("Tag starts with empty recipes set")
        void emptyRecipesSet() {
            Tag tag = new Tag("vegan");
            assertNotNull(tag.getRecipes());
            assertTrue(tag.getRecipes().isEmpty());
            assertEquals(0, tag.getRecipes().size());
        }

        @Test
        @DisplayName("addRecipe adds recipe to both sides of relationship")
        void addRecipe() {
            Tag tag = new Tag("quick");
            Recipe recipe = new BasicRecipe(1L, "Quick Pasta", "Fast meal", "pasta", "boil", 2);

            tag.addRecipe(recipe);

            assertTrue(tag.getRecipes().contains(recipe));
            assertTrue(recipe.getTags().contains(tag));
        }

        @Test
        @DisplayName("removeRecipe removes from both sides")
        void removeRecipe() {
            Tag tag = new Tag("healthy");
            Recipe recipe = new BasicRecipe(1L, "Salad", "Fresh salad", "lettuce", "mix", 1);

            tag.addRecipe(recipe);
            assertTrue(tag.getRecipes().contains(recipe));

            tag.removeRecipe(recipe);
            assertFalse(tag.getRecipes().contains(recipe));
            assertFalse(recipe.getTags().contains(tag));
        }

        @Test
        @DisplayName("Tag can have multiple recipes")
        void multipleRecipes() {
            Tag tag = new Tag("italian");
            Recipe recipe1 = new BasicRecipe(1L, "Pizza", "desc", "ing", "inst", 4);
            Recipe recipe2 = new BasicRecipe(2L, "Pasta", "desc", "ing", "inst", 2);
            Recipe recipe3 = new BasicRecipe(3L, "Risotto", "desc", "ing", "inst", 4);

            tag.addRecipe(recipe1);
            tag.addRecipe(recipe2);
            tag.addRecipe(recipe3);

            assertEquals(3, tag.getRecipes().size());
            assertTrue(tag.getRecipes().contains(recipe1));
            assertTrue(tag.getRecipes().contains(recipe2));
            assertTrue(tag.getRecipes().contains(recipe3));
        }

        @Test
        @DisplayName("Same recipe not added twice")
        void duplicateRecipe() {
            Tag tag = new Tag("breakfast");
            Recipe recipe = new BasicRecipe(1L, "Pancakes", "desc", "ing", "inst", 4);

            tag.addRecipe(recipe);
            tag.addRecipe(recipe);

            assertEquals(1, tag.getRecipes().size());
        }

        @Test
        @DisplayName("Can set recipes collection directly")
        void setRecipesCollection() {
            Tag tag = new Tag("dinner");
            Recipe recipe1 = new BasicRecipe(1L, "Steak", "desc", "ing", "inst", 2);
            Recipe recipe2 = new BasicRecipe(2L, "Fish", "desc", "ing", "inst", 2);

            java.util.Set<Recipe> recipes = new java.util.HashSet<>();
            recipes.add(recipe1);
            recipes.add(recipe2);

            tag.setRecipes(recipes);

            assertEquals(2, tag.getRecipes().size());
            assertTrue(tag.getRecipes().contains(recipe1));
            assertTrue(tag.getRecipes().contains(recipe2));
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Tags with same ID and name are equal")
        void equalTags() {
            Tag tag1 = new Tag(1L, "vegan", "Plant-based");
            Tag tag2 = new Tag(1L, "vegan", "Different description");

            assertEquals(tag1, tag2);
            assertEquals(tag1.hashCode(), tag2.hashCode());
        }

        @Test
        @DisplayName("Tags with different names are not equal")
        void differentNames() {
            Tag tag1 = new Tag(1L, "vegan", "desc");
            Tag tag2 = new Tag(1L, "vegetarian", "desc");

            assertNotEquals(tag1, tag2);
        }

        @Test
        @DisplayName("Tags with different IDs but same name are not equal")
        void differentIds() {
            Tag tag1 = new Tag(1L, "italian", "desc");
            Tag tag2 = new Tag(2L, "italian", "desc");

            assertNotEquals(tag1, tag2);
        }

        @Test
        @DisplayName("Tag equals itself")
        void equalsItself() {
            Tag tag = new Tag("spicy");
            assertEquals(tag, tag);
        }

        @Test
        @DisplayName("Tag not equal to null")
        void notEqualToNull() {
            Tag tag = new Tag("mexican");
            assertNotNull(tag);
        }

        @Test
        @DisplayName("Tags with null IDs but same name are equal")
        void nullIdsSameName() {
            Tag tag1 = new Tag("asian");
            Tag tag2 = new Tag("asian");

            assertEquals(tag1, tag2);
            assertEquals(tag1.hashCode(), tag2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString includes all important fields")
        void toStringIncludesFields() {
            Tag tag = new Tag(1L, "vegetarian", "No meat");
            String result = tag.toString();

            assertTrue(result.contains("Tag"));
            assertTrue(result.contains("id=1"));
            assertTrue(result.contains("name='vegetarian'"));
            assertTrue(result.contains("description='No meat'"));
            assertTrue(result.contains("recipeCount="));
        }

        @Test
        @DisplayName("toString shows recipe count")
        void toStringShowsRecipeCount() {
            Tag tag = new Tag("quick");
            Recipe recipe1 = new BasicRecipe(1L, "Recipe1", "d", "i", "n", 1);
            Recipe recipe2 = new BasicRecipe(2L, "Recipe2", "d", "i", "n", 1);

            tag.addRecipe(recipe1);
            tag.addRecipe(recipe2);

            String result = tag.toString();
            assertTrue(result.contains("recipeCount=2"));
        }

        @Test
        @DisplayName("toString handles null values gracefully")
        void toStringWithNulls() {
            Tag tag = new Tag();
            String result = tag.toString();

            assertNotNull(result);
            assertTrue(result.contains("Tag"));
            assertTrue(result.contains("null"));
        }
    }

    @Nested
    @DisplayName("BaseEntity Inheritance Tests")
    class BaseEntityInheritanceTests {

        @Test
        @DisplayName("Tag extends BaseEntity")
        void extendsBaseEntity() {
            Tag tag = new Tag();
            assertInstanceOf(BaseEntity.class, tag);
        }

        @Test
        @DisplayName("Can access BaseEntity methods")
        void accessBaseEntityMethods() {
            Tag tag = new Tag("breakfast");

            tag.setId(10L);
            assertEquals(10L, tag.getId());

            assertNull(tag.getCreatedAt());
            assertNull(tag.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Tag name is case-sensitive")
        void nameCaseSensitive() {
            Tag tag1 = new Tag("Italian");
            Tag tag2 = new Tag("italian");

            assertNotEquals(tag1.getName(), tag2.getName());
        }

        @Test
        @DisplayName("Description is optional")
        void descriptionOptional() {
            Tag tag = new Tag("gluten-free");
            assertNull(tag.getDescription());

            tag.setDescription("No gluten");
            assertEquals("No gluten", tag.getDescription());

            tag.setDescription(null);
            assertNull(tag.getDescription());
        }

        @Test
        @DisplayName("Can create minimal tag with just name")
        void minimalTag() {
            Tag tag = new Tag();
            tag.setName("simple");

            assertEquals("simple", tag.getName());
            assertNull(tag.getDescription());
            assertNotNull(tag.getRecipes());
        }

        @Test
        @DisplayName("Tag maintains recipes across operations")
        void maintainsRecipes() {
            Tag tag = new Tag("comfort-food");
            Recipe recipe1 = new BasicRecipe(1L, "Mac n Cheese", "d", "i", "n", 4);
            Recipe recipe2 = new BasicRecipe(2L, "Soup", "d", "i", "n", 2);

            tag.addRecipe(recipe1);
            tag.setDescription("Cozy meals");
            tag.addRecipe(recipe2);

            assertEquals(2, tag.getRecipes().size());
            assertEquals("comfort-food", tag.getName());
            assertEquals("Cozy meals", tag.getDescription());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Empty string name is allowed")
        void emptyName() {
            Tag tag = new Tag("");
            assertEquals("", tag.getName());
        }

        @Test
        @DisplayName("Very long tag name")
        void longName() {
            String longName = "a".repeat(50);
            Tag tag = new Tag(longName);
            assertEquals(longName, tag.getName());
        }

        @Test
        @DisplayName("Very long description")
        void longDescription() {
            String longDesc = "x".repeat(200);
            Tag tag = new Tag("tag", longDesc);
            assertEquals(longDesc, tag.getDescription());
        }

        @Test
        @DisplayName("Special characters in name")
        void specialCharacters() {
            Tag tag = new Tag("gluten-free/dairy-free");
            assertEquals("gluten-free/dairy-free", tag.getName());
        }

        @Test
        @DisplayName("Unicode characters in name")
        void unicodeInName() {
            Tag tag = new Tag("中国菜");
            assertEquals("中国菜", tag.getName());
        }

        @Test
        @DisplayName("Removing non-existent recipe doesn't cause error")
        void removeNonExistentRecipe() {
            Tag tag = new Tag("tag");
            Recipe recipe = new BasicRecipe(1L, "Recipe", "d", "i", "n", 1);

            assertDoesNotThrow(() -> tag.removeRecipe(recipe));
            assertTrue(tag.getRecipes().isEmpty());
        }
    }
}

