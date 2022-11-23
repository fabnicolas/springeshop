ALTER TABLE purchase_items ADD CONSTRAINT fk_articles FOREIGN KEY (article_id) REFERENCES articles(id);
ALTER TABLE purchase_items ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id);
