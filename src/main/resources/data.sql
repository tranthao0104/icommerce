-- Category
--insert into category(name, is_deleted, created_at, updated_at) values ('Category 1', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
--insert into category(name, is_deleted, created_at, updated_at) values ('Category 2', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
---- Brand
--insert into brand(name, is_deleted, created_at, updated_at) values ('Brand 1', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
--insert into brand(name, is_deleted, created_at, updated_at) values ('Brand 2', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
---- Color
--insert into color(name, is_deleted, created_at, updated_at) values ('White', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
--insert into color(name, is_deleted, created_at, updated_at) values ('Black', false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
---- Product
--INSERT INTO product  (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
--VALUES ('Product 1', 'Product 1', 10, 35.75, 3, 1, 2, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
--INSERT INTO product (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
--VALUES ('Product 2', 'Product 2', 20, 49.75, 1, 2, 3, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());
INSERT INTO product (name, description, quantity, price, category_id, brand_id, color_id, is_deleted, created_at, updated_at)
VALUES ('Product 33', 'Product 33', 20, 49.75, 1, 2, 3, false, CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP ());