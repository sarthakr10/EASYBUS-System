package com.controller;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.ContactUs;
import com.response.Booking;
import com.services.EmailService;

@RestController
@CrossOrigin("*")

public class ContactController {
	
    @Autowired
    private EmailService emailService;

//    @PostMapping("/contact")
//    public void contact(@RequestBody ContactUs form) {
//        String subject = "New Contact Us Message from " + form.getName();
//        String text = "Name: " + form.getName() + "\nEmail: " + form.getEmail() + "\nMessage: " + form.getMessage();
//        emailService.sendEmail(form.getEmail(), subject, text);
//    }
    
    @PostMapping("/contact")
    public void contact(@RequestBody ContactUs form) {
    	System.err.println("here");
        String to = "deekshit2602@gmail.com";  // Replace with your email
       String subject = form.getSubject();
        String text = "Name: " + form.getName() + "\nMailSentFrom: " + form.getEmail() + "\nMessage: " + form.getMessage();
        emailService.sendEmail(to, subject, text);
       // return "Message sent successfully";
    }
    
 
    
    
    
   @PostMapping("/bookingConfirmation")
    public void bookingMail(@RequestBody  Booking booking) {
   	
       String to = booking.getBookingEmail();  // Replace with your email
       String subject = "Booking Confirmation";
       String text = String.format(
               "Hello %s,\n\n" +
                       "Thank you for your booking. Here are your booking details:\n\n" +
                        "Booking ID: %d\n" +
                       "Number of Bookings: %d\n" +
                       "Bus ID: %d\n" +
                       "Sitting Seats: %d\n" +
                       "Sleeper Seats: %d\n" +
                       "Total Cost: %.2f\n\n" +
                       "We look forward to serving you.\n\n" +
                       "Best regards,\n" +
                       "Thanks for using Deekshit Bus Services",
                       booking.getBookingEmail(),                  
                       booking.getBookingId(),
                      booking.getNoOfBookings(),
                      booking.getBusId(),
                       booking.getTypeSittingCapacity(),
                        booking.getTypeSleeperCapacity(),  
                        booking.getTotalBookingCost()
                );
        emailService.sendEmail(to, subject, text);
      //return "Message sent successfully";
   }
   
    
    
}
