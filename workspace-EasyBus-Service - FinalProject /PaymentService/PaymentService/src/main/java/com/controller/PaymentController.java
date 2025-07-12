package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.entity.PaymentModel;
import com.entity.TransactionDetails;
import com.service.OrderDetailService;

@RestController
@CrossOrigin("*")
public class PaymentController {
	
	@Autowired
	OrderDetailService orderDetailService;
	
	 @PostMapping({"/createTransaction"})
	    public TransactionDetails createTransaction(@RequestBody PaymentModel amount) {
		
	        return orderDetailService.createTransaction(amount.getAmount());	    }

}
