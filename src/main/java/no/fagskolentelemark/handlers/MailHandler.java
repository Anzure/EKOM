package no.fagskolentelemark.handlers;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import no.fagskolentelemark.EkomMain;
import no.fagskolentelemark.wrapper.Credentials;

import javax.mail.PasswordAuthentication;

public class MailHandler {

	public static void sendMail(String email, String username, String password) {
		try {
			// Connection information
			Properties props =  new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.host", "smtp.domeneshop.no");
			props.put("mail.smtp.auth", "true");

			// Active session
			final Credentials internalCredentials = new Credentials();
			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(internalCredentials.getUsername(), internalCredentials.getPassword());
				}
			});
			session.setDebug(false);

			// Variables
			InternetAddress fromAddress = new InternetAddress(internalCredentials.getEmail(), internalCredentials.getName());
			InternetAddress toAddress = new InternetAddress(email);
			String msgSubject = "Velkommen til " + EkomMain.mainDir.getName();

			// Message
			Message msg = new MimeMessage(session);
			msg.setFrom(fromAddress);
			msg.addRecipient(Message.RecipientType.TO,toAddress);
//			msg.addRecipient(Message.RecipientType.CC, new InternetAddress("andre.mathisen@outlook.com"));
			msg.setSubject(msgSubject);

			Multipart multipart = new MimeMultipart();

			// Text
			MimeBodyPart textBodyPart = new MimeBodyPart();

			StringBuilder sb = new StringBuilder("Hei<br/><br/>");
			sb.append("<b>Canvas</b><br/>"
					+ "Innlogging: <a href=\"https://fagskolentelemark.instructure.com/login/canvas\">fagskolentelemark.instructure.com/login/canvas</a><br/>"
					+ "Login: " + email + "<br/>"
					+ "Passord: " + password + "<br/><br/>");

			if (EkomMain.fbLink != null && EkomMain.fbLink.length() > 2) {
				sb.append("<b>Facebook</b><br/>"
						+ "Meld deg inn i Facebook grouppen her: <a href=\"" + EkomMain.fbLink + "\">" + EkomMain.fbLink + "</a><br/><br/><br/>");
			}

			if (EkomMain.roomLink != null && EkomMain.roomLink.length() > 2) {
				sb.append("<b>Web klasserommet</b><br/>"
						+ "Kobling: <a href=\"" + EkomMain.roomLink + "\">" + EkomMain.roomLink + "</a><br/>"
						+ "(logg inn som gjest med ditt navn)<br/><br/>");
			}

			// Without ODIT AS
			sb.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td><h3 color=\"#000000\" class=\"sc-fBuWsC eeihxG\" style=\"margin: 0px; font-size: 16px; color: rgb(0, 0, 0);\"><span>Andre</span><span>&nbsp;</span><span>Mathisen</span></h3><p color=\"#000000\" font-size=\"small\" class=\"sc-fMiknA bxZCMx\" style=\"margin: 0px; color: rgb(0, 0, 0); font-size: 12px; line-height: 20px;\"><span>Konsulent</span></p></td><td width=\"15\"><div></div></td><td color=\"#00960a\" direction=\"vertical\" width=\"1\" class=\"sc-jhAzac hmXDXQ\" style=\"width: 1px; border-bottom: none; border-left: 1px solid rgb(0, 150, 10);\"></td><td width=\"15\"><div></div></td><td><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr height=\"25\" style=\"vertical-align: middle;\"><td width=\"30\" style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: bottom;\"><span color=\"#00960a\" width=\"11\" class=\"sc-jlyJG bbyJzT\" style=\"display: block; background-color: rgb(0, 150, 10);\"><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/phone-icon-2x.png\" color=\"#00960a\" width=\"13\" class=\"sc-iRbamj blSEcj\" style=\"display: block; background-color: rgb(0, 150, 10);\"></span></td></tr></tbody></table></td><td style=\"padding: 0px; color: rgb(0, 0, 0);\"><a href=\"tel:+47 456 60 785\" color=\"#000000\" class=\"sc-gipzik iyhjGb\" style=\"text-decoration: none; color: rgb(0, 0, 0); font-size: 12px;\"><span>+47 456 60 785</span></a></td></tr><tr height=\"25\" style=\"vertical-align: middle;\"><td width=\"30\" style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: bottom;\"><span color=\"#00960a\" width=\"11\" class=\"sc-jlyJG bbyJzT\" style=\"display: block; background-color: rgb(0, 150, 10);\"><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/email-icon-2x.png\" color=\"#00960a\" width=\"13\" class=\"sc-iRbamj blSEcj\" style=\"display: block; background-color: rgb(0, 150, 10);\"></span></td></tr></tbody></table></td><td style=\"padding: 0px;\"><a href=\"mailto:andre.mathisen@odit.no\" color=\"#000000\" class=\"sc-gipzik iyhjGb\" style=\"text-decoration: none; color: rgb(0, 0, 0); font-size: 12px;\"><span>andre.mathisen@odit.no</span></a></td></tr><tr height=\"25\" style=\"vertical-align: middle;\"><td width=\"30\" style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: bottom;\"><span color=\"#00960a\" width=\"11\" class=\"sc-jlyJG bbyJzT\" style=\"display: block; background-color: rgb(0, 150, 10);\"><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/link-icon-2x.png\" color=\"#00960a\" width=\"13\" class=\"sc-iRbamj blSEcj\" style=\"display: block; background-color: rgb(0, 150, 10);\"></span></td></tr></tbody></table></td><td style=\"padding: 0px;\"><a href=\"//www.odit.no\" color=\"#000000\" class=\"sc-gipzik iyhjGb\" style=\"text-decoration: none; color: rgb(0, 0, 0); font-size: 12px;\"><span>www.odit.no</span></a></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table>");

			// With ODIT AS
			//			sb.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td><h3 color=\"#000000\" class=\"sc-fBuWsC eeihxG\" style=\"margin: 0px; font-size: 16px; color: rgb(0, 0, 0);\"><span>Andre</span><span>&nbsp;</span><span>Mathisen</span></h3><p color=\"#000000\" font-size=\"small\" class=\"sc-fMiknA bxZCMx\" style=\"margin: 0px; color: rgb(0, 0, 0); font-size: 12px; line-height: 20px;\"><span>Konsulent</span></p><p color=\"#000000\" font-size=\"small\" class=\"sc-dVhcbM fghLuF\" style=\"margin: 0px; font-weight: 500; color: rgb(0, 0, 0); font-size: 12px; line-height: 20px;\"><span>ODIT AS</span></p></td><td width=\"15\"><div></div></td><td color=\"#00960a\" direction=\"vertical\" width=\"1\" class=\"sc-jhAzac hmXDXQ\" style=\"width: 1px; border-bottom: none; border-left: 1px solid rgb(0, 150, 10);\"></td><td width=\"15\"><div></div></td><td><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr height=\"25\" style=\"vertical-align: middle;\"><td width=\"30\" style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: bottom;\"><span color=\"#00960a\" width=\"11\" class=\"sc-jlyJG bbyJzT\" style=\"display: block; background-color: rgb(0, 150, 10);\"><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/phone-icon-2x.png\" color=\"#00960a\" width=\"13\" class=\"sc-iRbamj blSEcj\" style=\"display: block; background-color: rgb(0, 150, 10);\"></span></td></tr></tbody></table></td><td style=\"padding: 0px; color: rgb(0, 0, 0);\"><a href=\"tel:+47 456 60 785\" color=\"#000000\" class=\"sc-gipzik iyhjGb\" style=\"text-decoration: none; color: rgb(0, 0, 0); font-size: 12px;\"><span>+47 456 60 785</span></a></td></tr><tr height=\"25\" style=\"vertical-align: middle;\"><td width=\"30\" style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: bottom;\"><span color=\"#00960a\" width=\"11\" class=\"sc-jlyJG bbyJzT\" style=\"display: block; background-color: rgb(0, 150, 10);\"><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/email-icon-2x.png\" color=\"#00960a\" width=\"13\" class=\"sc-iRbamj blSEcj\" style=\"display: block; background-color: rgb(0, 150, 10);\"></span></td></tr></tbody></table></td><td style=\"padding: 0px;\"><a href=\"mailto:andre.mathisen@odit.no\" color=\"#000000\" class=\"sc-gipzik iyhjGb\" style=\"text-decoration: none; color: rgb(0, 0, 0); font-size: 12px;\"><span>andre.mathisen@odit.no</span></a></td></tr><tr height=\"25\" style=\"vertical-align: middle;\"><td width=\"30\" style=\"vertical-align: middle;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"sc-gPEVay eQYmiW\" style=\"vertical-align: -webkit-baseline-middle; font-size: small; font-family: Arial;\"><tbody><tr><td style=\"vertical-align: bottom;\"><span color=\"#00960a\" width=\"11\" class=\"sc-jlyJG bbyJzT\" style=\"display: block; background-color: rgb(0, 150, 10);\"><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/link-icon-2x.png\" color=\"#00960a\" width=\"13\" class=\"sc-iRbamj blSEcj\" style=\"display: block; background-color: rgb(0, 150, 10);\"></span></td></tr></tbody></table></td><td style=\"padding: 0px;\"><a href=\"//www.odit.no\" color=\"#000000\" class=\"sc-gipzik iyhjGb\" style=\"text-decoration: none; color: rgb(0, 0, 0); font-size: 12px;\"><span>www.odit.no</span></a></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table>");

			String msgBody = sb.toString();
			textBodyPart.setContent(msgBody, "text/html; charset=UTF-8");

			// Attachment
			if (EkomMain.sendPDF) {
				MimeBodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(EkomMain.mainDir.getAbsolutePath() + File.separator + "_Introduksjonsbrev.pdf");
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				attachmentBodyPart.setFileName("Introduksjonsbrev.pdf");
				multipart.addBodyPart(attachmentBodyPart);
			}

			msg.setContent(multipart);
			multipart.addBodyPart(textBodyPart);

			// Send email
			Transport.send(msg);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}