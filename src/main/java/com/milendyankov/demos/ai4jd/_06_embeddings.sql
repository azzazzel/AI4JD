
-- create new table for vector search 
CREATE TABLE books_embeddings AS
SELECT * FROM books_ts;

-- add column for 3 dimentional vector
ALTER TABLE books_embeddings
ADD COLUMN embeddings VECTOR;

SELECT embeddings FROM books_embeddings;