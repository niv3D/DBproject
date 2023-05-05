package models;

public class Product {

	public Product(int id, String name, Integer categoryId, Float price, String description) {
		this.id = id;
		this.name = name;
		this.categoryId = categoryId;
		this.price = price;
		this.description = description;
	}

	public Product(String name, Integer categoryId, Float price, String description) {
		this.id = 0;
		this.name = name;
		this.categoryId = categoryId;
		this.price = price;
		this.description = description;
	}

	public final int id;
	public final String name;
	public final Integer categoryId;
	public final Float price;
	public final String description;

}
