package com.payment.domain;

/**
 * Payment amount
 *
 */
public class PaymentAmount implements Comparable<PaymentAmount>{
	private int amount;

	public PaymentAmount() {
		this(0);
	}
	public PaymentAmount(int amount) {
		super();
		this.amount = amount;
	}
	
	public PaymentAmount(float amount) {
		super();
		this.amount = convertToInt(amount);
	}
	
	public static int convertToInt(float amount) {
		return (int)Math.rint(amount * 100.0);
	}

	public int getAmount() {
		return amount;
	}

	public void addAmount(PaymentAmount newAmount) {
		this.amount += newAmount.amount;
	}

	public void substractAmount(PaymentAmount newAmount) {
		this.amount -= newAmount.amount;
	}
	
	public void setAmount(float amount) {
		this.amount = convertToInt(amount);
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String toString() {
		float floatValue = amount / 100.0f;
		String floatValueStr = String.format("%.2f", floatValue);
		return floatValueStr;
	}

	public int compareTo(PaymentAmount paymentAmount2) {
		if (amount == paymentAmount2.amount) {
			return 0;
		} else if (amount < paymentAmount2.amount) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentAmount other = (PaymentAmount) obj;
		if (amount != other.amount)
			return false;
		return true;
	}	
}
