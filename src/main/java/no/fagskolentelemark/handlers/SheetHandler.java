package no.fagskolentelemark.handlers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import no.fagskolentelemark.EkomMain;
import no.fagskolentelemark.objects.Student;
import no.fagskolentelemark.utils.MainUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SheetHandler {

	private static final String APPLICATION_NAME = "Fagskolen Ekom";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.

	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	private static final String CLIENT_SECRET_DIR = "client_secret.json";

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = SheetHandler.class.getResourceAsStream(CLIENT_SECRET_DIR);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
				.setAccessType("offline")
				.build();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	public static void processSheet() throws Exception {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		final String spreadsheetId = EkomMain.gSheetId;
		final String range = "A:Z";
		Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME)
				.build();
		ValueRange response = service.spreadsheets().values()
				.get(spreadsheetId, range)
				.execute();

		List<List<Object>> values = response.getValues();
		if (values == null || values.isEmpty()) {
			System.out.println("No data found.");
		} else {
			String fileName = MainUtil.getDate() + ".csv";
			File outputFile = null;
			FileOutputStream outputStream = null;
			BufferedWriter outputWriter = null;

			HashMap<String, Integer> header = new HashMap<String, Integer>(); 
			for (@SuppressWarnings("rawtypes") List row : values) {
				// Load header
				if (header.isEmpty()) {
					int i = 0;
					for (Object o : row) {
						String raw = (String) o;
						header.put(raw, i);
						i++;
					}

				}
				// Proccess rows
				else {
					String firstName = (String) row.get(header.get("Fornavn"));
					String lastName = (String) row.get(header.get("Etternavn"));
					String email = (String) row.get(header.get("Din epost"));
					int phone = Integer.parseInt(((String) row.get(header.get("Tlf nr"))).replace("+47", "").replace(" ", ""));

					String username = (lastName.substring(0, 3) + firstName.substring(0, 1) + EkomMain.ekomNum).toLowerCase().replace("æ", "e").replace("ø", "o").replace("å", "a");
					String password = MainUtil.generatePassword();

					Student user = new Student(firstName, lastName, username, phone, email, password);
					if (!HistoryHandler.seenBefore(user)) {
						// Open stream for the first time
						if (outputWriter == null) {
							outputFile = new File(EkomMain.mainDir + File.separator + "Imports" + File.separator + fileName);
							outputStream = new FileOutputStream(outputFile);
							outputWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
						}

						// Write to stream
						outputWriter.write(username + ";" + password + ";" + firstName + ";" + lastName + ";" + email + ";" + phone);
						outputWriter.newLine();
						
						// Add new student
						EkomMain.newStudents.add(user);
					}
				}
			}

			if (outputWriter != null) outputWriter.close();
		}
	}
}