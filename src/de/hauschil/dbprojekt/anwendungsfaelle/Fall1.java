package de.hauschil.dbprojekt.anwendungsfaelle;

import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.Index;
import de.hauschil.dbprojekt.model.Kunde;

public class Fall1 {
	private long[] anfangszeit;
	private long[] endzeit;
	private DB_Controller db;
	private Kunde[] gesuchte;
	
	public Fall1(DB_Controller db) {
		this.db = db;
		anfangszeit = new long[2];
		endzeit = new long[2];
		gesuchte = Kunde.generateKunden(1000);
	}
	
	public void run(boolean indexed) {
		db.initDBConnection(new Index[] {
			new Index(Kunde.class, "vorname", indexed), 
			new Index(Kunde.class, "nachname", indexed)
		});
	
		anfangszeit[indexed ? 1 : 0] = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			/* liefert gesuchte Kunden zurück */
			db.getKunden(gesuchte[i % 1000].getVorname(), gesuchte[i % 1000].getNachname());
		}
		db.closeDBConncetion();
		
		endzeit[indexed ? 1 : 0] = System.nanoTime();
	}
	
	public long getTime(int which) {
		return (endzeit[which] - anfangszeit[which]) / 1000 / 1000;
	}

	@Override
	public String toString() {
		return "Fall1 ohne Index: " + getTime(0) + " ms\n" +
			   "Fall1  mit Index: " + getTime(1) + " ms";
	}
}
