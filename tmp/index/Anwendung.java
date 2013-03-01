import com.db4o.*;
import com.db4o.query.*;
import com.db4o.config.*;
import java.util.*;

public class Anwendung {

	private static ObjectContainer db;

	// Constructors and Operations:
	public static final ObjectContainer getDb() {
		return db;
	}

	private static final void setDb(ObjectContainer db) {
		Anwendung.db = db;
	}

	public static void loescheAlleObjekteUndIndexe() {
		long zeit = System.nanoTime();
		getDb().close();
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(Chauffeur.class).objectField("name")
				.indexed(false);
		conf.common().objectClass(Chauffeur.class).objectField("alter")
				.indexed(false);
		setDb(Db4oEmbedded.openFile(conf, DB.TAXI));
		List<Object> rs = getDb().queryByExample(new Object());
		for (Object o : rs) {
			getDb().delete(o);
		}
		System.out.println();
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out
				.println("Alle Objekte und Indexe der Datenbank wurden gelöscht."
						+ " (Zeit " + zeit + " ms)");
	}

	public static void erzeugeUndSpeichereVieleChauffeure(int anz) {
		long zeit = System.nanoTime();
		Random r = new Random();
		int n = anz / 2;
		int m;
		String s;
		Chauffeur chauffeur;
		for (int i = 0; i < n; i++) {
			m = 3 + r.nextInt(12);
			s = "" + (char) ('A' + r.nextInt(24));
			for (int j = 3; j < m; j++) {
				s = s + (char) ('a' + r.nextInt(24));
			}
			getDb().store(chauffeur = new Chauffeur(s, 18 + r.nextInt(50)));
			// System.out.println (chauffeur);
		}
		chauffeur = new Chauffeur("Ballack", 29);
		getDb().store(chauffeur);
		System.out.println();
		System.out.println(chauffeur + " wurde gespeichert.");
		chauffeur = new Chauffeur("Kahn", 39);
		getDb().store(chauffeur);
		System.out.println(chauffeur + " wurde gespeichert.");
		n = anz - anz / 2 - 2;
		for (int i = 0; i < n; i++) {
			m = 3 + r.nextInt(12);
			s = "" + (char) ('A' + r.nextInt(24));
			for (int j = 3; j < m; j++) {
				s = s + (char) ('a' + r.nextInt(24));
			}
			getDb().store(chauffeur = new Chauffeur(s, 18 + r.nextInt(50)));
			// System.out.println (chauffeur);
		}
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println(anz + " Chauffeure wurden neu gespeichert."
				+ " (Zeit " + zeit + " ms)");
	}

	public static void findeQBEChauffeur(String name) {
		long zeit = System.nanoTime();
		List<Chauffeur> chauffeure = getDb().queryByExample(
				new Chauffeur(name, 0));
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println();
		System.out.println("QBE: Es wurden folgende Chauffeure mit Namen "
				+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Chauffeur chauffeur : chauffeure) {
			System.out.println(chauffeur);
		}
	}

	public static void findeNQChauffeur(String name) {
		final String nname = name;
		long zeit = System.nanoTime();
		List<Chauffeur> chauffeure = getDb().query(new Predicate<Chauffeur>() {
			public boolean match(Chauffeur chauffeur) {
				return chauffeur.getName().equals(nname);
			}
		});
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println();
		System.out.println("NQ: Es wurden folgende Chauffeure mit Namen "
				+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Chauffeur chauffeur : chauffeure) {
			System.out.println(chauffeur);
		}
	}

	public static void findeSODAChauffeur(String name) {
		long zeit = System.nanoTime();
		Query query = getDb().query();
		query.constrain(Chauffeur.class);
		query.descend("name").constrain("Kahn");
		ObjectSet chauffeure = query.execute();
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println();
		System.out.println("SODA: Es wurden folgende Chauffeure mit Namen "
				+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Object chauffeur : chauffeure) {
			System.out.println(chauffeur);
		}
	}

	public static void findeQBEChauffeurMitAlter(int alter) {
		long zeit = System.nanoTime();
		List<Chauffeur> chauffeure = getDb().queryByExample(
				new Chauffeur(null, alter));
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println();
		System.out.println("QBE: Es wurden folgende Chauffeure mit Alter "
				+ alter + " gefunden (Zeit " + zeit + " ms):");
		int j = 0;
		for (Chauffeur chauffeur : chauffeure) {
			if (j > 1) {
				System.out.println("     ...");
				break;
			}
			System.out.println(chauffeur);
			j++;
		}
	}

