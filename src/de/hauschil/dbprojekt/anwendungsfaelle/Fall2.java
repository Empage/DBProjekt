package de.hauschil.dbprojekt.anwendungsfaelle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
	
	public Fall2(DB_Controller db) {
		this.db = db;
		anfangszeit = new long[2];
		endzeit = new long[2];
	}
	
	public void run(boolean indexed) {
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(Anruf.class).objectField("anrufer").indexed(indexed);
		conf.common().objectClass(Anruf.class).objectField("datum").indexed(indexed);
		db.initDBConnection(conf);
		
		ArrayList<Kunde> kunden = getAllKundenFromDb();
		ArrayList<Long> kosten = new ArrayList<>(kunden.size());
	
		anfangszeit[indexed ? 1 : 0] = System.nanoTime();

		for (int i = 0; i < kunden.size(); i++) {
			kosten.add(i, berechneKosten(kunden.get(i)));
		}

		db.closeDBConncetion();
		endzeit[indexed ? 1 : 0] = System.nanoTime();
	}
	
	public long getTime(int which) {
		return (endzeit[which] - anfangszeit[which]) / 1000 / 1000;
	}

	@Override
	public String toString() {
		return "Fall2 ohne Index: " + getTime(0) + " ms\n" +
			   "Fall2  mit Index: " + getTime(1) + " ms";
	}
	
	private ArrayList<Kunde> getAllKundenFromDb() {
		ArrayList<Kunde> list = new ArrayList<>();
		Query query = db.query();
		query.constrain(Kunde.class);
		ObjectSet<Kunde> set = query.execute();
		list.addAll(set);
		
		return list;
	}
	
	public ArrayList<Anruf> getAnrufeFromDb(Kunde k, int month) {
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.set(2012, month, 1, 0, 0, 0);
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.set(2012, month, cal1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
		ArrayList<Anruf> list = new ArrayList<>();
		
		for (Telefon tel : k.getTelefone()) {
			Query query = db.query();
			query.constrain(Anruf.class);
			Constraint constraint1 = query.descend("anrufer").constrain(tel);
			Constraint constraint2 = query.descend("datum").constrain(cal1.getTimeInMillis()).greater();
			query.descend("datum").constrain(cal2.getTimeInMillis()).smaller().and(constraint2).and(constraint1);
			ObjectSet<Anruf> set = query.execute();
			list.addAll(set);
		}
		
		return list;
	}
	
	//TODO evtl noch gleiche Vorwahl berücksichtigen im Preis
	private Long berechneKosten(Kunde k) {
		long kosten = 0;
		ArrayList<Anruf> anrufe = getAnrufeFromDb(k, Calendar.MAY);
		
		for (Anruf a : anrufe) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis(a.getDatum());
			if (
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
			) {
				kosten += a.getDauer() / 60 * 9; /* cent/min */
			} else {
				kosten += a.getDauer() / 60 * 19; /* cent/min */
			}
		}
		
		return kosten;
	}
}
