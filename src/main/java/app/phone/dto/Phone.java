package app.phone.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="phone")
public class Phone {
	@Id
	@Column(name="phoneid")
	int phoneId;
	String phoneName;
	int phoneprice;
	String phonemaker;
	int phoneremain;
		
	public Phone() {}
	@Override
	public String toString() {
		return "Phone [phoneId=" + phoneId + ", phoneName=" + phoneName + "]";
	}

	public Phone(int phoneId,String phoneName) {
		this.phoneId = phoneId;
		this.phoneName = phoneName;
	}
	
	public Phone(String name, int price, String maker, int remain) {
		
		this.phoneName = name;
		this.phoneprice = price;
		this.phonemaker = maker;
		this.phoneremain = remain;
	}

	public int getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(int phoneId) {
		this.phoneId = phoneId;
	}

	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	
	public int getPhoneprice() {
		return phoneprice;
	}
	public void setPhoneprice(int phoneprice) {
		this.phoneprice = phoneprice;
	}
	public String getPhonemaker() {
		return phonemaker;
	}
	public void setPhonemaker(String phonemaker) {
		this.phonemaker = phonemaker;
	}
	public int getPhoneremain() {
		return phoneremain;
	}
	public void setPhoneremain(int phoneremain) {
		this.phoneremain = phoneremain;
	}
	
	
	
}
