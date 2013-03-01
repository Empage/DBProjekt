package de.hauschil.dbprojekt.model;

import java.util.Random;

import de.hauschil.dbprojekt.controller.Helper;

public class Telefon {
	private static long base = 132342;
	
	private String nummer;
	
	public Telefon(String telnummer) {
		nummer = telnummer;
	}
	
	public static Telefon generateTelefon() {
		Random r = new Random(42);
		return new Telefon(
			Helper.vorwahlen[r.nextInt(Helper.vorwahlen.length)] + "/" + base++ + r.nextInt(100)
		);
	}
}
