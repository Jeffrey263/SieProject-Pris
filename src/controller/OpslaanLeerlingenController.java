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
import javax.json.JsonValue;

import model.PrIS;
import model.klas.Klas;
import model.les.Les;
import model.persoon.Docent;
import model.persoon.Student;
import model.rooster.Rooster;
import server.Conversation;
import server.Handler;


public class OpslaanLeerlingenController implements Handler{
	private PrIS informatieSysteem;
	private Les activeLes;

	
	public OpslaanLeerlingenController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/klas")) {
			System.out.println("\n\n\n\n\n\n\n\n[leerlingen opslaan init]");
			leerlingenOpslaan(conversation);
		}
	}
	
	public void setActiveLes(Les les) {
		activeLes = les;
	}
	
	
	private void leerlingenOpslaan(Conversation conversation) {
		JsonArray lJsonArrayIn = (JsonArray) conversation.getRequestBodyAsJSON();
		ArrayList<String> leerlingen = new ArrayList<String>();
		
		System.out.println("[leerlingenOpslaan] " + lJsonArrayIn);
		
		if (lJsonArrayIn != null) { 
			   int len = lJsonArrayIn.size();
			   for (int i=0;i<len;i++){ 
				   String student = lJsonArrayIn.get(i).toString();
				   String[] lstudent = student.split(":");
				   
				   String[] naam = lstudent[1].split(",");
				   String lnaam = naam[0]; 
				   lnaam = lnaam.replace('"', ' ');
				   leerlingen.add(lnaam);
				   
				   String[] nummer = lstudent[2].split(",");
				   String lnummer = nummer[0]; 
				   lnummer = lnummer.replace('"', ' ');
				   leerlingen.add(lnummer);
				   
				   String[] aanwezigheid = lstudent[3].split(",");
				   String laanwezigheid = aanwezigheid[0]; 
				   laanwezigheid = laanwezigheid.replace('"', ' ');
				   laanwezigheid = laanwezigheid.replace('}', ' ');
				   laanwezigheid = laanwezigheid.substring(1, (laanwezigheid.length()-2));
				   leerlingen.add(laanwezigheid);
			   } 
		} 
		
		
		System.out.println("[leerlingenOpslaan] " + leerlingen);
	
		activeLes.setLeerlingen(leerlingen);	
		
		conversation.sendJSONMessage("opgeslagen"); // terugsturen naar de Polymer-GUI!	
		
	}
}