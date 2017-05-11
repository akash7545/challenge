package com.n26.challenge.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TimeZone;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.util.Constants;
import com.n26.challenge.util.DataStorage;

import lombok.extern.log4j.Log4j;

/**
 * 
 * @author akash.shinde
 *
 */
@Component
@Log4j
public class StatisticsScheduler {

	//Initialize variables for statistics calculations
	private static Double sum = 0d;
	private static Double avg = 0d;
	private static Double max = 0d;
	private static Double min = 0d;
	private static Long count = 0L;

	/**
	 * Scheduled method to fill up empty transaction & form statistics objects every second.
	 */
	@Scheduled(fixedDelay = 1000)
	public void calculateStats() {

		//Resetting variables for every second's statistics calculation
		avg = 0d;
		sum = 0d;
		max = 0d;
		min = 0d;
		count = 0L;
		
		//Setting default time zone to overcome issue of timestamp mismatch
		TimeZone.setDefault(TimeZone.getDefault());
		Long currentTime = new Date().getTime() / 1000;
		
		//Getting synchronized sorted map as a storage
		SortedMap<Long, LinkedList<Transaction>> dataMap = DataStorage.dataMap;

		//Limiting map size to 60
		DataStorage.checkAndUpdateDataMap();
		
		//Checking if current second's timestamp is empty. If so then add empty value else skip
		if (!dataMap.containsKey(currentTime)) {
			log.info("Adding missing timestamp: " + currentTime);
			dataMap.put(currentTime, new LinkedList<Transaction>());
		}

		//Calculating statistic variables based on transaction for last 60 seconds
		DataStorage.dataMap.forEach((k,v) -> {
			
			if ((v != null) && (v.size() > 0)) {
				if (count == 0) {
					min = v.get(0).getAmount(); //Setting first amount in list as min
				}
				count += v.size();
				
				v.forEach(transaction -> {
					sum += transaction.getAmount();
					if (transaction.getAmount() > max) max = transaction.getAmount();
					if (transaction.getAmount() < min) min = transaction.getAmount();
				});
			}
		});

		//Formatting values to 2 decimal places & putting in Statistics object
		avg = (count == 0) ? 0 : (sum / count);
		Statistics statistics = DataStorage.statistics;
		statistics.setSum((sum == 0) ? 0 : Double.parseDouble(Constants.numberFormat.format(sum)));
		statistics.setAvg((avg == 0) ? 0 : Double.parseDouble(Constants.numberFormat.format(avg)));
		statistics.setMax((max == 0) ? 0 : Double.parseDouble(Constants.numberFormat.format(max)));
		statistics.setMin((min == 0) ? 0 : Double.parseDouble(Constants.numberFormat.format(min)));
		statistics.setCount(count);

		log.info("Setting statistics for timestamp: " + currentTime);
		
	}
}
