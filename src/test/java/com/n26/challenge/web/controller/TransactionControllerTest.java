package com.n26.challenge.web.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.spring.App;
import com.n26.challenge.util.DataStorage;

/**
 * 
 * @author akash.shinde
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = App.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TransactionControllerTest {

	//Declaring api endpoints
	protected final String transactionUrl = "/transactions";
	protected final String statisticsUrl = "/statistics";
	
	@Autowired
	protected WebApplicationContext wac;
	
	protected MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build(); //Initializing mocked web context
	}

	/**
	 * Test to check valid transaction
	 * @throws Exception
	 */
	@Test
	public void validTransactionTest() throws Exception {
		
		String json = "{\"amount\":13,\"timestamp\":1494438465}";
		getResponseResultForPost(transactionUrl, json).andExpect(status().isCreated());
	}
	
	/**
	 * Test to check invalid transaction - without amount
	 * @throws Exception
	 */
	@Test
	public void noTransactionAmountTest() throws Exception {
		
		String json = "{\"timestamp\":1494438465}";
		getResponseResultForPost(transactionUrl, json).andExpect(status().isBadRequest());
	}
	
	/**
	 * Test to check invalid transaction - without timestamp
	 * @throws Exception
	 */
	@Test
	public void noTransactionTimestampTest() throws Exception {
		
		String json = "{\"amount\":13}";
		getResponseResultForPost(transactionUrl, json).andExpect(status().isBadRequest());
	}
	
	/**
	 * Test to check valid statistics 
	 * @throws Exception
	 */
	@Test
	public void validStatisticsTest() throws Exception{
		
		TimeZone.setDefault(TimeZone.getDefault());
		Long currentTime = new Date().getTime() / 1000;
		
		String input1 = "{\"amount\":13,\"timestamp\":" + (currentTime + 1) +"}";
		getResponseResultForPost(transactionUrl, input1);
		
		String input2 = "{\"amount\":13,\"timestamp\":" + (currentTime + 3) +"}";
		getResponseResultForPost(transactionUrl, input2);

		Thread.sleep(4000);
		
		String expectedStats = "{\"sum\":26.0,\"avg\":13.0,\"max\":13.0,\"min\":13.0,\"count\":2}";
		String response = getResponseResultForGet(statisticsUrl, expectedStats);
		
		assertTrue(expectedStats.equals(response));
	}
	
	/**
	 * Test to check invalid statistics
	 * @throws Exception
	 */
	@Test
	public void invalidStatisticsTest() throws Exception{
		
		//Setting default timestamp to get current system timestamp
		TimeZone.setDefault(TimeZone.getDefault());
		Long currentTime = new Date().getTime() / 1000;
		
		//Adding first test transaction
		String input1 = "{\"amount\":13,\"timestamp\":"+currentTime+2+"}";
		getResponseResultForPost(transactionUrl, input1);
		
		//Adding second test transaction
		String input2 = "{\"amount\":13,\"timestamp\":"+(currentTime+3)+"}";
		getResponseResultForPost(transactionUrl, input2);

		//Pausing thread so that we can have all valid transactions
		Thread.sleep(4000);
		String expectedStats = "{\"sum\":20.0,\"avg\":45.0,\"max\":45.0,\"min\":0.0,\"count\":2}";
		String response = getResponseResultForGet(statisticsUrl, expectedStats);
		
		//Comparing results
		assertFalse(expectedStats.equals(response));
	}
	
	/**
	 * Clearing resources
	 */
	@After
	public void tearDown() {
		DataStorage.dataMap.clear();
		DataStorage.statistics = new Statistics();
	}

	/**
	 * 
	 * @param testUrl
	 * @param json
	 * @return ResultActions item containing response of POST request for api called 
	 * @throws Exception
	 */
	protected ResultActions getResponseResultForPost(String testUrl, String json) throws Exception {
		return this.mockMvc.perform(post(testUrl).content(json).contentType(MediaType.APPLICATION_JSON)
				.accept("application/json"));
	}
	
	/**
	 * 
	 * @param testUrl
	 * @param json
	 * @return ResultActions item containing response of GET request for api called 
	 * @throws Exception
	 */
	protected String getResponseResultForGet(String testUrl, String json) throws Exception {
		return this.mockMvc.perform(get(testUrl).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}
}
