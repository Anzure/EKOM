package no.fagskolentelemark.utils;

import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import no.fagskolentelemark.GitIgnored;

public class MainUtil {

	public static String getDate() {
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat("MMMM d (HH-mm-ss)").format(cal.getTime());
	}

	public static String generatePassword() {
		String[] wordList1 = {"Oste", "Velge", "Vaske", "Trene", "Telle", "Sykle", "Studere", "Stemme", "Snike", "Smile", "Skrive", "Knekke", "Danse", "Drikke", "Gul", "Lilla", "Svart", "Hvit"}; 
		String[] wordList2 = {"kake", "banan", "bil", "traktor", "fjell", "hytte", "eple", "taco", "pizza", "blyant", "fisk", "egg", "biff", "ball", "melk", "ost", "sko", "stol", "bord", "flaske"};

		String word1 = wordList1[new Random().nextInt(wordList1.length-1)];
		String word2 = wordList2[new Random().nextInt(wordList2.length-1)];
		int num = new Random().nextInt(90)+10;

		String password = word1 + word2 + num;
		return password;
	}

	public static int sendSMS(int phone, String txt) throws Exception {
		URL url = new URL("https://gatewayapi.com/rest/mtsms");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(
				"token=" + GitIgnored.sms_token
				+ "&sender=" + URLEncoder.encode("Fagskolen", "UTF-8")
				+ "&message=" + URLEncoder.encode(txt, "UTF-8")
				+ "&class=premium&priority=VERY_URGENT&recipients.0.msisdn=0047" + phone
				);
		wr.close();

		int responseCode = con.getResponseCode();
		return responseCode;
	}
}