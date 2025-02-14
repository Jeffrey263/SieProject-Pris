package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.klas.Klas;
import model.persoon.Docent;
import model.persoon.Student;
import model.les.*;
import model.rooster.*;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;
	private ArrayList<Klas> deKlassen;
	public Rooster hetRooster;

	/**
	 * De constructor maakt een set met standaard-data aan. Deze data moet nog
	 * uitgebreidt worden met rooster gegevens die uit een bestand worden ingelezen,
	 * maar dat is geen onderdeel van deze demo-applicatie!
	 * 
	 * De klasse PrIS (PresentieInformatieSysteem) heeft nu een meervoudige
	 * associatie met de klassen Docent, Student, Vakken en Klassen Uiteraard kan
	 * dit nog veel verder uitgebreid en aangepast worden!
	 * 
	 * De klasse fungeert min of meer als ingangspunt voor het domeinmodel. Op dit
	 * moment zijn de volgende methoden aanroepbaar:
	 * 
	 * String login(String gebruikersnaam, String wachtwoord) Docent
	 * getDocent(String gebruikersnaam) Student getStudent(String gebruikersnaam)
	 * ArrayList<Student> getStudentenVanKlas(String klasCode)
	 * 
	 * Methode login geeft de rol van de gebruiker die probeert in te loggen, dat
	 * kan 'student', 'docent' of 'undefined' zijn! Die informatie kan gebruikt
	 * worden om in de Polymer-GUI te bepalen wat het volgende scherm is dat getoond
	 * moet worden.
	 * 
	 */
	public PrIS() {
		deDocenten = new ArrayList<Docent>();
		deStudenten = new ArrayList<Student>();
		deKlassen = new ArrayList<Klas>(); // Inladen klassen
		hetRooster = new Rooster();
		
		
		vulKlassen(deKlassen); // Inladen studenten in klassen
		vulStudenten(deStudenten, deKlassen);
		// Inladen docenten
		vulDocenten(deDocenten);
		vulRooster();

	} // Einde Pris constructor

	// deze method is static onderdeel van PrIS omdat hij als hulp methode
	// in veel controllers gebruikt wordt
	// een standaardDatumString heeft formaat YYYY-MM-DD
	public static Calendar standaardDatumStringToCal(String pStadaardDatumString) {
		Calendar lCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			lCal.setTime(sdf.parse(pStadaardDatumString));
		} catch (ParseException e) {
			e.printStackTrace();
			lCal = null;
		}
		return lCal;
	}

	// deze method is static onderdeel van PrIS omdat hij als hulp methode
	// in veel controllers gebruikt wordt
	// een standaardDatumString heeft formaat YYYY-MM-DD
	public static String calToStandaardDatumString(Calendar pCalendar) {
		int lJaar = pCalendar.get(Calendar.YEAR);
		int lMaand = pCalendar.get(Calendar.MONTH) + 1;
		int lDag = pCalendar.get(Calendar.DAY_OF_MONTH);

		String lMaandStr = Integer.toString(lMaand);
		if (lMaandStr.length() == 1) {
			lMaandStr = "0" + lMaandStr;
		}
		String lDagStr = Integer.toString(lDag);
		if (lDagStr.length() == 1) {
			lDagStr = "0" + lDagStr;
		}
		String lString = Integer.toString(lJaar) + "-" + lMaandStr + "-" + lDagStr;
		return lString;
	}

	public Docent getDocent(String gebruikersnaam) {
		return deDocenten.stream().filter(d -> d.getGebruikersnaam().equals(gebruikersnaam)).findFirst().orElse(null);
	}

	public Klas getKlasVanStudent(Student pStudent) {
		return deKlassen.stream().filter(k -> k.bevatStudent(pStudent)).findFirst().orElse(null);
	}
	
	public ArrayList<Klas> getKlassen(){
		return deKlassen;
	}
	
	public Student getStudent(String pGebruikersnaam) {
		return deStudenten.stream().filter(s -> s.getGebruikersnaam().equals(pGebruikersnaam)).findFirst().orElse(null);
	}
	
	public Student getStudentByNaam(String pNaam) {
		return deStudenten.stream().filter(s -> (s.getVoornaam() + " " + s.getVolledigeAchternaam()).equals(pNaam)).findFirst().orElse(null);
	}

	public Student getStudentByNummer(int pStudentNummer) {
		return deStudenten.stream().filter(s -> s.getStudentNummer() == pStudentNummer).findFirst().orElse(null);
	}

	public Rooster getRooster() {
		return hetRooster;
	}
	
	public String login(String gebruikersnaam, String wachtwoord) {
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				if (d.komtWachtwoordOvereen(wachtwoord)) {
					return "docent";
				}
			}
		}

		for (Student s : deStudenten) {
			if (s.getGebruikersnaam().equals(gebruikersnaam)) {
				if (s.komtWachtwoordOvereen(wachtwoord)) {
					return "student";
				}
			}
		}

		return "undefined";
	}

	private void vulDocenten(ArrayList<Docent> pDocenten) {
		String csvFile = "././CSV/docenten.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		System.out.println(line);

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// use ";" as separator -> new csv files are incorrect.
				String[] element = line.split(cvsSplitBy);
				String gebruikersnaam = element[0].toLowerCase();
				String voornaam = element[1];
				String tussenvoegsel = element[2];
				String achternaam = element[3];
				String naamtest = voornaam +" "+ tussenvoegsel +" "+ achternaam;
				pDocenten.add(new Docent(voornaam, tussenvoegsel, achternaam, "geheim", gebruikersnaam, 1));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close the bufferedReader if opened.
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// verify content of arraylist, if empty add Jos
			if (pDocenten.isEmpty())
				pDocenten.add(new Docent("Jos", "van", "Reenen", "supergeheim", "jos.vanreenen@hu.nl", 1));
		}
	}

	private void vulKlassen(ArrayList<Klas> pKlassen) {
		// TICT-SIE-VIA is de klascode die ook in de rooster file voorkomt
		// V1A is de naam van de klas die ook als file naam voor de studenten van die
		// klas wordt gebruikt
		Klas k1 = new Klas("TICT-SIE-V1A", "V1A");
		Klas k2 = new Klas("TICT-SIE-V1B", "V1B");
		Klas k3 = new Klas("TICT-SIE-V1C", "V1C");
		Klas k4 = new Klas("TICT-SIE-V1D", "V1D");
		Klas k5 = new Klas("TICT-SIE-V1E", "V1E");

		pKlassen.add(k1);
		pKlassen.add(k2);
		pKlassen.add(k3);
		pKlassen.add(k4);
		pKlassen.add(k5);
	}

	private void vulStudenten(ArrayList<Student> pStudenten, ArrayList<Klas> pKlassen) {
		Student lStudent;
		Student dummyStudent = new Student("Stu", "de", "Student", "geheim", "test@student.hu.nl", 0);
		for (Klas k : pKlassen) {
			// per klas
			String csvFile = "././CSV/" + k.getNaam() + ".csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ";";

			try {

				br = new BufferedReader(new FileReader(csvFile));

				while ((line = br.readLine()) != null) {
					// line = line.replace(",,", ", ,");
					// use comma as separator
					String[] element = line.split(cvsSplitBy);
					String gebruikersnaam = (element[3] + "." + element[2] + element[1] + "@student.hu.nl")
							.toLowerCase();
					// verwijder spaties tussen dubbele voornamen en tussen bv van der
					gebruikersnaam = gebruikersnaam.replace(" ", "");
					String lStudentNrString = element[0];
					int lStudentNr = Integer.parseInt(lStudentNrString);
					// Volgorde 3-2-1 = voornaam, tussenvoegsel en achternaam
					lStudent = new Student(element[3], element[2], element[1], "geheim", gebruikersnaam, lStudentNr);
					pStudenten.add(lStudent);
					k.voegStudentToe(lStudent);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// mocht deze klas geen studenten bevatten omdat de csv niet heeft gewerkt:
				if (k.getStudenten().isEmpty()) {
					k.voegStudentToe(dummyStudent);
					System.out.println("Had to add Stu de Student to class: " + k.getKlasCode());
				}
			}

		}
		// mocht de lijst met studenten nu nog leeg zijn
		if (pStudenten.isEmpty())
			pStudenten.add(dummyStudent);
	}
	
	private void vulRooster() {
		JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
		
		String csvFile = "././CSV/rooster.csv";
		BufferedReader br = null;
		String line = "";
		line = line.replace(",,", ", ,");
		String cvsSplitBy = ",";
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] klasCheck = line.split(cvsSplitBy);
				// use comma as separator
				String[] element = line.split(",(?=([^\"]|\"[^\"]*\")*$)");
				String naam = element[0].replace("\"", "");
				String cursusCode = element[1].replace("\"", "");
				String startWeek = element[2].replace("\"", "");
				String startDag = element[3].replace("\"", "");
				String startDatum = element[4].replace("\"", "");
				String startTijd = element[5].replace("\"", "");
				String eindDag = element[6].replace("\"", "");
				String eindDatum = element[7].replace("\"", "");
				String eindTijd = element[8].replace("\"", "");
				String duur = element[9].replace("\"", "");
				String werkVorm = element[10].replace("\"", "");
				String docent = element[11].replace("\"", "");
				String lokaalNummer = element[12].replace("\"", "");
				String groep = element[13].replace("\"", "");
				String faculteit = element[14].replace("\"", "");
				String grootte = element[15].replace("\"", "");
				String opmerking = element[16].replace("\"", "");
				
				ArrayList<String> leerlingen = new ArrayList<String>();
				for(Klas klas : deKlassen) {
					String klasCodeZoek = klas.getKlasCode();
					if(groep.equals(klasCodeZoek)) {
						List<Student> lijstLeerlingen = klas.getStudenten();
						for(Student leerling : lijstLeerlingen) {
							String lnaam = leerling.getVoornaam() + " " + leerling.getVolledigeAchternaam();
							String lnummer = Integer.toString(leerling.getStudentNummer());
							leerlingen.add(lnaam);
							leerlingen.add(lnummer);
							leerlingen.add("Aanwezig");
						}
					}
				}
				System.out.println("[PRIS les] " + groep + "  -  " + leerlingen);
				
				//Toevoegen tijdelijk les object
				Les lesHolder = new Les(naam, cursusCode, startWeek, startDag, startDatum, startTijd,
						eindDag, eindDatum, eindTijd, duur, werkVorm, docent, lokaalNummer, groep,
						faculteit, grootte, opmerking, leerlingen);
				
				//Toevoegen aan het rooster
				hetRooster.voegLesToe(lesHolder);
			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
		
	}	

}
