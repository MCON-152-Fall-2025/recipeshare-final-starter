package com.mcon152.recipeshare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mcon152.recipeshare.web.RecipeController;
import com.mcon152.recipeshare.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddRecipe() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("title", "Cake");
        json.put("description", "Delicious cake");
        ArrayNode ingredients = mapper.createArrayNode();
        ingredients.add("1 cup of flour");
        ingredients.add("1 cup of sugar");
        ingredients.add("3 eggs");
        json.set("ingredients", ingredients);
        json.put("instructions", "Mix and bake");
        String jsonString = mapper.writeValueAsString(json);
        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cake"))
                .andExpect(jsonPath("$.description").value("Delicious cake"))
                .andExpect(jsonPath("$.ingredients").value("Flour, Sugar, Eggs"))
                .andExpect(jsonPath("$.instructions").value("Mix and bake"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void testGetAllRecipes() throws Exception {
        String json = "{\"title\":\"Pie\",\"description\":\"Apple pie\",\"ingredients\":\"Apples, Flour, Sugar\",\"instructions\":\"Mix and bake\"}";
        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Pie"));
    }
}
