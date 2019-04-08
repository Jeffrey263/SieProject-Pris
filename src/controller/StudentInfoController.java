package controller;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.klas.Klas;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

public class StudentInfoController implements Handler{
private PrIS informatieSysteem;
	
	public StudentInfoController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/info/opvragen")) {
			infoOphalen(conversation);
		}
	}
	
	public void infoOphalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println(lJsonObjectIn);
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
		Klas lKlas = informatieSysteem.getKlasVanStudent(lStudentZelf);		// klas van de student opzoeken
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
		
		JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject waar de les in komt
		lJsonObjectBuilderVoorStudent
		.add("voornaam", lStudentZelf.getVoornaam())
		.add("achternaam", lStudentZelf.getVolledigeAchternaam())
		.add("studentNummer", lStudentZelf.getStudentNummer())
		.add("klasNaam", lKlas.getNaam());
		lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent); // voeg toe aan de JsonArray die wordt teruggestuurd naar de front-end/Polymer-GUI
			
		String lJsonOutStr = lJsonArrayBuilder.build().toString();	
		conversation.sendJSONMessage(lJsonOutStr); // terugsturen naar de Polymer-GUI!
	}	
}

