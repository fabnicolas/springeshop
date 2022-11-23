CREATE TABLE articles (
	id int8 NOT NULL,
	description varchar(255) NULL,
	"name" varchar(255) NULL,
	price float8 NULL,
	CONSTRAINT pk_articles PRIMARY KEY (id)
);
