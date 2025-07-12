package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDetails {
	  private String paymentId;
	    private String currency;
	    private Integer amount;
	    private String key;
}
