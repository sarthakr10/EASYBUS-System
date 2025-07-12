package com.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.entity.TransactionDetails;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class OrderDetailService {

	  private static final String ORDER_PLACED = "Placed";

	    private static final String KEY = "rzp_test_gdeQJmHzRyHpZm";
	    private static final String KEY_SECRET = "Ma3Rb4RaL0hoOlzVCPm2bAGD";
	    private static final String CURRENCY = "INR";
	
	    
	    public TransactionDetails createTransaction(Double amount) {
	        try {

	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("amount", (amount) );
	            jsonObject.put("currency", CURRENCY);

	            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);

	            Order order = razorpayClient.orders.create(jsonObject);
	          System.err.println(order);

	            TransactionDetails transactionDetails =  prepareTransactionDetails(order);
	            System.err.println(transactionDetails);
	            return transactionDetails;
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	        return null;
	    }
	    
	
	 private TransactionDetails prepareTransactionDetails(Order order) {
	        String paymentId = order.get("id");
	        String currency = order.get("currency");
	        Integer amount = (order.get("amount"));

	        TransactionDetails transactionDetails = new TransactionDetails(paymentId, currency, amount, KEY);
	        return transactionDetails;
	    }
}
