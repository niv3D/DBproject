package models;

import java.sql.Date;

public class InventoryRecord {
	
	 
	public InventoryRecord(int id,Integer productId,Integer quantity, Date date,String notes) {
		this.id = id;
		this.productId =productId;
		this.quantity= quantity;
		this.date = date;
		this.notes= notes;
	}
	
	public InventoryRecord(Integer productId,Integer quantity,String notes) {
		this.id = null;
		this.productId =productId;
		this.quantity= quantity;
		this.date = null;
		this.notes= notes;
	}
	
	public final Integer id;
	public final Integer productId;
	public final Integer quantity;
	public final Date date;
	public final String notes;
}
