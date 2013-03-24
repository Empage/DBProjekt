package de.hauschil.dbprojekt.anwendungsfaelle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import de.hauschil.dbprojekt.controller.DB4O_Controller;
import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.HSQL_Controller;
import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public class Fallmanager {
	public static final int FAKTOR 		= 100;
	public static final int ANZ_KUNDEN 	= 100 * FAKTOR;
	public static final int ANZ_PARTNER = FAKTOR;
	public static final int ANZ_ANRUFPM	= 3 * FAKTOR;
	public static final String DB4O_PATH = "D:\\tmp\\db\\telefongesellschaft.db4o";
	public static final String HSQL_PATH = "D:\\tmp\\db\\telefongesellschaft.hsql";
	
	private static ArrayList<DB_Controller> dbs = new ArrayList<>();
	private static long db_size;
	
	public static void main(String... args) throws IOException {
//		dbs.add(new DB4O_Controller());
		dbs.add(new HSQL_Controller());		
		
		for (DB_Controller db : dbs) {
			System.out.println(db);
			long timer = System.currentTimeMillis();
			setUp(db);
			System.out.println((System.currentTimeMillis() - timer) / 1000 + " seks setuptime");
			
//			Fall1 f1 = new Fall1(db);
//			f1.run(true);
//			f1.run(false);
//			System.out.println(f1);
//			
//			Fall2 f2 = new Fall2(db);
//			f2.run(true);
//			/* Benötigt bei Faktor 10 schon über eine halbe Stunde */
//			f2.run(false);
//			System.out.println(f2);
//			
//			Fall4 f4 = new Fall4(db);
//			f4.run(true);
//			f4.run(false);
//			System.out.println(f4);
//			
//			/* Fall3 als letztes, weil er Sachen löscht, deshalb muss auch neu generiert werden */
//			Fall3 f3 = new Fall3(db);
//			f3.run(true);
//			setUp(db);
//			f3.run(false);
//			System.out.println(f3);
			
			System.out.println("DB size: " + db_size / 1024 + " kiB");
		}
	}
	
	private static void setUp(DB_Controller db) throws IOException {
		Path dbPath = null;
		
		if (db instanceof DB4O_Controller) {
			dbPath = Paths.get(DB4O_PATH);
			Files.deleteIfExists(dbPath);
		} else if (db instanceof HSQL_Controller) {
			dbPath = Paths.get(HSQL_PATH + ".script");
		} else {
			throw new RuntimeException("TODO");
		}
		
		db.initDBConnection();
		
		if (db instanceof HSQL_Controller) {
			db.dropTables();
			db.createTables();
		}
		
		Kunde[] kunden = Kunde.generateKunden(ANZ_KUNDEN);
		System.out.println(ANZ_KUNDEN + " Kunden generiert");
		db.storeKunden(kunden);
		Anruf.generateAnrufe(kunden, ANZ_ANRUFPM, db);
		
		db.closeDBConncetion();
		db_size = Files.size(dbPath);
	}
}
