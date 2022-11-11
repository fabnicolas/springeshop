create table tutorials(
	id BIGINT not null,
	title varchar(100) not null,
	description varchar(500),
	published boolean not null default false,
	primary KEY(id)
)
