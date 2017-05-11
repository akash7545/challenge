package com.n26.challenge.service;

import java.util.LinkedList;
import java.util.SortedMap;

import org.springframework.stereotype.Repository;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.util.DataStorage;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

/**
 * 
 * @author akash.shinde
 *
 */
@Repository
@Log4j
public class TransactionServiceImpl implements TransactionService {

	/**
	 * Method to make transaction
	 * @param Transaction object
	 * @return nothing
	 */
	@Override
	@Synchronized
	public void makeTransaction(Transaction transaction) {
		
		//Limiting size to 60 (seconds here)
		DataStorage.checkAndUpdateDataMap();

		SortedMap<Long, LinkedList<Transaction>> dataMap = DataStorage.dataMap;
		
		if (dataMap.containsKey(transaction.getTimestamp()) || (dataMap.get(transaction.getTimestamp())!=null)) {
			log.info("Transaction entry present for timestamp: "+ transaction.getTimestamp()+", adding object in timestamp bucket");
			dataMap.get(transaction.getTimestamp()).add(transaction);
		} else {
			LinkedList<Transaction> newList = new LinkedList<>();
			newList.add(transaction);
			dataMap.put(transaction.getTimestamp(), newList);
			log.info("Added transaction for timestamp: " + transaction.getTimestamp()+" and creating new bucket");
		}
	}

	/**
	 * Method to get latest statistics
	 * @param 
	 * @return Statistic object
	 */
	@Override
	@Synchronized
	public Statistics getStatistics() {
		return DataStorage.statistics;
	}
}
