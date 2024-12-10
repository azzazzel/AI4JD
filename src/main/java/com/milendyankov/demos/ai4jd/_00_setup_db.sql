DROP TABLE books;

CREATE TABLE books (
	title TEXT,
	description TEXT,
	authors TEXT,
	publisher TEXT,
	publishedDate TEXT,
	genres TEXT,
	ratingsCount NUMERIC
);

COPY books (title, description, authors, publisher, publishedDate, genres, ratingsCount)
FROM 'data.csv'
DELIMITER ','
CSV HEADER;

CREATE EXTENSION vector;