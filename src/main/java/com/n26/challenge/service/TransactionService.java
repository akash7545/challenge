package com.n26.challenge.service;

import org.springframework.stereotype.Service;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;

/**
 * 
 * @author akash.shinde
 *
 */
@Service
public interface TransactionService {

	public void makeTransaction(Transaction transaction);
	
	public Statistics getStatistics();
}
