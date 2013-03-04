package de.hauschil.dbprojekt.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import de.hauschil.dbprojekt.controller.Helper;

public class Kunde {
	private static Random r = new Random(42);
	
	private String nachname;
	private String vorname;
	
	private ArrayList<Telefon> telefone;
	
	public Kunde(String vorname, String nachname, ArrayList<Telefon> tel) {
		this.nachname = nachname;
		this.vorname = vorname;
		telefone = tel;
	}
	
	public static Kunde[] generateKunden(int anzahl) {
		Kunde[] kunden = new Kunde[anzahl];
		
		/* generiere einen Kunden mit bis zu 5 Telefonen und random Vor- und Nachnamen*/
		for (int i = 0; i < anzahl; i++) {
			kunden[i] = new Kunde(
				Helper.vornamen[r.nextInt(Helper.vornamen.length)],
				Helper.nachnamen[r.nextInt(Helper.nachnamen.length)],
				Telefon.generateTelefon(r.nextInt(5) + 1)
			);
		}
		
		return kunden;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public ArrayList<Telefon> getTelefone() {
		return telefone;
	}

	public void setTelefone(ArrayList<Telefon> telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		return vorname + " " + nachname + " " + telefone;
	}
}
