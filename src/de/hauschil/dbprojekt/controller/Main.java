package de.hauschil.dbprojekt.controller;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public class Main {
	public static final int FAKTOR 		= 50;
	public static final int ANZ_KUNDEN 	= 100 * FAKTOR;
	public static final int ANZ_PARTNER = FAKTOR;
	public static final int ANZ_ANRUFPM	= 3 * FAKTOR;
	public static final String DB_PATH = "db/telefongesellschaft.db4o";
	
	public static void main(String[] args) {
		DB_Controller db = new DB4O_Controller();
		db.initDBConnection();
		Kunde[] kunden = Kunde.generateKunden(ANZ_KUNDEN);
		db.storeObject(kunden);
//		Anruf[] anrufe = Anruf.generateAnrufe(kunden, ANZ_ANRUFPM);
		Anruf.generateAnrufe(kunden, ANZ_ANRUFPM, db);
		
//		System.out.println(kunden.length);
//		System.out.println(anrufe.length);
//		for (int i = 0; i < kunden.length; i++) {
//			System.out.println(kunden[i]);
//		}
//		for (int i = 0; i < anrufe.length; i++) {
//			System.out.println(anrufe[i]);
//		}
	}

}
