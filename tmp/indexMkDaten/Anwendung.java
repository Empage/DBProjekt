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
		EmbeddedConfiguration	conf = Db4oEmbedded.newConfiguration ();
		conf.common ().objectClass (Chauffeur.class)
			.objectField ("name").indexed (false);
		conf.common ().objectClass (Chauffeur.class)
			.objectField ("alter").indexed (false);
		setDb (Db4oEmbedded.openFile (conf, DB.TAXI));
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

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			int	anzChauffeure = 10000;
			loescheAlleObjekteUndIndexe ();
			erzeugeUndSpeichereVieleChauffeure (anzChauffeure);
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
