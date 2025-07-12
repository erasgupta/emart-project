package com.portal.emart.api.service;

import com.portal.emart.api.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String to, String customerName, Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String formattedDate = order.getDeliveryDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

            String htmlContent = String.format(
            	    """
            	    <div style="font-family:'Segoe UI', sans-serif; max-width:600px; margin:0 auto; border:1px solid #ddd; border-radius:10px; overflow:hidden;">
            	      <!-- Top Section (30–40%%) -->
            	      <div style="background-color:#1c1c1c; color:#ffffff; padding:30px;">
            	        <h2 style="color:#ffcc70; margin-top:0;">Hi %s,</h2>
            	        <p style="font-size:16px; line-height:1.6; margin-bottom:0;">
            	          Thank you for your order with <strong style="color:#ffae42;">Baked Cravings</strong>!<br/>
            	          We're thrilled to serve you something sweet.
            	        </p>
            	      </div>

            	      <!-- Main White Section -->
            	      <div style="background-color:#ffffff; color:#1c1c1c; padding:30px;">
            	        <h3 style="color:#d35400; margin-top:0;">🧾 Your Request Summary</h3>
            	        <table style="width:100%%; font-size:15px; margin-top:15px;">
            	          <tr><td style="padding:6px;"><strong>Cake Type:</strong></td><td>%s</td></tr>
            	          <tr><td style="padding:6px;"><strong>Flavor:</strong></td><td>%s</td></tr>
            	          <tr><td style="padding:6px;"><strong>Delivery Date:</strong></td><td>%s</td></tr>
            	          <tr><td style="padding:6px;"><strong>Message:</strong></td><td>%s</td></tr>
            	        </table>

            	        <p style="margin-top:20px; font-size:15px; line-height:1.5;">
            	          We handcraft our cakes with <strong style="color:#ffae42;">natural ingredients</strong> to ensure every bite
            	          is both healthy and indulgent. ✨
            	        </p>

            	        <div style="margin-top:30px;">
            	          <a href="https://www.instagram.com/_bakedcravings_?igsh=MW50ZHF3aHR6dXBzdw=="
            	             style="background-color:#ffcc70; color:#1c1c1c; padding:12px 20px;
            	             text-decoration:none; border-radius:5px; font-weight:bold; font-size:16px;">
            	            🍽️ Checkout Our Designs!
            	          </a>
            	        </div>

            	        <p style="margin-top:40px; font-size:14px; color:#666;">
            	          Sweet regards,<br/>
            	          <strong style="color:#d35400;">Team Baked Cravings 💖</strong>
            	        </p>
            	      </div>
            	    </div>
            	    """,
            	    customerName,
            	    order.getCakeType(),
            	    order.getFlavor(),
            	    formattedDate,
            	    order.getMessage() != null ? order.getMessage() : "None"
            	);



            helper.setTo(to);
            helper.setSubject("Your Baked Cravings Cake Request Confirmation 🎂");
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
