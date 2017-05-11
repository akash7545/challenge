package com.n26.challenge.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author akash.shinde
 *
 */
@JsonPropertyOrder({"amount", "timestamp"})
public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private Double amount;
	
	@Getter @Setter
	private Long timestamp;

}
