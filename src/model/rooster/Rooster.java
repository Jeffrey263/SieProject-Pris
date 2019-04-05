package model.rooster;

import java.util.ArrayList;

import model.les.*;

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
}
