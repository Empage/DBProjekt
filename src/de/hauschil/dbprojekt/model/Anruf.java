package de.hauschil.dbprojekt.model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import de.hauschil.dbprojekt.controller.DB4O_Controller;
import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.HSQL_Controller;
import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.*;

public class Anruf {
	private static Random r = new Random(42);

	private Telefon anrufer;
	private Telefon angerufener;
	private int dauer;
	private long datum;
	
	public Anruf(Telefon anrufer, Telefon angerufener, long datum, int dauer) {
		this.anrufer = anrufer;
		this.angerufener = angerufener;
		this.datum = datum;
		this.dauer = dauer;
	}
		
	public Telefon getAnrufer() {
		return anrufer;
	}
	public void setAnrufer(Telefon anrufer) {
		this.anrufer = anrufer;
	}
	public Telefon getAngerufener() {
		return angerufener;
	}
	public void setAngerufener(Telefon angerufener) {
		this.angerufener = angerufener;
	}
	public int getDauer() {
		return dauer;
	}
	public void setDauer(int dauer) {
		this.dauer = dauer;
	}
	public long getDatum() {
		return datum;
	}
	public void setDatum(long datum) {
		this.datum = datum;
	}
	
	@Override
	public String toString() {
		return "Anruf [anrufer=" + anrufer + ", angerufener=" + angerufener
				+ ", dauer=" + dauer + ", datum=" + datum + "]";
	}

	public static void generateAnrufe(Kunde[] kunden, int anzahlProMonat, DB_Controller db) {
		float fortschritt = 2.0f;
		System.out.println("                     10   20   30   40   50   60   70   80   90  100");
		System.out.print("Generiere Anrufe: ");
		Anruf[] an = new Anruf[anzahlProMonat * 12];
		Kunde[] partner = new Kunde[ANZ_PARTNER];
		int next_partner;
		int next_partner_telefon;
		int next_telefon;
		/* mache fuer jeden Kunden folgendes */
		for (int i = 0; i < kunden.length; i++) {
			/* Generiere erst die Partner */
			for (int j = 0; j < ANZ_PARTNER; j++) {
				partner[j] = kunden[r.nextInt(kunden.length)];
			}
			/* jeden Monat */
			for (int month = 0; month < 12; month++) {
				/* Nun generiere die Anrufe pro Monat */
				for (int j = 0; j < anzahlProMonat; j++) {
					next_partner = r.nextInt(partner.length);
					next_partner_telefon = r.nextInt(partner[next_partner].getTelefone().size());
					next_telefon = r.nextInt(kunden[i].getTelefone().size());
					an[month * anzahlProMonat + j] = new Anruf(
						kunden[i].getTelefone().get(next_telefon),
						partner[next_partner].getTelefone().get(next_partner_telefon),
						generateDate(month),
						r.nextInt(600)
					);
				}
			}
			db.storeAnrufe(an);
			if (i % 100 == 99) {
				db.commit();
			}
			if (i % 1000 == 999) {
				db.closePStatement();
			}
			if (((float) i / kunden.length * 100) > fortschritt) {
				fortschritt += 2;
				System.out.print("+");
			}
		}
		db.closePStatement();
		System.out.println();
		System.out.println(anzahlProMonat * 12 * ANZ_KUNDEN + " Anrufe generiert");
	}
	
	/* Ein random Datum aus 2012 generieren */
	private static long generateDate(int month) {
		GregorianCalendar cal = new GregorianCalendar(2012, month, 1);
		cal.set(Calendar.DAY_OF_MONTH, r.nextInt(cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
		cal.set(Calendar.HOUR_OF_DAY, r.nextInt(24));
		cal.set(Calendar.MINUTE, r.nextInt(60));
		cal.set(Calendar.SECOND, r.nextInt(60));
		
		return cal.getTimeInMillis();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((angerufener == null) ? 0 : angerufener.hashCode());
		result = prime * result + ((anrufer == null) ? 0 : anrufer.hashCode());
		result = prime * result + (int) (datum ^ (datum >>> 32));
		result = prime * result + dauer;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Anruf other = (Anruf) obj;
		if (angerufener == null) {
			if (other.angerufener != null)
				return false;
		} else if (!angerufener.equals(other.angerufener))
			return false;
		if (anrufer == null) {
			if (other.anrufer != null)
				return false;
		} else if (!anrufer.equals(other.anrufer))
			return false;
		if (datum != other.datum)
			return false;
		if (dauer != other.dauer)
			return false;
		return true;
	}
}
