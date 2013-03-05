package de.hauschil.dbprojekt.anwendungsfaelle;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.controller.DB_Controller;
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
	
	public void run() {
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(Kunde.class).objectField("vorname").indexed(false);
		conf.common().objectClass(Kunde.class).objectField("nachname").indexed(false);
		db.initDBConnection(conf);
	
		anfangszeit[0] = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			Query query = db.query();
			query.constrain(Kunde.class);
			query.descend("vorname").constrain(gesuchte[i % 1000].getVorname());
			query.descend("nachname").constrain(gesuchte[i % 1000].getNachname());
			ObjectSet set = query.execute();
		}
		db.closeDBConncetion();
		
		endzeit[0] = System.nanoTime();
	}
	
	public void runIndexed() {
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(Kunde.class).objectField("vorname").indexed(true);
		conf.common().objectClass(Kunde.class).objectField("nachname").indexed(true);
		db.initDBConnection(conf);
		System.out.println("Indizes kreiert");
		
		anfangszeit[1] = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			Query query = db.query();
			query.constrain(Kunde.class);
			query.descend("vorname").constrain(gesuchte[i % 1000].getVorname());
			query.descend("nachname").constrain(gesuchte[i % 1000].getNachname());
			ObjectSet set = query.execute();
		}
		db.closeDBConncetion();
		
		endzeit[1] = System.nanoTime();
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
