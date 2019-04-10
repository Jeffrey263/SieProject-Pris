package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.klas.Klas;
import model.les.Les;
import model.persoon.Docent;
import model.persoon.Student;
import model.rooster.Rooster;
import server.Conversation;
import server.Handler;


public class RoosterController implements Handler{
	private PrIS informatieSysteem;
	
	public RoosterController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/rooster")) {
			studentOphalen(conversation);
		} 
	}
	
	private void studentOphalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println(lJsonObjectIn);
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		LocalDate systeemDatum = LocalDate.parse(lJsonObjectIn.getString("systeemdatum"));
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
		Klas lKlas = informatieSysteem.getKlasVanStudent(lStudentZelf);		// klas van de student opzoeken
		
		Rooster hetRooster = informatieSysteem.getRooster();
		System.out.println(hetRooster);
		ArrayList<Les> roosterStudent = hetRooster.getLesByKlas(lKlas.getKlasCode());
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
		
		for(Les les : roosterStudent) {
			if (les.getStartDatum().compareTo(systeemDatum) >= 0 && les.getStartDatum().compareTo(systeemDatum.plusDays(7)) <= 0 ) {
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject waar de les in komt
				lJsonObjectBuilderVoorStudent
				.add("datum", les.getDatum())
				.add("tijd", les.getTijd())
				.add("vak", les.getVak())
				.add("lokaal", les.getLokaal())
				.add("docent", les.getDocent())
				.add("klas", les.getKlas());
				if(les.getAfwezigen().contains(lStudentZelf)) {
					lJsonObjectBuilderVoorStudent.add("aanwezigheid", "false");
				} else {
					lJsonObjectBuilderVoorStudent.add("aanwezigheid", "true");
				}
				System.out.println(les.getLeerlingenVanLes().toString());
				lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent); // voeg toe aan de JsonArray die wordt teruggestuurd naar de front-end/Polymer-GUI
			}
	
		}
			
		String lJsonOutStr = lJsonArrayBuilder.build().toString();	
		conversation.sendJSONMessage(lJsonOutStr); // terugsturen naar de Polymer-GUI!
	}	
	
	
}

