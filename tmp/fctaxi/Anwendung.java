import	com.db4o.*;
import	com.db4o.query.*;
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

	public static void	erzeugeChauffeureUndTaxen ()
		{
		ArrayList<Chauffeur> ac = new ArrayList<Chauffeur> ();
		ac.add (new Chauffeur ("Ballack", 31));
		ac.add (new Chauffeur ("Kahn", 39));
		ac.add (new Chauffeur ("Gomez", 22));
		ac.add (new Chauffeur ("Klose", 29));
		ArrayList<Taxi> at = new ArrayList<Taxi> ();
		at.add (new Taxi ("BMW", "13", null));
		at.add (new Taxi ("VW", "1", null));
		at.add (new Taxi ("Mercedes", "32", null));
		at.add (new Taxi ("Opel", "11", null));
		Random	r = new Random ();
		for (Taxi t : at)
			{
			int	n = r.nextInt (5);
			for (int i = 0; i <= n; i++)
				{
				t.setChauffeur (ac.get (r.nextInt (ac.size ())));
				t.erzeugeFahrt ();
				}
			}
		int	j = 0;
		for (Taxi t : at)
			{
			t.setChauffeur (ac.get (j));
			j = (++j) % ac.size ();
			getDb ().store (t);	//	Alles wird gespeichert.
			}
		
		System.out.println ("Alle Taxen in der Datenbank:");
		List<Taxi>	resultat = getDb ().query (Taxi.class);
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("und alle Objekte in der Datenbank:");
		List<Object> resultat2 = getDb ().queryByExample (new Object ());
		DB.zeigeResultat (resultat2);
		}

	public static void	findeAlleFahrtenQBE ()
		{
		System.out.println ();
		System.out.println ("Alle Fahrten mit Klose (QBE):");
		Chauffeur	prototypChauffeur = new Chauffeur ("Klose", 0);
		Fahrt	prototypFahrt = new Fahrt (null, null, null, prototypChauffeur);
		List<Fahrt>	resultat = getDb ().queryByExample (prototypFahrt);
		DB.zeigeResultat (resultat);
		}

	public static void	findeAlleFahrtenNQ ()
		{
		System.out.println ();
		System.out.println ("Alle Fahrten, bei denen die Geschwindigkeit");
		System.out.println ("   unter 40 oder über 80 km/h liegt (NQ):");
		List<Fahrt>	resultat = getDb ().query (new Predicate<Fahrt> ()
			{
			public boolean	match (Fahrt fahrt)
				{
				double	v = fahrt.getEntfernung ()[0]
					/ ((fahrt.getEnde ().getTime () 
						- fahrt.getBeginn ().getTime ())
						/1000.0/60.0/60.0);
				return v < 40.0 || v > 80.0;
				}
			});
		DB.zeigeResultat (resultat);
		}

	public static void	findeAlleFahrtenSODA ()
		{
		System.out.println ();
		System.out.println ("Alle Fahrten mit Klose (SODA):");
		Query	fahrtQuery = getDb ().query ();
		fahrtQuery.constrain (Fahrt.class);
		fahrtQuery.descend ("aChauffeur")
			.descend ("name").constrain ("Klose");
		List	resultat = fahrtQuery.execute ();
		DB.zeigeResultat (resultat);
		}

	public static void	alleObjekteLoeschen ()
		{
		List<Object>	resultat 
			= getDb ().queryByExample (new Object ());
		for (Object o : resultat)
			{
			getDb ().delete (o);
			}
		resultat = getDb ().queryByExample (new Object ());
		DB.zeigeResultat (resultat);
		}

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			alleObjekteLoeschen ();
			erzeugeChauffeureUndTaxen ();
			findeAlleFahrtenQBE ();
			findeAlleFahrtenNQ ();
			findeAlleFahrtenSODA ();
			alleObjekteLoeschen ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
