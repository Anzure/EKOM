package no.fagskolentelemark.handlers;

import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import no.fagskolentelemark.EkomMain;

public class ConfigHandler {

	private static JSONParser parser = new JSONParser();
	private static JSONObject json = new JSONObject();

	public static void loadConfig(){
		try {
			Object obj = parser.parse(new FileReader(EkomMain.mainDir + File.separator + "config.json"));
			json = (JSONObject) obj;

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static boolean containsKey(String key){
		return json.containsKey(key);
	}

	public static Object get(String key){
		return json.get(key);
	}
}