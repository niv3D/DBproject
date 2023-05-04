CREATE TABLE categories(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name varchar(50) NOT NULL
);

CREATE TABLE products(
	id INT PRIMARY KEY AUTO_INCREMENT,
	category_id INT, FOREIGN KEY(category_id) REFERENCES categories(id),
	quantity_in_stock INT DEFAULT 0,
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

