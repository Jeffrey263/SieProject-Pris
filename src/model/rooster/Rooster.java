package model.rooster;

import java.util.ArrayList;

import model.les.*;
import model.persoon.Docent;

public class Rooster {
	private ArrayList<Les> deLessen = new ArrayList<Les>();
	
	public Rooster() {
	}
	
	public void voegLesToe(Les les) {
		if(!(deLessen.contains(les))) {
			deLessen.add(les);
		}
	}
	
	public ArrayList<Les> getLesByKlas(String klas) {
		ArrayList<Les> lessenKlas = new ArrayList<Les>();
		
		for(Les les : deLessen) {
			if(les.getKlas().equals(klas)) {
				lessenKlas.add(les);
			}
		}
		
		return lessenKlas;
	}
	
	public ArrayList<Les> getLessenByDocent(String docent){
		System.out.println("[Rooster] start getLessenByDocent");
		ArrayList<Les> lessenDocent = new ArrayList<Les>();
		
		for(Les les : deLessen) {
			if(les.getDocent().equals(docent)) {
				lessenDocent.add(les);
			}
		}
		System.out.println("[Rooster] " + lessenDocent);
		return lessenDocent;
	}
	
	public Les getLesByInfo(String klas, String naam, String datum) {
		Les deLes = null;
		for(Les les : deLessen) {
//			System.out.println("------------------------------------------------------");
//			System.out.println("[Rooster] Vak: " + les.getVak() + " -=- " + naam);
//			System.out.println("[Rooster] klas: " + les.getKlas() + " -=- " + klas);
//			System.out.println("[Rooster] Datum: " + les.getDatum() + " -=- " + datum);
			
			if(les.getKlas().equals(klas) && les.getVak().equals(naam) && les.getDatum().equals(datum)){
				deLes = les;
				break;
			}
		}
		return deLes;
	}
}
