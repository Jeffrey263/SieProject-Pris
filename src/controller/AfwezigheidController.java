package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import model.PrIS;
import model.klas.Klas;
import model.les.Les;
import model.persoon.Student;
import model.rooster.Rooster;
import server.Conversation;
import server.Handler;

public class AfwezigheidController implements Handler {
	private PrIS informatieSysteem;
	
	public AfwezigheidController(PrIS infoSysteem) {
		informatieSysteem = infoSysteem;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/afwezigheid/doorgeven")) {
			afwezigheidDoorgeven(conversation);
		} 
	}
	
	public void afwezigheidDoorgeven(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println(lJsonObjectIn);
		Rooster hetRooster = informatieSysteem.getRooster();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
		JsonArray jsonArray = lJsonObjectIn.getJsonArray("info");
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject lesInfo = (JsonObject) jsonArray.get(i);
			if(lesInfo.getString("aanwezigheid").equals("false")) {
				String klas = lesInfo.getString("klas");
				String naam = lesInfo.getString("vak");
				String datum = lesInfo.getString("datum");
				
				
				///De les ophalen en vervolgens de functie afwezigToevoegen gebruiken om de ingelogde student toe te voegen
				Les deLes = hetRooster.getLesByInfo(klas, naam, datum);
				deLes.afwezigToevoegen(lStudentZelf);
				System.out.println("[AfwezigheidsController] Op afwezig gezet!");
			}
			if(lesInfo.getString("aanwezigheid").equals("true")) {
				String klas = lesInfo.getString("klas");
				String naam = lesInfo.getString("vak");
				String datum = lesInfo.getString("datum");
				
				///De les ophalen en vervolgens de functie afwezigToevoegen gebruiken om de ingelogde student toe te voegen
				Les deLes = hetRooster.getLesByInfo(klas, naam, datum);
				if(deLes.getAfwezigen().contains(lStudentZelf)) {
					deLes.afwezigVerwijderen(lStudentZelf);
					System.out.println("[AfwezigheidsController] Op aanwezig gezet!");
				}
			}
		}	
	}
}
