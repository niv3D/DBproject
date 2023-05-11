package models;

public class Category {

	private final int id;
	private final String name;

	private Category(CategoryBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static class CategoryBuilder {
		private int id;
		private final String name;

		public CategoryBuilder(String name) {
			this.name = name;
		}

		public CategoryBuilder id(int id) {
			this.id = id;
			return this;
		}

		public Category build() {
			return new Category(this);
		}
	}
}
