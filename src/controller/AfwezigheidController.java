package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.json.JsonArray;
import javax.json.JsonObject;

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
		String lKlas;
		String lNaam;
		String lDatum;
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
//		JsonArray jsonArray = lJsonObjectIn.getJsonArray("info");
//		System.out.println(jsonArray);
//		ArrayList<String> deLessen = new ArrayList<String>();
//		for(int i=0; i<jsonArray.size(); i++) {
//			deLessen.add(jsonArray.getJsonObject(i).toString());
//		}
//		System.out.println(deLessen);
//		Les deLes = hetRooster.getLesByInfo(klas, naam, datum);
//		deLes.afwezigToevoegen(lStudentZelf);
		
		
	}

}
