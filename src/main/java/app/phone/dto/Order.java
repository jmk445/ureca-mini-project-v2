package app.phone.dto;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private String customerName;
    private String OrderPhoneString;
    
   
	public String getOrderPhoneString() {
		return OrderPhoneString;
	}

	public void setOrderPhoneString(String orderPhoneString) {
		OrderPhoneString = orderPhoneString;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	private Timestamp orderTime;  // 주문 시간 (DATETIME or TIMESTAMP)
    private List<Phone> orderedPhones;  // 주문한 휴대폰 리스트

    // Getter & Setter
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public List<Phone> getOrderedPhones() {
        return orderedPhones;
    }

    public void setOrderedPhones(List<Phone> orderedPhones) {
        this.orderedPhones = orderedPhones;
    }
}
