package de.hauschil.dbprojekt.anwendungsfaelle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Statement;

import de.hauschil.dbprojekt.controller.DB4O_Controller;
import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.HSQL_Controller;
import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public class Fallmanager {
	public static final int FAKTOR 		= 10;
	public static final int ANZ_KUNDEN 	= 100 * FAKTOR;
	public static final int ANZ_PARTNER = FAKTOR;
	public static final int ANZ_ANRUFPM	= 3 * FAKTOR;
//	public static final String DB_PATH = "db/telefongesellschaft.db4o";
	public static final String DB4O_PATH = "D:\\tmp\\db\\telefongesellschaft.db4o";
	public static final String HSQL_PATH = "D:\\tmp\\db\\telefongesellschaft.hsql";
	
	private static DB_Controller db;
	private static long db_size;
	
	public static void main(String... args) throws IOException {
		db = new HSQL_Controller();
		db.initDBConnection(null);
	
		
//		setUpBeforeClass();
		
//		Fall1 f1 = new Fall1(db);
//		f1.run(true);
//		f1.run(false);
//		System.out.println(f1);
		
//		Fall2 f2 = new Fall2(db);
//		f2.run(true);
//		/* Benötigt bei Faktor 10 schon über eine halbe Stunde */
//		f2.run(false);
//		System.out.println(f2);
		
//		Fall4 f4 = new Fall4(db);
//		f4.run(true);
//		f4.run(false);
//		System.out.println(f4);
		
//		/* Fall3 als letztes, weil er Sachen löscht, deshalb muss auch neu generiert werden */
//		Fall3 f3 = new Fall3(db);
//		f3.run(true);
//		setUpBeforeClass();
//		f3.run(false);
//		System.out.println(f3);
		
//		System.out.println("DB size: " + db_size / 1024 + " kiB");
	}
	
	private static void setUpBeforeClass() throws IOException {
		Path dbPath = Paths.get(DB4O_PATH);
		Files.deleteIfExists(dbPath);
		
		db = new DB4O_Controller();
		db.initDBConnection(null);
		
		Kunde[] kunden = Kunde.generateKunden(ANZ_KUNDEN);
		System.out.println(ANZ_KUNDEN + " Kunden generiert");
		db.storeObject(kunden);
		Anruf.generateAnrufe(kunden, ANZ_ANRUFPM, db);
		
		db.closeDBConncetion();
		
		db_size = Files.size(dbPath);
	}
	
	private static void tearDownAfterClass() {
	}
}
