package com.servicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.services.EmailService;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail_shouldSendMailSuccessfully() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "This is a test message.";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assert sentMessage.getTo() != null;
       assert sentMessage.getTo()[0].equals(to);
        assert sentMessage.getSubject().equals(subject);
        assert sentMessage.getText().equals(body);
    }
}