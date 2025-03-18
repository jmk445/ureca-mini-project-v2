package app.phone.dto;

import java.util.List;

public class ShoppingCart {
	int shoppingCartId;
	int customerId;
	List<Phone> addedPhones;
	
	
	
	public ShoppingCart(int shoppingCartId, int customerId, List<Phone> addedPhones) {
		super();
		this.shoppingCartId = shoppingCartId;
		this.customerId = customerId;
		this.addedPhones = addedPhones;
	}
	
	public int getShoppingCartId() {
		return shoppingCartId;
	}
	public void setShoppingCartId(int shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public List<Phone> getAddedPhones() {
		return addedPhones;
	}
	public void setAddedPhones(List<Phone> addedPhones) {
		this.addedPhones = addedPhones;
	}
	
	
}
