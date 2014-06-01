package com.payment.algo;

import java.util.List;

import com.payment.domain.PaymentAmount;
import com.payment.exception.PaymentException;

public interface IFindPaymentCalculator {
	/**
	 * return a list of due payments for which the sum is equal to the bank transfer.
	 * Produce the list of elements which sum is equal to the bank transfer
	 * @param bankTransfer
	 * @param duePayments
	 * @return foundPayments
	 * @throws PaymentException
	 */
	public List<PaymentAmount> findPayments(PaymentAmount bankTransfer, List<PaymentAmount> duePayments) throws PaymentException;
}
