package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactUs {
    private String name;
    private String email;
    private String message;
    private String subject;

    // getters and setters
}