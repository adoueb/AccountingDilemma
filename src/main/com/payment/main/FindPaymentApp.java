package com.payment.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.payment.algo.FindPaymentCalculator;
import com.payment.algo.IFindPaymentCalculator;
import com.payment.domain.PaymentAmount;
import com.payment.exception.PaymentException;

public class FindPaymentApp {
	private String inputFile;
	private String outputFile;
	private PaymentAmount bankTransfer;
	private List<PaymentAmount> duePayments;
	private IFindPaymentCalculator calculator;
	
	// Bank transfer max value is 10000 dollars.
	private static final double MAX_VALUE = 10000;
	
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(FindPaymentApp.class.getSimpleName());
	
	/**
	 * Param 1: input file
	 * Param 2: output file
	 * @param args
	 */
	public static void main(String[] args) {
		// Get input and output files from arguments (or default files).
		String inputFile = args.length >= 1 ? args[0] : "input.txt";
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO, "Input file is " + inputFile);
		}
		String outputFile = args.length >= 2 ? args[1] : "output.txt";
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO, "Ouput file is " + outputFile);
		}
		
		// Find the payments.
		try {
			FindPaymentApp findPaymentApp = new FindPaymentApp(inputFile, outputFile);

			findPaymentApp.writeResult();
		} catch (PaymentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Build an instance of the application from the input and output files.
	 * @param inputFile
	 * @param outputFile
	 * @throws PaymentException
	 */
	public FindPaymentApp(String inputFile, String outputFile) throws PaymentException {
		super();
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.bankTransfer = new PaymentAmount();
		this.duePayments = new ArrayList<PaymentAmount>();
		this.calculator = new FindPaymentCalculator();

	}

	public void writeResult() throws PaymentException {
		// Read input data and create BankTransfer and DuePayments.
		readInputFile();
		
		// Find the payments.
		List<PaymentAmount> payments = findPayment();
		
		if (payments != null) {
			if (checkResult(payments) == false) {
				throw new PaymentException("Unexpected error: wrong result");
			} else {
				logger.log(Level.INFO, "This is a success");
			}
		}
		
		// Write the result in the output file.
		writeOutputFile(payments);
	}
	
	private void readInputFile() throws PaymentException {
		BufferedReader inputReader = null;
		try {
			// Read line by line.
			inputReader = new BufferedReader(new FileReader(inputFile));
			String line;
			int lineIndex = 1;
			while ((line = inputReader.readLine()) != null) {
				// Process the line.
				if (lineIndex == 1) {
					// This is the bank transfer.
					float bankTransferAmount = Float.parseFloat(line);
					if (bankTransferAmount > MAX_VALUE) {
						throw new PaymentException("Bank transfer " + bankTransferAmount + " is above max accepted amount 10000 dollars");
					}
					bankTransfer.setAmount(bankTransferAmount);
				} else {
					// This is a due payment.
					float duePaymentAmount = Float.parseFloat(line);
					// Accept only due values below bank tranfer max (skip if above).
					if (duePaymentAmount <= MAX_VALUE) {
						duePayments.add(new PaymentAmount(duePaymentAmount));
					}
				}
				lineIndex++;
			}
		} catch (IOException e) {
			throw new PaymentException("Can't read input file: " + inputFile, e);
		} catch (NumberFormatException e) {
			throw new PaymentException("Can't parse correctly amounts in file", e);
		} finally {
			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (IOException e) {
					throw new PaymentException("Can't close input file: " + inputFile, e);
				}
			}
		}
	}
	
	private List<PaymentAmount> findPayment() throws PaymentException {
		return calculator.findPayments(bankTransfer, duePayments);
	}
	
	private void writeOutputFile(List<PaymentAmount> foundPayments) throws PaymentException {
		BufferedWriter outputWriter = null;
        try {
        	outputWriter = new BufferedWriter(new FileWriter(outputFile));
        	
        	// Empty result (no solution).
        	int nbPayments = foundPayments != null ? foundPayments.size() : 0;
        	if (nbPayments== 0) {
        		// Empty result (no solution).
        		outputWriter.write("NO SOLUTION");
        	} else {
            	// Iterate through the results.
             	for (int indexPayment=0; indexPayment < nbPayments; indexPayment++) {
            		outputWriter.write(foundPayments.get(indexPayment).toString());
            		outputWriter.newLine();
            	}
        	}
        } catch (IOException e) {
        	throw new PaymentException("Can't write into output file: " + outputFile, e);
        } finally {
            try {
                // Close the writer regardless of what happens.
            	outputWriter.close();
            } catch (Exception e) {
            	throw new PaymentException("Can't close output file: " + outputFile, e);
            }
        }		
	}
	
	private boolean checkResult(List<PaymentAmount> results) {
		PaymentAmount sum = new PaymentAmount(0);
		for (int resultIndex = 0; resultIndex < results.size(); resultIndex++) {
			PaymentAmount paymentAmount = results.get(resultIndex);
			sum.addAmount(paymentAmount);
		}
		return bankTransfer.equals(sum);
	}
}
