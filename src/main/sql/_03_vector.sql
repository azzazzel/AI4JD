
-- create new table for vector search 
CREATE TABLE books_vector AS 
SELECT * FROM books_ts;

-- add column for 3 dimentional vector
ALTER TABLE books_vector 
ADD COLUMN embedding VECTOR(3); 

SELECT embedding FROM books_vector;
