-- fix-recipe-type.sql
-- Safe, minimal SQL to make the discriminator column tolerant of Hibernate's schema update.
-- Review before running. This script assumes the H2 database file is ./data/recipeshare (relative to the project directory).

-- 1) Backfill any NULLs in the main table
UPDATE RECIPES SET RECIPE_TYPE = 'BASIC' WHERE RECIPE_TYPE IS NULL;

-- 2) Make the column nullable so Hibernate's temporary COPY inserts won't fail
ALTER TABLE RECIPES ALTER COLUMN RECIPE_TYPE DROP NOT NULL;

-- 3) Set a default for future inserts/DDL operations
ALTER TABLE RECIPES ALTER COLUMN RECIPE_TYPE SET DEFAULT 'BASIC';

-- 4) (Optional) If temporary copy tables exist (e.g. RECIPES_COPY_3_0), uncomment and run these
-- UPDATE RECIPES_COPY_3_0 SET RECIPE_TYPE = 'BASIC' WHERE RECIPE_TYPE IS NULL;
-- ALTER TABLE RECIPES_COPY_3_0 ALTER COLUMN RECIPE_TYPE DROP NOT NULL;
-- ALTER TABLE RECIPES_COPY_3_0 ALTER COLUMN RECIPE_TYPE SET DEFAULT 'BASIC';

-- 5) Show the table and a few rows to verify
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE 'RECIPES%';
SELECT ID, TITLE, RECIPE_TYPE FROM RECIPES LIMIT 10;

