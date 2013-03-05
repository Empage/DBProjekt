package de.hauschil.dbprojekt.anwendungsfaelle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.hauschil.dbprojekt.controller.DB4O_Controller;
import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public class Fallmanager {
	public static final int FAKTOR 		= 20;
	public static final int ANZ_KUNDEN 	= 100 * FAKTOR;
	public static final int ANZ_PARTNER = FAKTOR;
	public static final int ANZ_ANRUFPM	= 3 * FAKTOR;
	public static final String DB_PATH = "db/telefongesellschaft.db4o";
	
	private static DB_Controller db;
	
	public static void main(String... args) throws IOException {
		setUpBeforeClass();
		
		Fall1 f1 = new Fall1(db);
		f1.runIndexed();
		f1.run();
		
		System.out.println(f1);
	}
	
	private static void setUpBeforeClass() throws IOException {
		Path dbPath = Paths.get(DB_PATH);
		Files.deleteIfExists(dbPath);
		
		db = new DB4O_Controller();
		db.initDBConnection(null);
		
		Kunde[] kunden = Kunde.generateKunden(ANZ_KUNDEN);
		System.out.println(ANZ_KUNDEN + " Kunden generiert");
		db.storeObject(kunden);
//		Anruf.generateAnrufe(kunden, ANZ_ANRUFPM, db);
		db.closeDBConncetion();
	}
	
	private static void tearDownAfterClass() {
	}
}
