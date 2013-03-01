package de.hauschil.dbprojekt.model;

import java.util.Date;
import java.util.Random;

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
	
	public static Anruf[] generateAnrufe(Kunde[] k, int faktor) {
		Anruf[] an = new Anruf[k.length * faktor * 3];
		long cur_millis = new Date().getTime();
		
		/* mache fuer jeden Kunden folgendes */
		for (int i = 0; i < k.length; i++) {
			Kunde[] partner = new Kunde[faktor];
			/* Generiere erst die Partner */
			for (int j = 0; j < partner.length; j++) {
				partner[j] = k[r.nextInt(k.length)];
			}
			/* Nun generiere die Anrufe */
			for (int j = 0; j < faktor * 3; j++) {
				an[i * faktor * 3 + j] = new Anruf(
					k[i].getTelefone().get(0),
					partner[r.nextInt(partner.length)].getTelefone().get(0),
					/* Ein Datum aus dem letzten Jahr generieren */
					new Date(cur_millis - Math.abs(r.nextLong() % (1000L * 60L * 60L * 24L * 365L))),
					r.nextInt(3600)
				);
			}
		}
		return an;
	}

	@Override
	public String toString() {
		return "Anruf [anrufer=" + anrufer + ", angerufener=" + angerufener
				+ ", dauer=" + dauer + ", datum=" + datum + "]";
	}
}
