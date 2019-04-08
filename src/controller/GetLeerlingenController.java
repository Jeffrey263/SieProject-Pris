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


public class GetLeerlingenController implements Handler{
	private PrIS informatieSysteem;
	private OpslaanLeerlingenController opslag;
	
	
	public GetLeerlingenController(PrIS infoSys, OpslaanLeerlingenController opslaan) {
		informatieSysteem = infoSys;
		opslag = opslaan;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/klas")) {
			System.out.println("[leerlingen ophalen init]");
			leerlingenOphalen(conversation);
		}
	}
	
	
	private void leerlingenOphalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println(lJsonObjectIn);
		String lvak = lJsonObjectIn.getString("vak");
		String lklas = lJsonObjectIn.getString("klas");
		String ldatum = lJsonObjectIn.getString("datum");
		Rooster hetRooster = informatieSysteem.getRooster();
		Les lLes = hetRooster.getLesByInfo(lklas, lvak, ldatum);
		System.out.println(lLes);
		opslag.setActiveLes(lLes);
		ArrayList<String> leerlingen = lLes.getLeerlingenVanLes();
		System.out.println("[GetLeerlingenController] " + lLes);
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
		System.out.println("[JSONbuilder] START");
		int i = 0;
		while(i<leerlingen.size()) {		
			//if (les.getStartDatum().compareTo(systeemDatum) >= 0 && les.getStartDatum().compareTo(systeemDatum.plusDays(7)) <= 0 ) {
				System.out.println("[JSONbuilder] \tvoeg leeerling toe: " + leerlingen.get(i));
				JsonObjectBuilder lJsonObjectBuilderVoorLeerlingen = Json.createObjectBuilder(); // maak het JsonObject waar de les in komt
				lJsonObjectBuilderVoorLeerlingen.add("naam", leerlingen.get(i));
				lJsonObjectBuilderVoorLeerlingen.add("studentNummer", leerlingen.get(i+1));
				lJsonObjectBuilderVoorLeerlingen.add("aanwezigheid", leerlingen.get(i+2));
				i = i+3;
				lJsonArrayBuilder.add(lJsonObjectBuilderVoorLeerlingen); // voeg toe aan de JsonArray die wordt teruggestuurd naar de front-end/Polymer-GUI
			//}
	
		}
		String lJsonOutStr = lJsonArrayBuilder.build().toString();	
		System.out.println(lJsonOutStr);
		conversation.sendJSONMessage(lJsonOutStr); // terugsturen naar de Polymer-GUI!	
		
	}
}
