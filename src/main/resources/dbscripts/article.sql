CREATE TABLE articles(
	id BIGINT,
	name varchar(200) not null,
	description varchar(200) not null,
	price double precision,
	primary key(id)
)