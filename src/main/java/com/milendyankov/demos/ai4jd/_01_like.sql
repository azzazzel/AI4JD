
SELECT * FROM books

-- SELECT * FROM books WHERE description IS ABOUT ('animals in the rainforest');
-- SELECT * FROM books WHERE description IS ABOUT ('scientific achievements in recent years');
-- SELECT * FROM books WHERE description IS ABOUT ('how to play a song on a piano');


SELECT * FROM books
WHERE description LIKE ('%music%');


SELECT * FROM books
WHERE description LIKE ('%piano%')
AND description LIKE ('%play%')

