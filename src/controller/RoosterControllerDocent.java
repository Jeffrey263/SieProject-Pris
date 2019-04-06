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


public class RoosterControllerDocent implements Handler{
	private PrIS informatieSysteem;
	
	public RoosterControllerDocent(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/rooster")) {
			docentOphalen(conversation);
		}
	}
	
	
	private void docentOphalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println(lJsonObjectIn);
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		LocalDate systeemDatum = LocalDate.parse(lJsonObjectIn.getString("systeemdatum"));
		Docent lDocentZelf = informatieSysteem.getDocent(lGebruikersnaam);
		Rooster hetRooster = informatieSysteem.getRooster();
		ArrayList<Les> roosterDocent = hetRooster.getLessenByDocent(lGebruikersnaam);
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
		System.out.println("[JSONbuilder] START");
		for(Les les : roosterDocent) {		
			//if (les.getStartDatum().compareTo(systeemDatum) >= 0 && les.getStartDatum().compareTo(systeemDatum.plusDays(7)) <= 0 ) {
				System.out.println("[JSONbuilder] \tvoeg les toe: " +les);
				JsonObjectBuilder lJsonObjectBuilderVoorDocent = Json.createObjectBuilder(); // maak het JsonObject waar de les in komt
				lJsonObjectBuilderVoorDocent.add("klas", les.getKlas());
				lJsonObjectBuilderVoorDocent.add("datum", les.getDatum());
				lJsonObjectBuilderVoorDocent.add("vak", les.getVak());
				lJsonArrayBuilder.add(lJsonObjectBuilderVoorDocent); // voeg toe aan de JsonArray die wordt teruggestuurd naar de front-end/Polymer-GUI
			//}
	
		}
			
		String lJsonOutStr = lJsonArrayBuilder.build().toString();	
		System.out.println(lJsonOutStr);
		conversation.sendJSONMessage(lJsonOutStr); // terugsturen naar de Polymer-GUI!	
	}
}

