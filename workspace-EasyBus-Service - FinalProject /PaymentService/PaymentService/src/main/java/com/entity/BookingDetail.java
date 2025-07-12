package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetail {
	 private Integer bookingId;
	  private String orderStatus;
	    private Integer orderAmount;
}
