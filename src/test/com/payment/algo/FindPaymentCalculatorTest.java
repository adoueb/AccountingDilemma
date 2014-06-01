package com.payment.algo;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.payment.domain.PaymentAmount;
import com.payment.exception.PaymentException;


public class FindPaymentCalculatorTest {
	
	IFindPaymentCalculator calculator = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		calculator = new FindPaymentCalculator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindPaymentsOneValueResult() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(12.10f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(12.10f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue(resultList.size() == 1); 
	}

	@Test
	public void testFindPaymentsTwoValuesResult() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(5.00f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(12.10f));
		duePayments.add(new PaymentAmount(1.10f));
		duePayments.add(new PaymentAmount(2.00f));
		duePayments.add(new PaymentAmount(3.00f));
		duePayments.add(new PaymentAmount(4.10f));
		duePayments.add(new PaymentAmount(6.00f));
		
		List<PaymentAmount> resultPayments = new ArrayList<PaymentAmount>();
		resultPayments.add(new PaymentAmount(2.0f));
		resultPayments.add(new PaymentAmount(3.0f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue("Bank transfer: " + bankTransfer.toString() + " due payments: " + duePayments.toString(), 2 == resultList.size());
		assertTrue(resultList.containsAll(resultPayments)); 
	}

	@Test
	public void testFindPaymentsThreeValuesResult() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(74.06f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(22.75f));
		duePayments.add(new PaymentAmount(59.33f));
		duePayments.add(new PaymentAmount(34.22f));
		duePayments.add(new PaymentAmount(27.21f));
		duePayments.add(new PaymentAmount(17.09f));
		duePayments.add(new PaymentAmount(100.99f));
		
		List<PaymentAmount> resultPayments = new ArrayList<PaymentAmount>();
		resultPayments.add(new PaymentAmount(22.75f));
		resultPayments.add(new PaymentAmount(34.22f));
		resultPayments.add(new PaymentAmount(17.09f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue("Bank transfer: " + bankTransfer.toString() + " due payments: " + duePayments.toString(), 3 == resultList.size());
		assertTrue(resultList.containsAll(resultPayments)); 
	}

	@Test
	public void testFindPaymentsFirstAmongTwoResult() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(27.21f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(27.21f));
		duePayments.add(new PaymentAmount(34.22f));
		
		List<PaymentAmount> resultPayments = new ArrayList<PaymentAmount>();
		resultPayments.add(new PaymentAmount(27.21f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue("Bank transfer: " + bankTransfer.toString() + " due payments: " + duePayments.toString(), 1 == resultList.size());
		assertTrue(resultList.containsAll(resultPayments)); 
	}

	@Test
	public void testFindPaymentsSecondAmongTwoResult() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(34.22f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(27.21f));
		duePayments.add(new PaymentAmount(34.22f));
		
		List<PaymentAmount> resultPayments = new ArrayList<PaymentAmount>();
		resultPayments.add(new PaymentAmount(34.22f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue("Bank transfer: " + bankTransfer.toString() + " due payments: " + duePayments.toString(), 1 == resultList.size());
		assertTrue(resultList.containsAll(resultPayments)); 
	}

	@Test
	public void testFindPaymentsMaxBankTransferResult() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(10000.00f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(5000.00f));
		duePayments.add(new PaymentAmount(2000.00f));
		duePayments.add(new PaymentAmount(3000.00f));
		
		List<PaymentAmount> resultPayments = new ArrayList<PaymentAmount>();
		resultPayments.add(new PaymentAmount(5000.00f));
		resultPayments.add(new PaymentAmount(2000.00f));
		resultPayments.add(new PaymentAmount(3000.00f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue("Bank transfer: " + bankTransfer.toString() + " due payments: " + duePayments.toString(), 3 == resultList.size());
		assertTrue(resultList.containsAll(resultPayments)); 
	}

	@Test
	public void testFindPaymentsNoResult() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(74.06f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(22.75f));
		duePayments.add(new PaymentAmount(59.33f));
		duePayments.add(new PaymentAmount(34.23f));
		duePayments.add(new PaymentAmount(27.21f));
		duePayments.add(new PaymentAmount(17.09f));
		duePayments.add(new PaymentAmount(100.99f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue(resultList == null); 
	}

	@Test
	public void testFindPaymentsNoResultAllHigher() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(4.06f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(22.75f));
		duePayments.add(new PaymentAmount(59.33f));
		duePayments.add(new PaymentAmount(34.22f));
		duePayments.add(new PaymentAmount(27.21f));
		duePayments.add(new PaymentAmount(17.09f));
		duePayments.add(new PaymentAmount(100.99f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue(resultList == null); 
	}

	@Test(expected=PaymentException.class)
	public void testFindPaymentsNoBankTransfer() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = null;
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(22.75f));
		duePayments.add(new PaymentAmount(59.33f));
		duePayments.add(new PaymentAmount(34.23f));
		duePayments.add(new PaymentAmount(27.21f));
		duePayments.add(new PaymentAmount(17.09f));
		duePayments.add(new PaymentAmount(100.99f));
		
		// Apply algo.
		calculator.findPayments(bankTransfer, duePayments);
	}

	@Test(expected=PaymentException.class)
	public void testFindPaymentsNoDuePayments() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(74.06f);
		
		// Apply algo.
		calculator.findPayments(bankTransfer, null);
	}

	@Test
	public void testFindPaymentsEmptyDuePayments() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(74.06f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue(resultList == null); 
	}

	@Test
	public void testFindPaymentsZeroBankTransfer() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(0.00f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(22.75f));
		duePayments.add(new PaymentAmount(59.33f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue("Bank transfer: " + bankTransfer.toString() + " due payments: " + duePayments.toString(), 0 == resultList.size());
	}

	@Test
	public void testFindPaymentsZeroDuePayments() throws PaymentException {
		// Prepare data.
		PaymentAmount bankTransfer = new PaymentAmount(74.06f);
		List<PaymentAmount> duePayments = new ArrayList<PaymentAmount>();
		duePayments.add(new PaymentAmount(0.00f));
		
		// Apply algo.
		List<PaymentAmount> resultList = calculator.findPayments(bankTransfer, duePayments);
		
		// Check result.
		assertTrue(resultList == null); 
	}
}
