package com.payment.exception;

public class PaymentException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3955243595900302945L;

	public PaymentException(String msg, Throwable th) {
		super(msg, th);
	}
	
	public PaymentException(String msg) {
		super(msg);
	}
}
