package com.payment.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.payment.domain.PaymentAmount;
import com.payment.exception.PaymentException;

/**
 * This class is an implementation of the calculator interface
 *
 */
public class FindPaymentCalculator implements IFindPaymentCalculator {
	
	private static final PaymentAmount ZERO = new PaymentAmount(0);
	
	/**
	 * Logger
	 * Logger level = WARNING (Disable info)
	 */
	private static Logger logger = Logger.getLogger(FindPaymentCalculator.class.getSimpleName());
	static {
		logger.setLevel(Level.WARNING);
	}

	public FindPaymentCalculator() {
		super();
	}
	
	/**
	 * Interface implementation
	 */
	public List<PaymentAmount> findPayments(PaymentAmount bankTransfer, List<PaymentAmount> duePayments) throws PaymentException {
		// Validate parameters.
		if (bankTransfer == null) {
			throw new PaymentException("No bank transfer");
		}
		if (duePayments == null) {
			throw new PaymentException("No due payments");
		}
		
		// Duration.
		long startTime = new Date().getTime();

		// Sort the due payments.
		Collections.sort(duePayments, new Comparator<PaymentAmount>() {

			@Override
			public int compare(PaymentAmount arg0, PaymentAmount arg1) {
				return arg0.compareTo(arg1);
			}
		});

		// Map to avoid calculating several times with the same values.
		Map<PaymentAmountKey, List<PaymentAmount>> visitors = new HashMap<PaymentAmountKey, List<PaymentAmount>>();
		
		// Call recursive method. 
		List<PaymentAmount> foundPayments = calculatePayments(bankTransfer, duePayments,visitors);
	
		// Duration.
		long endTime = new Date().getTime();
		long duration = (endTime - startTime);
		
		logger.log(Level.INFO, "Duration (in seconds): " + duration / 1000.0);
		
		return foundPayments;
	}
	
	// Recursive method.
	private List<PaymentAmount> calculatePayments(PaymentAmount bankTransfer, List<PaymentAmount> duePayments, Map<PaymentAmountKey, List<PaymentAmount>> visitors) throws PaymentException {
		PaymentAmountKey paymentAmountKey = new PaymentAmountKey(bankTransfer, duePayments);
		
		if (visitors.containsKey(paymentAmountKey))
			return visitors.get(paymentAmountKey);
		
		// Log.
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO, "Find payments for bank transfer: " + bankTransfer.toString() + " and due payments: " + duePayments.toString());
		}
	
		// ---------------------------------------------------------------------------------------------------
		// 1- Iterate through the due payments to remove the too high amounts compared to the bank transfer.
		// ---------------------------------------------------------------------------------------------------
		int duePaymentSize = duePayments.size();
		
		// Get the sublist max index.
		int maxIndex = -1;
		for (int paymentIndex = duePaymentSize - 1; paymentIndex >= 0; paymentIndex--) {
			if (duePayments.get(paymentIndex).compareTo(bankTransfer) <= 0) {
				maxIndex = paymentIndex;
				break;
			}
		}
		
		// Extract sublist.
		List<PaymentAmount> filteredDuePayments;	
		if (maxIndex < 0) {
			filteredDuePayments = new ArrayList<PaymentAmount>();
		} else if (maxIndex < duePaymentSize-1) {
			filteredDuePayments = duePayments.subList(0, maxIndex + 1);
		} else {
			filteredDuePayments = duePayments;
		}
	
		// Log.
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO, "Filtered list: " + filteredDuePayments.toString());
		}
	
		// ---------------------------------------------------------------------------------------------------
		// 2- Find the payments on the (n-1) sublists (all combinations).
		// ---------------------------------------------------------------------------------------------------
		int filteredDuePaymentsSize = filteredDuePayments.size();
		
		if (filteredDuePaymentsSize == 0) {
			List<PaymentAmount> list;
			
			// Nothing more to do.
			if (bankTransfer.equals(ZERO)) {
				// It works.
				list = new ArrayList<PaymentAmount>();
			} else {
				// No solution.
				list = null;
			}
			
			visitors.put(paymentAmountKey, list);
			return list;
			
		} else {
			// Iterate through the due payments.
			for (int paymentIndex = 0; paymentIndex < filteredDuePaymentsSize; paymentIndex++) {
				// Find the (n-1) solution.
				List<PaymentAmount> newDuePayments;
				if (paymentIndex == 0) {
					newDuePayments = filteredDuePayments.subList(1, filteredDuePaymentsSize);
				} else if (paymentIndex == filteredDuePaymentsSize - 1) {
					newDuePayments = filteredDuePayments.subList(0, filteredDuePaymentsSize - 1);
				} else {
					List<PaymentAmount> subListBefore = filteredDuePayments.subList(0, paymentIndex);
					List<PaymentAmount> subListAfter = filteredDuePayments.subList(paymentIndex+1, filteredDuePaymentsSize);
					newDuePayments = new ArrayList<PaymentAmount>(subListBefore);
					newDuePayments.addAll(subListAfter);
				}
				
				PaymentAmount currentDuePayment = filteredDuePayments.get(paymentIndex);
				
				// Pass bank transfer minus current item.
				PaymentAmount newBankTransfer = new PaymentAmount(bankTransfer.getAmount()-currentDuePayment.getAmount());
				
				List<PaymentAmount> result = calculatePayments(newBankTransfer, newDuePayments, visitors);
		
				if (result != null) {
					// It works.
					result.add(currentDuePayment);
					visitors.put(paymentAmountKey, result);
					return result;
				}
			}
			
			visitors.put(paymentAmountKey, null);
			return null;
		}
	}
	
	
	private static class PaymentAmountKey {
		PaymentAmount amount;
		List<PaymentAmount> duePayments;
		
		
		public PaymentAmountKey(PaymentAmount amount,
				List<PaymentAmount> duePayments) {
			super();
			this.amount = amount;
			this.duePayments = duePayments;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((amount == null) ? 0 : amount.hashCode());
			result = prime * result
					+ ((duePayments == null) ? 0 : duePayments.hashCode());
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
			PaymentAmountKey other = (PaymentAmountKey) obj;
			if (amount == null) {
				if (other.amount != null)
					return false;
			} else if (!amount.equals(other.amount))
				return false;
			if (duePayments == null) {
				if (other.duePayments != null)
					return false;
			} else if (!duePayments.equals(other.duePayments))
				return false;
			return true;
		}
	}

}

	