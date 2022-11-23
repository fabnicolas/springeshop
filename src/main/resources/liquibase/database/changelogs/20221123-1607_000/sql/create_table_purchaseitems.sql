CREATE TABLE purchase_items (
	id int8 NOT NULL,
	article_id int8 NULL,
	user_id int8 NULL,
	CONSTRAINT pk_purchase_items PRIMARY KEY (id)
);
