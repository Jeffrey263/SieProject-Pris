package controller;

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
		if (conversation.getRequestedURI().startsWith("student/rooster")) {
			ophalen(conversation);
		} 
	}
	
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
		String  lGroepIdZelf = lStudentZelf.getGroepId();
		
		Klas lKlas = informatieSysteem.getKlasVanStudent(lStudentZelf);		// klas van de student opzoeken
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
		JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
		
		lJsonObjectBuilderVoorStudent
		.add("id", "test")																	//vul het JsonObject		     
		.add("firstName", "test")	
		.add("lastName", "test")				     
		.add("sameGroup", "test");
		
		lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);	
		
		String lJsonOutStr = lJsonArrayBuilder.build().toString();	
		conversation.sendJSONMessage(lJsonOutStr); // terugsturen naar de Polymer-GUI!
	}	
}
