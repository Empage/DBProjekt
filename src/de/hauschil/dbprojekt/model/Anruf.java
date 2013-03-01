package de.hauschil.dbprojekt.model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import de.hauschil.dbprojekt.controller.DB4O_Controller;
import de.hauschil.dbprojekt.controller.DB_Controller;
import static de.hauschil.dbprojekt.controller.Main.*;

public class Anruf {
	private static Random r = new Random(42);

	private Telefon anrufer;
	private Telefon angerufener;
	
	private int dauer;
	private Date datum;
	
	public Anruf(Telefon anrufer, Telefon angerufener, Date datum, int dauer) {
		this.anrufer = anrufer;
		this.angerufener = angerufener;
		this.datum = datum;
		this.dauer = dauer;
	}
	
	public static Anruf[] generateAnrufe(Kunde[] kunden, int anzahlProMonat, DB_Controller db) {
		Anruf[] an = new Anruf[kunden.length * anzahlProMonat * 12];
		
		/* mache fuer jeden Kunden folgendes */
		for (int i = 0; i < kunden.length; i++) {
			Kunde[] partner = new Kunde[ANZ_PARTNER];
			/* Generiere erst die Partner */
			for (int j = 0; j < ANZ_PARTNER; j++) {
				partner[j] = kunden[r.nextInt(kunden.length)];
			}
			/* jeden Monat */
			for (int month = 0; month < 12; month++) {
				/* Nun generiere die Anrufe pro Monat */
				for (int j = 0; j < anzahlProMonat; j++) {
					/* Jeder Kunde 12 Monate * jeden Monat anzahlProMonat */
					an[i * 12 * anzahlProMonat + month * anzahlProMonat + j] = new Anruf(
						kunden[i].getTelefone().get(0),
						partner[r.nextInt(partner.length)].getTelefone().get(0),
						generateDate(month),
						r.nextInt(3600)
					);
				}
			}
			//TODO hier db zeugs
		}
		return an;
	}

	@Override
	public String toString() {
		return "Anruf [anrufer=" + anrufer + ", angerufener=" + angerufener
				+ ", dauer=" + dauer + ", datum=" + datum + "]";
	}
	
	/* Ein random Datum aus 2012 generieren */
	private static Date generateDate(int month) {
		GregorianCalendar cal = new GregorianCalendar(2012, month, 1);
		cal.set(Calendar.DAY_OF_MONTH, r.nextInt(cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
		cal.set(Calendar.HOUR_OF_DAY, r.nextInt(24));
		cal.set(Calendar.MINUTE, r.nextInt(60));
		cal.set(Calendar.SECOND, r.nextInt(60));
		
		return cal.getTime();
	}
}
