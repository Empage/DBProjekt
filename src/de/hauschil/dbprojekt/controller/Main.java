package de.hauschil.dbprojekt.controller;

import java.util.List;

import com.db4o.query.Predicate;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public class Main {
	public static final int FAKTOR 		= 2;
	public static final int ANZ_KUNDEN 	= 100 * FAKTOR;
	public static final int ANZ_PARTNER = FAKTOR;
	public static final int ANZ_ANRUFPM	= 3 * FAKTOR;
	public static final String DB_PATH = "db/telefongesellschaft.db4o";
	
	public static void main(String[] args) {
		DB_Controller db = new DB4O_Controller();
//		db.initDBConnection();
//		
//		Kunde[] kunden = Kunde.generateKunden(ANZ_KUNDEN);
//		System.out.println(ANZ_KUNDEN + " Kunden generiert");
//		db.storeObject(kunden);
//		Anruf.generateAnrufe(kunden, ANZ_ANRUFPM, db);
//		
//		db.closeDBConncetion();
		
		db.initDBConnection();
		List<Anruf> anruf = db.query(new Predicate<Anruf>() {
			public boolean match(Anruf k) {
				return k.getDatum().getMonth() == 11;
			}
		});
		for (Anruf a : anruf)
			System.out.println(a.getDatum());
		
		db.closeDBConncetion();
	}

}
