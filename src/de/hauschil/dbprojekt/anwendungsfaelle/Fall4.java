package de.hauschil.dbprojekt.anwendungsfaelle;

import java.util.ArrayList;
import java.util.Random;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

public class Fall4 {
	private long[] anfangszeit;
	private long[] endzeit;
	private DB_Controller db;
	private Kunde[] gesuchte;
	
	public Fall4(DB_Controller db) {
		this.db = db;
		anfangszeit = new long[2];
		endzeit = new long[2];
	}
	
	public void run(boolean indexed) {
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(Kunde.class).objectField("vorname").indexed(indexed);
		conf.common().objectClass(Kunde.class).objectField("nachname").indexed(indexed);
		db.initDBConnection(conf);
		
		Kunde[] kunden = getKundenFromDb(50);
	
		anfangszeit[indexed ? 1 : 0] = System.nanoTime();
		for (Kunde k : kunden) {
			getAnrufeFromDb(k);
		}
		
		db.closeDBConncetion();
		endzeit[indexed ? 1 : 0] = System.nanoTime();
	}
	
	public long getTime(int which) {
		return (endzeit[which] - anfangszeit[which]) / 1000 / 1000;
	}

	@Override
	public String toString() {
		return "Fall4 ohne Index: " + getTime(0) + " ms\n" +
			   "Fall4  mit Index: " + getTime(1) + " ms";
	}
	
	private Kunde[] getKundenFromDb(int count) {
		Random r = new Random(1337);
		Kunde[] k = new Kunde[count];
		
		Query query = db.query();
		query.constrain(Kunde.class);
		ObjectSet set = query.execute();
		
		for (int i = 0; i < count; i++) {
			k[i] = (Kunde) set.get(r.nextInt(set.size()));
		}
		
		return k;
	}
	
	public ArrayList<Anruf> getAnrufeFromDb(Kunde k) {
		ArrayList<Anruf> list = new ArrayList<>();
		for (Telefon tel : k.getTelefone()) {
			Query query = db.query();
			query.constrain(Anruf.class);
			Constraint constraint1 = query.descend("anrufer").constrain(tel);
			query.descend("angerufener").constrain(tel).or(constraint1);
			ObjectSet set = query.execute();
			list.addAll(set);
		}
		
		return list;
	}
}
