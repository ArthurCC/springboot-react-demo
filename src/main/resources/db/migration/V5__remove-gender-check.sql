-- Remove check constraint on gender which is unnecessary now that we have defined our custom enum
ALTER TABLE students DROP CONSTRAINT IF EXISTS students_gender_check;