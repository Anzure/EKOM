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

	public static File mainDir = new File("C:\\Users\\andre\\OneDrive - USN\\Fagskolen\\Canvas\\IoT S20");
	public static String fbLink;
	public static String courseName;
	public static String courseNum;
	public static String roomLink;
	public static boolean sendPDF;
	public static String gSheetId;

	public static List<Student> newStudents = new ArrayList<Student>();

	public static void main(String[] args) {
		try {
			// Load settings
			ConfigHandler.loadConfig();
			courseNum = (String) ConfigHandler.get("name");
			courseName = mainDir.getName();
			fbLink = (String) ConfigHandler.get("facebookLink");
			gSheetId = (String) ConfigHandler.get("googleSheetId");
			roomLink = (String) ConfigHandler.get("roomLink");
			sendPDF = (Boolean) ConfigHandler.get("sendPDF");

			// Fail safe
			if (!courseName.endsWith(courseNum)) {
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
				System.out.println("Email sent to " + student.getEmail() + " with " + student.getUsername() + " and " + student.getPassword());
			}

			// Send SMS
			for (Student student : newStudents) {
				String txt = courseName + "\nBrukernavn: " + student.getUsername() + "\nPassord: " + student.getPassword() + "\nSe epost for mer info.\n(sjekk eventuelt spam-mappen)";

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