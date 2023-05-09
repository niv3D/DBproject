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

DESCRIBE inventoryrecords ;
DESCRIBE products;

SELECT * FROM categories;

SELECT * FROM products;


DELETE FROM products WHERE id > 1;



SELECT id,name,category_id,price,description FROM products WHERE name LIKE '%d%';

ALTER TABLE inventoryrecords MODIFY quantity INT NOT NULL;