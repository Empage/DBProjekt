package de.hauschil.dbprojekt.anwendungsfaelle;

import java.util.ArrayList;
import java.util.HashSet;

import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.Index;
import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

public class Fall4 {
	private long[] anfangszeit;
	private long[] endzeit;
	private DB_Controller db;
	/* Anzahl zu überprüfender Kontakte */
	private final int anz = 1;
	/* Kontakte, die ans Bundeskriminalamt übergeben werden können */
	private HashSet<Kunde> kontakte_anrufer = new HashSet<>();
	private HashSet<Kunde> kontakte_angerufene = new HashSet<>();
	
	public Fall4(DB_Controller db) {
		this.db = db;
		anfangszeit = new long[2];
		endzeit = new long[2];
	}
	
	public void run(boolean indexed) {
		db.initDBConnection(new Index[] {
			new Index(Anruf.class, "anrufer", indexed), 
			new Index(Anruf.class, "angerufener", indexed)
		});
		/* Alle Kunden mit Telefonnummern holen, um schnell das Mapping von Nummer auf Kunde hinzubekommen */
		ArrayList<Kunde> help = db.getKunden(null, null);
		/* beziehe zu überprüfende Kunden aus help-Liste */
		Kunde[] kunden = new Kunde[anz];
		for (int i = 0; i < anz; i++) {
			kunden[i] = help.get(i);
		}
		
		anfangszeit[indexed ? 1 : 0] = System.nanoTime();
		for (Kunde k : kunden) {
			for (Anruf a : getAnrufeFromDb(k, true)) {
				kontakte_angerufene.add(getKundeByNumber(help, a.getAngerufener()));
			}
			for (Anruf a : getAnrufeFromDb(k, false)) {
				kontakte_anrufer.add(getKundeByNumber(help, a.getAnrufer()));
			}
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
	
	private Kunde getKundeByNumber(ArrayList<Kunde> kunden, Telefon tel) {
		for (Kunde k : kunden) {
			if (k.getTelefone().contains(tel)) {
				return k;
			}
		}
		return null;
	}
	
	public ArrayList<Anruf> getAnrufeFromDb(Kunde k, boolean anrufer) {
		ArrayList<Anruf> list = new ArrayList<>();
		if (anrufer) {
			for (Telefon tel : k.getTelefone()) {
				list.addAll(db.getAnrufe(tel, null, null, null));
			}
		} else {
			for (Telefon tel : k.getTelefone()) {
				list.addAll(db.getAnrufe(null, tel, null, null));
			}
		}
		return list;
	}
}
