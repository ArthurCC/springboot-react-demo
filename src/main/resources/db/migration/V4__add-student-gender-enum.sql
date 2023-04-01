-- Create gender enum type
CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');

-- USING statement cast actual values in DB which are varchar to specified type
-- ERROR: column "gender" cannot be cast automatically to type gender
ALTER TABLE students ALTER COLUMN gender TYPE gender USING gender::gender;

