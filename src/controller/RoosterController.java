package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.klas.Klas;
import model.persoon.Student;
import server.Conversation;
import server.Handler;


public class RoosterController implements Handler{
	private PrIS informatieSysteem;
	
	public RoosterController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/rooster")) {
			ophalen(conversation);
		} 
	}
	
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println(lJsonObjectIn);
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		LocalDate systeemDatum = LocalDate.parse(lJsonObjectIn.getString("systeemdatum"));
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
		Klas lKlas = informatieSysteem.getKlasVanStudent(lStudentZelf);		// klas van de student opzoeken
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
		JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
		
//		for(int i=0; i<14; i++) {
			String csvFile = "././CSV/rooster.csv";
			BufferedReader br = null;
			String line = "";
			line = line.replace(",,", ", ,");
			String cvsSplitBy = ",";
			
			try {
				br = new BufferedReader(new FileReader(csvFile));
				while ((line = br.readLine()) != null) {
					String[] klasCheck = line.split(cvsSplitBy);
					if(klasCheck[13].contains(lKlas.getKlasCode()) 
							&& LocalDate.parse(klasCheck[4].replace("\"", "")).compareTo(systeemDatum) >= 0
							&& (LocalDate.parse(klasCheck[4].replace("\"", "")).compareTo(systeemDatum.plusDays(7)) <= 0)){
						// use comma as separator
						String[] element = line.split(",(?=([^\"]|\"[^\"]*\")*$)");
						String naam = element[0].replace("\"", "");
						String cursusCode = element[1].replace("\"", "");
						String startWeek = element[2].replace("\"", "");
						String startDag = element[3].replace("\"", "");
						String startDatum = element[4].replace("\"", "");
						String startTijd = element[5].replace("\"", "");
						String eindDag = element[6].replace("\"", "");
						String eindDatum = element[7].replace("\"", "");
						String eindTijd = element[8].replace("\"", "");
						String duur = element[9].replace("\"", "");
						String werkVorm = element[10].replace("\"", "");
						String docent = element[11].replace("\"", "");
						String lokaalNummer = element[12].replace("\"", "");
						String groep = element[13].replace("\"", "");
						String faculteit = element[14].replace("\"", "");
						String grootte = element[15].replace("\"", "");
						String opmerking = element[16].replace("\"", "");
						
						//Toevoegen aan Object
						lJsonObjectBuilderVoorStudent
						.add("naam", naam)																	//vul het JsonObject		     
						.add("cursusCode", cursusCode)	
						.add("startWeek", startWeek)				     
						.add("startDag", startDag)
						.add("startDatum", startDatum)	
						.add("startTijd", startTijd)				     
						.add("eindDag", eindDag)
						.add("eindDatum", eindDatum)	
						.add("eindTijd", eindTijd)				     
						.add("duur", duur)
						.add("werkVorm", werkVorm)	
						.add("docent", docent)				     
						.add("lokaalNummer", lokaalNummer)
						.add("groep", groep)
						.add("faculteit", faculteit)	
						.add("grootte", grootte)				     
						.add("opmerking", opmerking)
						.add("aanwezig", "true");
						
						lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);	
					}
				}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			}
//		}
		
		String lJsonOutStr = lJsonArrayBuilder.build().toString();	
		conversation.sendJSONMessage(lJsonOutStr); // terugsturen naar de Polymer-GUI!
	}	
}

