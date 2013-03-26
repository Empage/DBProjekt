package de.hauschil.dbprojekt.anwendungsfaelle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.Index;
import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

// TODO getrennte Listen für ein- und ausgehende Kontakte
public class Fall4 {
	private long[] anfangszeit;
	private long[] endzeit;
	private DB_Controller db;
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

		//TODO anzahl
		Kunde[] kunden = getKundenFromDb(1);
		anfangszeit[indexed ? 1 : 0] = System.nanoTime();
		for (Kunde k : kunden) {
			System.out.println(k);
			for (Anruf a : getAnrufeFromDb(k, true)) {
				kontakte_angerufene.add(getKundeByNumber(help, a.getAngerufener()));
//				Kunde kontakt = db.getKundeByNumber(a.getAngerufener().toString());
//				if (!kontakte.contains(kontakt)) {
//					kontakte.add(kontakt);
//				}
			}
			for (Anruf a : getAnrufeFromDb(k, false)) {
				kontakte_anrufer.add(getKundeByNumber(help, a.getAnrufer()));
			}
		}
		
		System.out.println(kontakte_angerufene);
		System.out.println(kontakte_anrufer);
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
	
	private Kunde[] getKundenFromDb(int count) {
		Random r = new Random(1337);
		Kunde[] k = new Kunde[count];
		
		ArrayList<Kunde> list = db.getKunden(null, null);
		
		for (int i = 0; i < count; i++) {
			k[i] = list.get(r.nextInt(list.size()));
		}
		
		return k;
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
