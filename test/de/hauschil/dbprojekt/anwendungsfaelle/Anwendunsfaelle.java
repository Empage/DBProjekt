package de.hauschil.dbprojekt.anwendungsfaelle;

import static org.junit.Assert.*;
import static de.hauschil.dbprojekt.controller.Main.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hauschil.dbprojekt.controller.DB4O_Controller;
import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public class Anwendunsfaelle {
	private static DB_Controller db;
	private static long zeit[][];
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		zeit = new long[8][2];
		Path dbPath = Paths.get(DB_PATH);
		Files.deleteIfExists(dbPath);
		
		db = new DB4O_Controller();
		db.initDBConnection();
		
		Kunde[] kunden = Kunde.generateKunden(ANZ_KUNDEN);
		System.out.println(ANZ_KUNDEN + " Kunden generiert");
		db.storeObject(kunden);
		Anruf.generateAnrufe(kunden, ANZ_ANRUFPM, db);
		db.closeDBConncetion();
		db.initDBConnection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		db.closeDBConncetion();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/* Telefonnummer über Vorname/Nachname */
	@Test
	public void Fall1() {
		/* Generiere zunächst Kunden, nach denen gesucht werden kann */
		final Kunde[] kunden = Kunde.generateKunden(1000);
		
		zeit[0][0] = System.nanoTime();
		for (int i = 0; i < 1; i++) {
//			db.query(new Predicate<Kunde>() {
//				@Override
//				public boolean match(Kunde k) {
//					if (
//						k.getNachname().equals(kunden[i % 1000].getNachname()) && 
//						k.getVorname().equals(kunden[i % 1000].getVorname())
//					) {
//				
//					}
//				}
//				
//			});
//			Chauffeur	chauffeur = new Chauffeur (null, 29);
//			List<Chauffeur>	resultat = getDb ().queryByExample (chauffeur);
//			System.out.println ("Mit \"getDb ().queryByExample (new Chauffeur (null, 29))\" finden wir:");
//			DB.zeigeResultat (resultat);
			Kunde k = new Kunde("Schmidt", null, null);
			List<Kunde> result = db.query(k);
			System.out.println(result);
		}
		
		zeit[0][1] = System.nanoTime();
		assertTrue(true);
	}

	@Test
	public void Fall2() {
		fail("Not yet implemented");
	}

	@Test
	public void Fall3() {
		fail("Not yet implemented");
	}
	
	@Test
	public void Fall4() {
		fail("Not yet implemented");
	}
	
	@Test
	public void Fall1Indexed() {
		fail("Not yet implemented");
	}

	@Test
	public void Fall2Indexed() {
		fail("Not yet implemented");
	}

	@Test
	public void Fall3Indexed() {
		fail("Not yet implemented");
	}
	
	@Test
	public void Fall4Indexed() {
		fail("Not yet implemented");
	}

}
