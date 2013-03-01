package de.hauschil.dbprojekt.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import de.hauschil.dbprojekt.controller.Helper;

public class Kunde {
	private String nachname;
	private String vorname;
	
	private ArrayList<Telefon> telefone;
	
	public Kunde(String vorname, String nachname, Telefon tel) {
		this.nachname = nachname;
		this.vorname = vorname;
		telefone = new ArrayList<>(1);
		telefone.add(tel);
	}
	
	public static Kunde[] generateKunden(int faktor) {
		Kunde[] kunden = new Kunde[faktor * 100];
		Random r = new Random(42);
		
		for (int i = 0; i < faktor * 100; i++) {
			kunden[i] = new Kunde(
				Helper.vornamen[r.nextInt(Helper.vornamen.length)],
				Helper.nachnamen[r.nextInt(Helper.nachnamen.length)],
				Telefon.generateTelefon(1)[0]
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
