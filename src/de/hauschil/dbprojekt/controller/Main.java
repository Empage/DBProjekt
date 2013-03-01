package de.hauschil.dbprojekt.controller;

import java.util.Arrays;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public class Main {
	private static final int FAKTOR = 100;
	
	public static void main(String[] args) {
		Kunde[] kunden = Kunde.generateKunden(FAKTOR);
		Anruf[] anrufe = Anruf.generateAnrufe(kunden, FAKTOR);
		
		System.out.println(kunden.length);
		System.out.println(anrufe.length);
		for (int i = 0; i < kunden.length; i++) {
//			System.out.println(kunden[i]);
		}
		for (int i = 0; i < anrufe.length; i++) {
//			System.out.println(anrufe[i]);
		}
	}

}
