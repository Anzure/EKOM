package no.fagskolentelemark.handlers;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;

import java.io.File;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import no.fagskolentelemark.EkomMain;
import no.fagskolentelemark.GitIgnored;

import javax.mail.PasswordAuthentication;

public class MailHandler {

	public static void sendMail(String email, String username, String password) {
		try {
			// Connection information
			Properties props =  new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.host", "m.outlook.com");
			props.put("mail.smtp.auth", "true");

			// Active session
			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(GitIgnored.credentials[0], GitIgnored.credentials[1]);
				}
			});
			session.setDebug(false);

			// Variables
			InternetAddress fromAddress = new InternetAddress(GitIgnored.credentials[0],
					GitIgnored.credentials[2]);
			InternetAddress toAddress = new InternetAddress(email);
			String msgSubject = "Velkommen til " + EkomMain.mainDir.getName();

			// Message
			Message msg = new MimeMessage(session);
			msg.setFrom(fromAddress);
			msg.addRecipient(Message.RecipientType.TO,toAddress);
			msg.addRecipient(Message.RecipientType.CC, new InternetAddress(
					GitIgnored.credentials[0]));
			msg.setSubject(msgSubject);

			Multipart multipart = new MimeMultipart();

			// Text
			MimeBodyPart textBodyPart = new MimeBodyPart();
			String msgBody = "Hei<br/><br/>"
					+ "<b>Fronter</b><br/>"
					+ "Innlogging: <a href=\"https://fronter.com/telemarkvgs/\">https://fronter.com/telemarkvgs/</a><br/>"
					+ "Brukernavn: " + username + "<br/>"
					+ "Passord: " + password + "<br/><br/>"
					+ "<b>Facebook</b><br/>"
					+ "Meld deg inn i Facebook grouppen her: <a href=\"" + EkomMain.fbLink + "\">" + EkomMain.fbLink + "</a><br/><br/>"
					+ "<b>Web klasserommet</b><br/>"
					+ "Kobling: <a href=\"https://tfk.adobeconnect.com/ekom18/\">https://tfk.adobeconnect.com/ekom18/</a>";
			textBodyPart.setContent(msgBody, "text/html");

			// Attachment
			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(EkomMain.mainDir.getAbsolutePath() + File.separator + "_Introduksjonsbrev EKOM.pdf");
			attachmentBodyPart.setDataHandler(new DataHandler(source));
			attachmentBodyPart.setFileName("Introduksjonsbrev EKOM " + EkomMain.ekomNum + ".pdf");
			multipart.addBodyPart(attachmentBodyPart);

			msg.setContent(multipart);
			multipart.addBodyPart(textBodyPart);

			// Send email
			Transport.send(msg);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}