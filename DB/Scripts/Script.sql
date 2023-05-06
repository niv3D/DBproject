CREATE TABLE categories(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE products(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	category_id INT, FOREIGN KEY(category_id) REFERENCES categories(id),
	price DECIMAL(10,2),
	description TEXT
);

CREATE TABLE inventoryrecords (
	id INT PRIMARY KEY AUTO_INCREMENT,
	product_id INT, FOREIGN KEY(product_id) REFERENCES products(id),
	quantity INT,
	date DATE,
	notes TEXT
);

DESCRIBE products;

SELECT id,name FROM products WHERE id = 7;

INSERT INTO products (name,price,description) VALUES ('dummy',999.99,'hi very good product');

DELETE FROM products WHERE id > 1;

ALTER TABLE categories MODIFY 