package de.hauschil.dbprojekt.model;

import java.util.ArrayList;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nachname == null) ? 0 : nachname.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kunde other = (Kunde) obj;
		if (nachname == null) {
			if (other.nachname != null)
				return false;
		} else if (!nachname.equals(other.nachname))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (vorname == null) {
			if (other.vorname != null)
				return false;
		} else if (!vorname.equals(other.vorname))
			return false;
		return true;
	}
}
