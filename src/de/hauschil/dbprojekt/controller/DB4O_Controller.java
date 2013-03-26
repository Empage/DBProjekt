package de.hauschil.dbprojekt.controller;

import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.DB4O_PATH;

import java.util.ArrayList;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

public class DB4O_Controller implements DB_Controller {
	private static ObjectContainer db;
	
	@Override
	public void initDBConnection(Index... indizes) {
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.file().blockSize(8);
		for (Index i : indizes) {
			conf.common().objectClass(i.getIndexClass()).objectField(i.getIndexField()).indexed(i.isIndexed());
		}
		db = Db4oEmbedded.openFile(conf, DB4O_PATH);
	}

	@Override
	public void commit() {
		db.commit();
	}

	@Override
	public void closeDBConncetion() {
		db.close();
	}

	@Override
	public void createTables() {
		/* wird nicht benötigt für db4o */
	}

	@Override
	public void dropTables() {
		/* wird nicht benötigt für db4o */
	}

	@Override
	public void storeKunden(Kunde[] k) {
		db.store(k);
	}

	@Override
	public void storeAnrufe(Anruf[] a) {
		db.store(a);
	}

	@Override
	public void closePStatement() {
		/* wird nicht benötigt für db4o */
	}

	@Override
	public ArrayList<Kunde> getKunden(String vorname, String nachname) {
		ArrayList<Kunde> k = new ArrayList<>();
		
		Query query = db.query();
		query.constrain(Kunde.class);
		if (vorname != null) {
			query.descend("vorname").constrain(vorname);
		}
		if (nachname != null) {
			query.descend("nachname").constrain(nachname);
		}

		ObjectSet<Kunde> set = query.execute();
		k.addAll(set);
		
		return k;
	}

	/* d1 unteres Datum, d2 oberes Datum */
	@Override
	public ArrayList<Anruf> getAnrufe(Telefon anrufer, Telefon angerufener, Long d1, Long d2) {
		ArrayList<Anruf> list = new ArrayList<>();
		Query query = db.query();
		System.out.println("hier4");
		query.constrain(Anruf.class);
		System.out.println("hier5");
		/* Fall2 */
		if (anrufer != null && angerufener == null && d1 != null && d2 != null) {
			Constraint constraint1 = query.descend("anrufer").constrain(anrufer);
			Constraint constraint2 = query.descend("datum").constrain(d1.longValue()).greater();
			query.descend("datum").constrain(d2.longValue()).smaller().and(constraint2).and(constraint1);
		/* Fall3 */
		} else if (anrufer == null && angerufener == null && d1 != null && d2 != null) {
			Constraint constraint2 = query.descend("datum").constrain(d1.longValue()).greater();
			query.descend("datum").constrain(d2.longValue()).smaller().and(constraint2);
		/* Fall 4 */
		} else if (anrufer != null && angerufener == null && d1 == null && d2 == null) {
			//XXX
			System.out.println("hier2");
			query.descend("anrufer").constrain(anrufer);
//			query.descend("angerufener").constrain(angerufener).or(constraint1);
		/* Fall 4 */
		} else if (anrufer == null && angerufener != null && d1 == null && d2 == null) { 
//			query.descend("anrufer").constrain(anrufer);
			query.descend("angerufener").constrain(angerufener);
		} else {
			throw new RuntimeException("TODO getAnrufe");
		}
			System.out.println("hier3");
		ObjectSet<Anruf> set = query.execute();
		list.addAll(set);

		return list;
	}

	@Override
	public void deleteAnrufe(long datum1, long datum2) {
		ArrayList<Anruf> list = getAnrufe(null, null, datum1, datum2);
	
		for (Anruf a : list) {
			db.delete(a);
		}
	}

	@Override
	public Kunde getKundeByNumber(String number) {
		Kunde k;
		
		Query query = db.query();
		query.constrain(Kunde.class);
		query.descend("telefone").descend("nummer").constrain(number);

		ObjectSet<Kunde> set = query.execute();
		k = set.get(0);
		
		return k;
	}
	
	@Override
	public String toString() {
		return "db4o-Controller!";
	}
}
