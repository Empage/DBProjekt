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
		at.add (new LastTaxi ("VW", "1", null, 1.6));
		at.add (new LastTaxi ("Mercedes", "32", null, 2.1));
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
		List<Object>	resultat2 = getDb ().queryByExample (new Object ());
		DB.zeigeResultat (resultat2);
		}

	public static void	findeLastTaxiQBE ()
		{
		System.out.println ();
		System.out.println ("Alle Lasttaxi (QBE):");
		List<LastTaxi>	resultat = getDb ().query (LastTaxi.class);
		DB.zeigeResultat (resultat);
		}

	public static void	findeAlleFahrtenNQ ()
		{
		System.out.println ();
		System.out.print ("Alle Fahrten von Gomez mit einem LastTaxi");
		System.out.println (" (NQ):");
		List<Fahrt>	resultat = getDb ().query (new Predicate<Fahrt> ()
			{
			public boolean	match (Fahrt fahrt)
				{
				Chauffeur	gomez 
					= (Chauffeur) getDb ().queryByExample (new Chauffeur ("Gomez", 0)).next ();
				if (fahrt.getChauffeur () == gomez)
					{
					List<LastTaxi> lt = getDb ().query (LastTaxi.class);
					for (LastTaxi t : lt)
						{
						if (t.getFahrten ().contains (fahrt)) return true;
						}
					}
				return false;
				}
			});
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
			findeLastTaxiQBE ();
			findeAlleFahrtenNQ ();
			alleObjekteLoeschen ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
