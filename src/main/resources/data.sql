INSERT INTO USER (username, password, firstname, lastname, email)
VALUES ('Theo','password123','Theo','Persson','theo@trivimania.se');

INSERT INTO USER (username, password, firstname, lastname, email, avatar_id)
VALUES ('Joel','password123','Joel','Rosenstam','joel@trivimania.se', 5);

INSERT INTO USER (username, password, firstname, lastname, email)
VALUES ('Melody','password123','Melody','Vikström','melody@trivimania.se');

INSERT INTO USER (username, password, firstname, lastname, email)
VALUES ('Veronica','password123','Veronica','Danko','veronica@trivimania.se');

INSERT INTO USER (username, password, firstname, lastname, email)
VALUES ('Felix','password123','Felix','Frost','felix@trivimania.se');

INSERT INTO SECRET (question, correct_answer, incorrect_answers[])
VALUES ('Vilka av dessa har varit gruppers projekt namn?', 'SirQuizAlot, Bitter, IMFDB, SneakySnakey', {('SirQuizLot, Ditter, IMSDB, Sneakysnake'), ('CatVikingQuiz, Birdy Fake IMDB, SnakeClone'), ('JustAnotherQuiz, TwitterClone, BlatentRipOff, Snake... Just snake')});