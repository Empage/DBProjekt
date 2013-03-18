package de.hauschil.dbprojekt.model;

import java.util.ArrayList;
import java.util.Random;

import de.hauschil.dbprojekt.controller.Helper;

public class Telefon {
	private static long base = 132342;
	private static Random r = new Random(42);
	
	private String nummer;
	
	public Telefon(String telnummer) {
		nummer = telnummer;
	}
		
	@Override
	public String toString() {
		return nummer;
	}
	
	public static ArrayList<Telefon> generateTelefon(int count) {
		ArrayList<Telefon> tels = new ArrayList<>(count);
		
		for (int i = 0; i < count; i++) {
			tels.add(new Telefon(
				Helper.vorwahlen[r.nextInt(Helper.vorwahlen.length)] + "/" + base++ + r.nextInt(100)
			));
		}
		
		return tels;
	}
}
