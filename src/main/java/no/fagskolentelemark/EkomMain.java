package no.fagskolentelemark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import no.fagskolentelemark.handlers.ConfigHandler;
import no.fagskolentelemark.handlers.HistoryHandler;
import no.fagskolentelemark.handlers.MailHandler;
import no.fagskolentelemark.handlers.SheetHandler;
import no.fagskolentelemark.objects.Student;
import no.fagskolentelemark.utils.MainUtil;

public class EkomMain {
	
	public static File mainDir = new File("C:\\Users\\andre\\Dropbox (Fagskolen Telemark)\\Jobb\\Fronter\\Ekom 6-18");
	public static String fbLink;
	public static String ekomName;
	public static String ekomNum;
	public static String gSheetId;
	
	public static List<Student> newStudents = new ArrayList<Student>();

	public static void main(String[] args) {
		try {
			// Load settings
			ConfigHandler.loadConfig();
			ekomNum = (String) ConfigHandler.get("name");
			ekomName = "Ekom " + ekomNum;
			fbLink = (String) ConfigHandler.get("facebookLink");
			gSheetId = (String) ConfigHandler.get("googleSheetId");
			
			// Fail safe
			if (!ekomNum.endsWith(ekomNum) || !ekomName.equals(mainDir.getName())) {
				System.out.println("Security check failed, please check configuration file.");
				System.exit(0);
				return;
			}
			
			// Load history
			System.out.println("Loading already imported students...");
			HistoryHandler.loadHistory();
			
			// Process Google spreadsheet
			System.out.println("Processing external spreadsheet...");
			SheetHandler.processSheet();
			
			// Send emails
			System.out.println("Sending email...");
			for (Student student : newStudents) {
				MailHandler.sendMail(student.getEmail(), student.getUsername(), student.getPassword());
				System.out.println("Email sent to " + student.getEmail());
			}
			
			// Send SMS
			for (Student student : newStudents) {
				String txt = "EKOM " + ekomNum + "\nDu har blitt tilsendt en e-post med brukernavn og passord.\nVennligst ta kontakt med IT om du ikke fikk den.\nMvh Fagskolen Telemark";
				int result = MainUtil.sendSMS(student.getPhone(), txt);
				if (result == 200) {
					System.out.println("SMS sent to " + student.getPhone());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}