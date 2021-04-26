package com.se2.hanuairline.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.Mail;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.service.user.UserService;
@Service
public class EmailService {
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	private UserService userService;
	private void sendSimpleMessage(Mail mail, String templateName) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		
		Context context = new Context();
		context.setVariables(mail.getModel());
		String html = templateEngine.process(templateName, context);
		System.out.println(mail.toString());
		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		
		emailSender.send(message);
		
	}
	
	private Mail createMail(String to, Map<String, Object> model, String title) {
		Mail mail = new Mail();
		mail.setFrom("hanuairline@gmail.com");
		mail.setTo(to);
		mail.setSubject(title);
		mail.setModel(model);
		return mail;
	}
	public void updateTimeThenSendEmail(Flight flight) throws MessagingException {
		Set<Ticket> tickets = flight.getTicket();
		Vector<User> users = new Vector<>(); 
		for(Ticket ticket:tickets) {
			users.add(ticket.getUser());
		}
		for(User user:users) {
			Map<String, Object> model = new HashMap<>();
			model.put("name", user.getUsername());
			model.put("aircraft", flight.getAircraft().getName());
			model.put("departureAirport", flight.getAirway().getDepartureAirport().getName());
			model.put("arrivalAirport", flight.getAirway().getArrivalAirport().getName());
			model.put("departureTime", flight.getDepartureTime());
			model.put("arrivalTime", flight.getArrivalTime());
			Mail mail = createMail(user.getEmail(), model, "Announce: Update time flight!!!");
			sendSimpleMessage(mail,"email-template.html");
		}
	}
	public void verifyTicketByEmail(Ticket ticket) throws MessagingException {
		Map<String, Object> model = new HashMap<>();
		model.put("aircraft", ticket.getFlight().getAircraft().getName());
		model.put("departureAirport", ticket.getFlight().getAirway().getDepartureAirport().getName() );
		model.put("arrivalAirport", ticket.getFlight().getAirway().getArrivalAirport().getName() );
		model.put("arrivalGate", ticket.getFlight().getArrivalGate().getName());
		model.put("departureGate", ticket.getFlight().getDepartureGate().getName());
		model.put("seat", ticket.getAircraftSeat().getId());
		model.put("ticketType", ticket.getType());
		model.put("departureTime", ticket.getFlight().getDepartureTime());
		model.put("arrivalTime", ticket.getFlight().getArrivalTime());
		model.put("userName", ticket.getUser().getName());
		model.put("ticketId", ticket.getId());
		
		Mail mail = createMail(ticket.getUser().getEmail(), model, "HanuAirLine: E-Ticket");
		sendSimpleMessage(mail, "test.html");
	}
}