package com.n26.challenge.util;

import com.n26.challenge.model.Transaction;

/**
 * 
 * @author akash.shinde
 *
 */
public class Validator {

	/**
	 * 
	 * @param transaction
	 * @return boolean true if transaction is valid else false
	 */
	public static boolean validateRequest(Transaction transaction) {
		if ((transaction.getAmount() == null) || (transaction.getTimestamp() == null)) 
			return false;
		return true;
	}
}
