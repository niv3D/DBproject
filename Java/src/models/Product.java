package models;

public class Product {

	private final int id;
	private final String name;
	private final int categoryId;
	private final float price;
	private final String description;
	private final int quantityInStock;

	private Product(ProductBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.categoryId = builder.categoryId;
		this.price = builder.price;
		this.description = builder.description;
		this.quantityInStock = builder.quantityInStock;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public float getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public int getQuantityInStock() {
		return quantityInStock;
	}

	public static class ProductBuilder {

		private int id;
		private final String name;
		private int categoryId;
		private final float price;
		private final String description;
		private int quantityInStock;

		public ProductBuilder(String name, float price, String description) {
			this.name = name;
			this.price = price;
			this.description = description;
		}

		public ProductBuilder id(int id) {
			this.id = id;
			return this;
		}

		public ProductBuilder categoryId(int categoryId) {
			this.categoryId = categoryId;
			return this;
		}

		public ProductBuilder quantityInStock(int quantityInStock) {
			this.quantityInStock = quantityInStock;
			return this;
		}

		public Product build() {
			return new Product(this);
		}

	}

}
