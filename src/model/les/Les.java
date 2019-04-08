package model.les;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import model.PrIS;
import model.klas.Klas;
import model.persoon.*;

public class Les {
	private PrIS informatieSysteem;
	private String naam;
	private String cursusCode;
	private String startWeek;
	private String startDag;
	private String startDatum;
	private String startTijd;
	private String eindDag;
	private String eindDatum;
	private String eindTijd;
	private String duur;
	private String werkVorm;
	private String docent;
	private String lokaalNummer;
	private String groep;
	private String faculteit;
	private String grootte;
	private String opmerking;
	private ArrayList<String> leerlingen = new ArrayList<String>();
	private ArrayList<Student> deAfwezigen = new ArrayList<Student>();
	
	public Les(String naam, String cursusCode, String startWeek, String startDag, String startDatum, String startTijd, String eindDag,
			String eindDatum, String eindTijd, String duur, String werkVorm, String docent, String lokaalNummer, String groep,
			String faculteit, String grootte, String opmerking, ArrayList<String> students) {
		this.naam = naam;
		this.cursusCode = cursusCode;
		this.startWeek = startWeek;
		this.startDag = startDag;
		this.startDatum = startDatum;
		this.startTijd = startTijd;
		this.eindDag = eindDag;
		this.eindDatum = eindDatum;
		this.eindTijd = eindTijd;
		this.duur = duur;
		this.werkVorm = werkVorm;
		this.docent = docent;
		this.lokaalNummer = lokaalNummer;
		this.groep = groep;
		this.faculteit = faculteit;
		this.grootte = grootte;
		this.opmerking = opmerking;
		this.leerlingen = students;
	}
	
	public ArrayList<String> getLeerlingenVanLes(){
		return leerlingen;
	}
	
	public void setLeerlingen(ArrayList<String> students) {
		leerlingen = students;
	}
	
	public LocalDate getStartDatum() {
		LocalDate datum = LocalDate.parse(startDatum);
		return datum;
	}
	
	public String getDocent() {
		return docent;
	}
	public String getKlas() {
		return groep;
	}
	public String getDatum() {
		return startDatum;
	}
	public String getTijd() {
		return startTijd + " tot " + eindTijd;
	}
	public String getVak() {
		return naam;
	}
	public String getLokaal() {
		return lokaalNummer;
	}
	
	public void afwezigToevoegen(Student student) {
		deAfwezigen.add(student);
	}
	
	public String toString() {
		return naam + " is de naam van deze instantie Les!";
	}
}
	
	