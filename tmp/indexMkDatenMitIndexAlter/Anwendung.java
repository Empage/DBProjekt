import	com.db4o.*;
import	com.db4o.query.*;
import	com.db4o.config.*;
import	java.util.*;

public class	Anwendung
	{

	private static ObjectContainer	db;

	//	Constructors and Operations:
	public static final ObjectContainer	getDb ()
		{
		return db;
		}

	private static final void	setDb (ObjectContainer db)
		{
		Anwendung.db = db;
		}

	public static void	loescheAlleObjekteUndIndexe ()
		{
		long	zeit = System.nanoTime ();
		getDb ().close ();
		Configuration	conf = Db4o.newConfiguration ();
		conf.objectClass (Chauffeur.class)
			.objectField ("name").indexed (false);
		conf.objectClass (Chauffeur.class)
			.objectField ("alter").indexed (false);
		setDb (Db4o.openFile (conf, DB.TAXI));
		List<Object>	rs = getDb ().queryByExample (new Object ());
		for (Object o : rs) { getDb ().delete (o); }
		System.out.println ();
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ("Alle Objekte und Indexe der Datenbank wurden gelöscht."
			+ " (Zeit " + zeit + " ms)");
		}

	public static void	erzeugeUndSpeichereVieleChauffeure (int anz)
		{
		long	zeit = System.nanoTime ();
		Random	r = new Random ();
		int	n = anz / 2;
		int	m;
		String	s;
		Chauffeur	chauffeur;
		for (int i = 0; i < n; i++)
			{
			m = 3 + r.nextInt (12);
			s = "" + (char) ('A' + r.nextInt (24));
			for (int j = 3; j < m; j++)
				{
				s = s + (char) ('a' + r.nextInt (24));
				} 
			getDb ().store (chauffeur = new Chauffeur (s, 18 + r.nextInt (50)));
			//System.out.println (chauffeur);
			}
		chauffeur = new Chauffeur ("Ballack", 29);
		getDb ().store (chauffeur);
		System.out.println ();
		System.out.println (chauffeur + " wurde gespeichert.");
		chauffeur = new Chauffeur ("Kahn", 39);
		getDb ().store (chauffeur);
		System.out.println (chauffeur + " wurde gespeichert.");
		n = anz - anz / 2 - 2;
		for (int i = 0; i < n; i++)
			{
			m = 3 + r.nextInt (12);
			s = "" + (char) ('A' + r.nextInt (24));
			for (int j = 3; j < m; j++)
				{
				s = s + (char) ('a' + r.nextInt (24));
				} 
			getDb ().store (chauffeur = new Chauffeur (s, 18 + r.nextInt (50)));
			//System.out.println (chauffeur);
			}
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println (anz + " Chauffeure wurden neu gespeichert."
			+ " (Zeit " + zeit + " ms)");
		}

	public static void	findeQBEChauffeur (String name)
		{
		long	zeit = System.nanoTime ();
		List<Chauffeur>	chauffeure 
			= getDb ().queryByExample (new Chauffeur (name, 0));
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ();
		System.out.println ("QBE: Es wurden folgende Chauffeure mit Namen "
			+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Chauffeur	chauffeur : chauffeure)
			{
			System.out.println (chauffeur);
			}
		}

	public static void	findeNQChauffeur (String name)
		{
		final	String	nname = name;
		long	zeit = System.nanoTime ();
		List<Chauffeur>	chauffeure = getDb ().query (
			new Predicate<Chauffeur> ()
				{
				public boolean	match (Chauffeur chauffeur)
					{
					return chauffeur.getName ().equals (nname);
					}
				});
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ();
		System.out.println ("NQ: Es wurden folgende Chauffeure mit Namen "
			+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Chauffeur	chauffeur : chauffeure)
			{
			System.out.println (chauffeur);
			}
		}

	public static void	erzeugeIndex (String feld)
		{
		long	zeit = System.nanoTime ();
		getDb ().close ();
		Configuration	conf = Db4o.newConfiguration ();
		conf.objectClass (Chauffeur.class)
			.objectField (feld).indexed (true);
		setDb (Db4o.openFile (conf, DB.TAXI));
		System.out.println ();
		getDb ().queryByExample (new Chauffeur (null, 0));
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ("Index über " + feld + " wurde erzeugt."
			+ " (Zeit " + zeit + " ms)");
		}

	public static void	main (String[] arg)
		{
		setDb (Db4o.openFile (DB.TAXI));
		try
			{
			int	anzChauffeure = 10000;
			loescheAlleObjekteUndIndexe ();
			erzeugeUndSpeichereVieleChauffeure (anzChauffeure);
			erzeugeIndex ("alter");
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
