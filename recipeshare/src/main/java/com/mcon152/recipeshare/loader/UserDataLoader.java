package com.mcon152.recipeshare.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcon152.recipeshare.domain.AppUser;
import com.mcon152.recipeshare.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Loads sample users from JSON file into the database on application startup.
 * This runs before RecipeDataLoader to ensure users exist when recipes reference them.
 */
@Component
@Order(1)
public class UserDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UserDataLoader.class);
    private final AppUserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserDataLoader(AppUserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            logger.info("Users already exist in database. Skipping data load.");
            return;
        }

        logger.info("Loading sample users from JSON...");

        try {
            List<AppUser> users = loadUsersFromJson();
            userRepository.saveAll(users);
            logger.info("Successfully loaded {} users", users.size());

            users.forEach(user ->
                logger.debug("Loaded user: {} ({})", user.getUsername(), user.getDisplayName())
            );
        } catch (Exception e) {
            logger.error("Failed to load users from JSON: {}", e.getMessage(), e);
        }
    }

    private List<AppUser> loadUsersFromJson() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/users.json");

        try (InputStream inputStream = resource.getInputStream()) {
            AppUser[] usersArray = objectMapper.readValue(inputStream, AppUser[].class);
            return Arrays.asList(usersArray);
        }
    }
}
