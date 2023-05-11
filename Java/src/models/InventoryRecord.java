package models;

import java.sql.Date;

public class InventoryRecord {

	private final int id;
	private final int quantity;
	private final int productId;
	private final Date date;
	private final String notes;

	public InventoryRecord(InventoryRecordBuilder builder) {
		this.id = builder.id;
		this.productId = builder.productId;
		this.quantity = builder.quantity;
		this.date = builder.date;
		this.notes = builder.notes;
	}

	public int getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getProductId() {
		return productId;
	}

	public Date getDate() {
		return date;
	}

	public String getNotes() {
		return notes;
	}

	public static class InventoryRecordBuilder {

		private int id;
		private final int quantity;
		private final int productId; 
		private  Date date;
		private String notes;

		public InventoryRecordBuilder(int productId, int quantity) {

			this.productId = productId;
			this.quantity = quantity;
			

		}
		
		public InventoryRecordBuilder id(int id) {
			this.id = id;
			return this;
		}
		
		public InventoryRecordBuilder notes(String notes) {
			this.notes=notes;
			return this;
		}
		
		public InventoryRecordBuilder date(Date date) {
			this.date = date;
			return this;
		}
		public InventoryRecord build() {
			return new InventoryRecord(this);
			
		}
	}
}
