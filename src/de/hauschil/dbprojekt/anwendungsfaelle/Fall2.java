package de.hauschil.dbprojekt.anwendungsfaelle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.Index;
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
		db.initDBConnection(new Index[] {
			new Index(Anruf.class, "anrufer", indexed), 
			new Index(Anruf.class, "datum", indexed)
		});
		/* null, null um keine Constraints zu haben und damit ALLE Kunden zu bekommen */
		ArrayList<Kunde> kunden = db.getKunden(null, null);
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
	
	public ArrayList<Anruf> getAnrufeFromDb(Kunde k, int month) {
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.set(2012, month, 1, 0, 0, 0);
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.set(2012, month, cal1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
		ArrayList<Anruf> list = new ArrayList<>();
		
		for (Telefon tel : k.getTelefone()) {
			list.addAll(db.getAnrufe(tel, null, cal1.getTimeInMillis(), cal2.getTimeInMillis()));
		}
		
		return list;
	}
	
	private Long berechneKosten(Kunde k) {
		long kosten = 0;
		ArrayList<Anruf> anrufe = getAnrufeFromDb(k, Calendar.MAY);
		
		for (Anruf a : anrufe) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis(a.getDatum());
			/* Am Wochenende ist Telefonieren günstiger */
			if (
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
			) {
				/* Wenn beide die gleiche Vorwahl haben, gibts auch Rabatt */
				if (getVorwahl(a.getAnrufer()).equals(getVorwahl(a.getAngerufener()))) {
					kosten += a.getDauer() / 60 * 4; /* cent/min */
				} else {
					kosten += a.getDauer() / 60 * 9; /* cent/min */
				}
			} else {
				/* Wenn beide die gleiche Vorwahl haben, gibts auch Rabatt */
				if (getVorwahl(a.getAnrufer()).equals(getVorwahl(a.getAngerufener()))) {
					kosten += a.getDauer() / 60 * 10; /* cent/min */
				} else {
					kosten += a.getDauer() / 60 * 19; /* cent/min */
				}
			}
		}
		
		return kosten;
	}
	
	private static String getVorwahl(Telefon t) {
		return t.toString().split("/")[0];
	}
}
