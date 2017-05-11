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

@JsonPropertyOrder({"sum", "avg", "max", "min", "count"})
public class Statistics implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter @Setter
	private Double sum;
	
	@Getter @Setter
	private Double avg;
	
	@Getter @Setter
	private Double max;
	
	@Getter @Setter
	private Double min;
	
	@Getter @Setter
	private Long count;
}

