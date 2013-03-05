package de.hauschil.dbprojekt.anwendungsfaelle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

public class Fall2 {
	private long[] anfangszeit;
	private long[] endzeit;
	private DB_Controller db;
	private Kunde[] gesuchte;
	
	public Fall2(DB_Controller db) {
		this.db = db;
		anfangszeit = new long[2];
		endzeit = new long[2];
	}
	
	public void run(boolean indexed) {
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(Kunde.class).objectField("vorname").indexed(indexed);
		conf.common().objectClass(Kunde.class).objectField("nachname").indexed(indexed);
		db.initDBConnection(conf);
		
		ArrayList<Kunde> kunden = getAllKundenFromDb();
		ArrayList<Long> kosten = new ArrayList<>(kunden.size());
	
		anfangszeit[indexed ? 1 : 0] = System.nanoTime();
		for (int i = 0; i < kunden.size(); i++) {
			kosten.set(i, berechneKosten(kunden.get(i)));
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
	
	private ArrayList<Kunde> getAllKundenFromDb() {
		ArrayList<Kunde> list = new ArrayList<>();
		Query query = db.query();
		query.constrain(Kunde.class);
		ObjectSet<Kunde> set = query.execute();
		list.addAll(set);
		
		return list;
	}
	
	public ArrayList<Anruf> getAnrufeFromDb(Kunde k) {
		Date date = new Date(2012, 11, 01);
		ArrayList<Anruf> list = new ArrayList<>();
		for (Telefon tel : k.getTelefone()) {
			Query query = db.query();
			query.constrain(Anruf.class);
			Constraint constraint1 = query.descend("anrufer").constrain(tel);
			query.descend("datum").constrain(date).or(constraint1);
			ObjectSet<Anruf> set = query.execute();
			list.addAll(set);
		}
		
		return list;
	}
	
	private Long berechneKosten(Kunde k) {
		long kosten = 0;
		ArrayList<Anruf> anrufe = getAnrufeFromDb(k);

		for (Anruf a : anrufe) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(a.getDatum());
			if (
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
			) {
				//XXX
				System.out.println("we");
				kosten += a.getDauer() * 9; /* cent */
			} else {
				kosten += a.getDauer() * 19; /* cent */
			}
		}
		
		return kosten;
	}
}
