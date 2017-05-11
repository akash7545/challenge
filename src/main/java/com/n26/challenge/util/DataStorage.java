package com.n26.challenge.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

/**
 * 
 * @author akash.shinde
 *
 */
@Component
@Log4j
public class DataStorage {

	public static SortedMap<Long, LinkedList<Transaction>> dataMap = Collections.synchronizedSortedMap(new TreeMap<Long, LinkedList<Transaction>>());
	
	public static Statistics statistics = new Statistics(); 

	@Synchronized
	public static void checkAndUpdateDataMap() {
		
		//Limiting size to 60 (seconds here)
		if (dataMap.size() > 60) {
			
			log.info("Deleting key: " + dataMap.firstKey());
			dataMap.remove(dataMap.firstKey());
		}
	}
}