	public static void findeNQChauffeurMitAlter(int alter) {
		final int nalter = alter;
		long zeit = System.nanoTime();
		List<Chauffeur> chauffeure = getDb().query(new Predicate<Chauffeur>() {
			public boolean match(Chauffeur chauffeur) {
				return chauffeur.getAlter() == nalter;
			}
		});
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println();
		System.out.println("NQ: Es wurden folgende Chauffeure mit Alter "
				+ alter + " gefunden (Zeit " + zeit + " ms):");
		int j = 0;
		for (Chauffeur chauffeur : chauffeure) {
			if (j > 1) {
				System.out.println("     ...");
				break;
			}
			System.out.println(chauffeur);
			j++;
		}
	}

	public static void findeSODAChauffeurMitAlter(int alter) {
		long zeit = System.nanoTime();
		Query query = getDb().query();
		query.constrain(Chauffeur.class);
		query.descend("alter").constrain(39);
		ObjectSet chauffeure = query.execute();
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println();
		System.out.println("SODA: Es wurden folgende Chauffeure mit Alter "
				+ alter + " gefunden (Zeit " + zeit + " ms):");
		int j = 0;
		for (Object chauffeur : chauffeure) {
			if (j > 1) {
				System.out.println("     ...");
				break;
			}
			System.out.println(chauffeur);
			j++;
		}
	}

	public static void erzeugeIndex(String feld) {
		long zeit = System.nanoTime();
		getDb().close();
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(Chauffeur.class).objectField(feld)
				.indexed(true);
		setDb(Db4oEmbedded.openFile(conf, DB.TAXI));
		System.out.println();
		getDb().queryByExample(new Chauffeur(null, 0));
		zeit = (System.nanoTime() - zeit) / 1000 / 1000;
		System.out.println("Index über " + feld + " wurde erzeugt." + " (Zeit "
				+ zeit + " ms)");
	}

	public static void main(String[] arg) {
		setDb(Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB.TAXI));
		try {
			int anzChauffeure = 10000;
			loescheAlleObjekteUndIndexe();
			erzeugeUndSpeichereVieleChauffeure(anzChauffeure);
			getDb().close();
			setDb(Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
					DB.TAXI));
			findeQBEChauffeur("Kahn");
			System.out.println("Es wurde kein Index verwendet.");
			findeNQChauffeur("Kahn");
			System.out.println("Es wurde kein Index verwendet.");
			findeSODAChauffeur("Kahn");
			System.out.println("Es wurde kein Index verwendet.");
			erzeugeIndex("name");
			getDb().close();
			setDb(Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
					DB.TAXI));
			findeQBEChauffeur("Kahn");
			System.out.println("Es wurde der Index über name verwendet.");
			findeNQChauffeur("Kahn");
			System.out.println("Es wurde der Index über name verwendet.");
			findeSODAChauffeur("Kahn");
			System.out.println("Es wurde der Index über name verwendet.");

			loescheAlleObjekteUndIndexe();
			erzeugeUndSpeichereVieleChauffeure(anzChauffeure);
			getDb().close();
			setDb(Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
					DB.TAXI));
			findeQBEChauffeurMitAlter(39);
			System.out.println("Es wurde kein Index verwendet.");
			findeNQChauffeurMitAlter(39);
			System.out.println("Es wurde kein Index verwendet.");
			findeSODAChauffeurMitAlter(39);
			System.out.println("Es wurde kein Index verwendet.");
			erzeugeIndex("alter");
			getDb().close();
			setDb(Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
					DB.TAXI));
			findeQBEChauffeurMitAlter(39);
			System.out.println("Es wurde der Index über alter verwendet.");
			findeNQChauffeurMitAlter(39);
			System.out.println("Es wurde der Index über alter verwendet.");
			findeSODAChauffeurMitAlter(39);
			System.out.println("Es wurde der Index über alter verwendet.");
			loescheAlleObjekteUndIndexe();
		} finally {
			getDb().close();
		}
	}

} // end Anwendung
