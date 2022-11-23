CREATE TABLE users (
	id int8 NOT NULL,
	email varchar(255) NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	phone varchar(255) NOT NULL,
	CONSTRAINT uk_email UNIQUE (email),
	CONSTRAINT pk_users PRIMARY KEY (id)
);


