
-- create new table for vector search 
CREATE TABLE IF NOT EXISTS books_embeddings AS
SELECT * FROM books_ts;

-- add column for 3 dimentional vector
ALTER TABLE books_embeddings
ADD COLUMN IF NOT EXISTS embeddings VECTOR;

SELECT embeddings FROM books_embeddings;
