# Recipe & User Data Loaders

This project includes automatic data loaders that populate the database with sample users and recipes when the application starts.

## Overview

Two data loaders run automatically on application startup:

1. **UserDataLoader** - Loads sample users from `users.json`
2. **RecipeDataLoader** - Loads sample recipes from `recipes.json`

The loaders only run if the database is empty, so they won't duplicate data on subsequent runs.

## Data Files Location

Sample data files are located in:
```
src/main/resources/data/
├── users.json
└── recipes.json
```

## Sample Users

The system includes 5 pre-configured users:

| Username | Display Name | Password |
|----------|--------------|----------|
| chef_john | Chef John | password123 |
| mary_baker | Mary Baker | password123 |
| veggie_chef | Veggie Chef | password123 |
| dessert_master | Dessert Master | password123 |
| italian_cook | Italian Cook | password123 |

## Sample Recipes

12 sample recipes are included covering different types:
- **BASIC**: Spaghetti Carbonara, Grilled Chicken, Pizza Margherita, Caesar Salad, Beef Tacos
- **VEGETARIAN**: Mediterranean Quinoa Salad, Veggie Stir-Fry, Caprese Salad
- **DESSERT**: Chocolate Chip Cookies, New York Cheesecake, Chocolate Brownies
- **DAIRY**: Creamy Mac and Cheese

Each recipe includes:
- Title, description, ingredients, and instructions
- Author (linked to a user)
- Tags for categorization
- Number of servings

## Customizing the Data

### Adding Users

Edit `src/main/resources/data/users.json`:

```json
[
  {
    "username": "new_user",
    "password": "password123",
    "displayName": "New User Name"
  }
]
```

**Fields:**
- `username` - Unique username (required)
- `password` - User password (required) - **Note:** In production, use hashed passwords
- `displayName` - User's display name (optional)

### Adding Recipes

Edit `src/main/resources/data/recipes.json`:

```json
[
  {
    "type": "BASIC",
    "title": "Recipe Title",
    "description": "Recipe description",
    "ingredients": "List of ingredients",
    "instructions": "Step by step instructions",
    "servings": 4,
    "authorUsername": "chef_john",
    "tags": ["tag1", "tag2"]
  }
]
```

**Fields:**
- `type` - Recipe type: `BASIC`, `VEGETARIAN`, `DESSERT`, or `DAIRY` (default: BASIC)
- `title` - Recipe title (required)
- `description` - Brief description (optional)
- `ingredients` - Ingredients list (required)
- `instructions` - Cooking instructions (required)
- `servings` - Number of servings (default: 1)
- `authorUsername` - Username of the recipe author (optional)
- `tags` - Array of tag names (optional)

## Tags

Tags are automatically created when recipes are loaded. The sample data includes tags like:
- italian, pasta, dinner, quick
- vegetarian, healthy, salad
- dessert, cookies, chocolate, baking
- comfort-food, cheese
- mexican, family-friendly

## How It Works

1. **Startup**: When the Spring Boot application starts, the loaders run automatically
2. **Check**: Each loader checks if data already exists
3. **Load**: If the database is empty, data is loaded from JSON files
4. **Order**: UserDataLoader runs first (@Order(1)), RecipeDataLoader runs second (@Order(2))
5. **Relationships**: Recipes are linked to users by username, tags are created automatically

## Clearing Data

To reload the sample data:

1. Stop the application
2. Delete the database file(s) in the `data/` directory
3. Restart the application - data will reload automatically

## Technical Details

### Dependencies

The loaders use:
- **Jackson ObjectMapper** - JSON parsing
- **Spring Boot CommandLineRunner** - Startup execution
- **JPA Repositories** - Database access
- **SLF4J Logger** - Logging

### Loader Classes

- `com.mcon152.recipeshare.loader.UserDataLoader`
- `com.mcon152.recipeshare.loader.RecipeDataLoader`

### Repositories

- `AppUserRepository` - User data access
- `RecipeRepository` - Recipe data access
- `TagRepository` - Tag data access

## Disabling Data Loaders

To disable automatic data loading, you can:

1. **Delete the JSON files** from `src/main/resources/data/`
2. **Remove the loaders** from the classpath
3. **Use Spring Profiles** to conditionally load data:

```java
@Component
@Order(1)
@Profile("dev") // Only load in 'dev' profile
public class UserDataLoader implements CommandLineRunner {
    // ...
}
```

## Error Handling

- If JSON files are missing, loaders will log an error but the application will continue
- If users are missing when loading recipes, recipes will be created without authors
- If duplicate usernames exist, the loader will skip that user
- Invalid recipe types default to BASIC

## Logging

Check the application logs for data loading information:

```
INFO  - Loading sample users from JSON...
INFO  - Successfully loaded 5 users
INFO  - Loading sample recipes from JSON...
INFO  - Successfully loaded 12 recipes
```

## For Students

This data loader system provides you with:
- Ready-to-use sample data for testing
- Examples of different recipe types
- Sample user accounts
- Tagged recipes for filtering exercises

You can modify the JSON files to add your own test data or create additional loaders for other entities.

