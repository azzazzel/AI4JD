-- get the tsvectors for the descriptions
SELECT to_tsvector('english', description)
FROM books;

-- search with converting to tsvector on the fly 
SELECT
	ts_headline('english', description, query) AS highlighted,
	ts_rank_cd(vector, query) AS rank
FROM
	books,
	to_tsquery('piano') query,
	to_tsvector('english', description) vector
WHERE vector @@ query
ORDER BY rank DESC;

-- create new table with a dedicated tsvector column 
CREATE TABLE books_ts AS 
SELECT 
	*,
	to_tsvector('english', description) AS tokens
FROM books;


SELECT
	ts_headline('english', description, query) AS highlighted,
	ts_rank_cd(tokens, query) AS rank
FROM
	books_ts,
	to_tsquery('piano') query
WHERE tokens @@ query
ORDER BY rank DESC;


