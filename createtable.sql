USE `cs122b`;

CREATE TABLE IF NOT EXISTS MOVIES 
(
	id varchar(10) NOT NULL DEFAULT '',
    title varchar(100) NOT NULL DEFAULT '',
    year int NOT NULL,
    director varchar(100) NOT NULL DEFAULT '',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS STARS 
(
	id varchar(10) NOT NULL DEFAULT '',
    name varchar(100) NOT NULL DEFAULT '',
    birthYear int,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS STARS_IN_MOVIES 
(
	starId varchar(10) NOT NULL,
    movieId varchar(10) NOT NULL,
	FOREIGN KEY (starId) REFERENCES STARS(id),
    FOREIGN KEY (movieId) REFERENCES MOVIES(id)
);

CREATE TABLE IF NOT EXISTS GENRES 
(
	id int AUTO_INCREMENT,
    name varchar(32) NOT NULL DEFAULT '',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS GENRES_IN_MOVIES 
(
	genreId int NOT NULL,
    movieId varchar(10) NOT NULL,
	FOREIGN KEY (genreId) REFERENCES GENRES(id),
    FOREIGN KEY (movieId) REFERENCES MOVIES(id)
);

CREATE TABLE IF NOT EXISTS CUSTOMERS 
(
	id int AUTO_INCREMENT,
    firstName varchar(50) NOT NULL DEFAULT '',
    lastName varchar(50) NOT NULL DEFAULT '',
    ccId varchar(20) NOT NULL,
    FOREIGN KEY (ccId) REFERENCES CREDITCARDS(id),
    address varchar(200) NOT NULL DEFAULT '',
    email varchar(50) NOT NULL DEFAULT '',
    password varchar(20) NOT NULL DEFAULT '',
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS SALES 
(
	id int AUTO_INCREMENT,
    customerId int NOT NULL,
    movieId varchar(10) NOT NULL,
    FOREIGN KEY (customerId) REFERENCES CUSTOMERS(id),
    FOREIGN KEY (movieId) REFERENCES MOVIES(id),
    saleDate date NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS CREDITCARDS 
(
	id varchar(20) NOT NULL DEFAULT '',
    firstName varchar(50) NOT NULL DEFAULT '',
    lastName varchar(50) NOT NULL DEFAULT '',
    expiration date NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS RATINGS 
(
	moviesId varchar(10) NOT NULL DEFAULT '',
	FOREIGN KEY (moviesId) REFERENCES MOVIES(id),
    rating float NOT NULL,
    numVotes int NOT NULL
);