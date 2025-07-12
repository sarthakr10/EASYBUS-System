package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInput {
	 private String fullName;
	    private String fullAddress;
	    private String contactNumber;
	    private String alternateContactNumber;
	    private String transactionId;
}
