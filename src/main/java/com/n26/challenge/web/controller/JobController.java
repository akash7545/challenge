package com.n26.challenge.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.service.TransactionService;
import com.n26.challenge.util.Validator;

import lombok.extern.log4j.Log4j;

/**
 * 
 * @author akash.shinde
 *
 */

@RestController
@Log4j
public class JobController {

	@Autowired
	private TransactionService transactionService;
	
	/**
	 * Controller method to make transaction
	 * @param transaction
	 * @return ResopnseEntity with Status 201 if transaction is successful, otherwise 500
	 */
	@RequestMapping(value="/transactions", method={ RequestMethod.POST }, consumes = "application/json")
	public ResponseEntity<String> makeTransaction(@Validated final @RequestBody(required=true) Transaction transaction) {
		try {
			if (!Validator.validateRequest(transaction))
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			log.info("Adding transaction in controller");
			transactionService.makeTransaction(transaction);
			
			return new ResponseEntity<String>(HttpStatus.CREATED);
			
		} catch(Exception e) {
			log.error("Error while making transaction: " + e.getMessage());
			return new ResponseEntity<String>("Error while making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller method to get statistics
	 * @return ResopnseEntity with Statistics object in JSon format of Status 200 if successful, otherwise 500
	 */
	@RequestMapping(value="/statistics", method={ RequestMethod.GET }, produces="application/json")
	public ResponseEntity<Object> changeWorkers() {
		try {
			
			log.info("Getting statistics in controller");
			Statistics stats = transactionService.getStatistics();
			
			return new ResponseEntity<Object>(stats, HttpStatus.OK);
			
		} catch(Exception e) {
			log.error("Error while getting statistics: " + e.getMessage());
			return new ResponseEntity<Object>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
