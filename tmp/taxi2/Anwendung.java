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
		Chauffeur	chauffeur = new Chauffeur ("Ballack", 31);
		getDb ().store (chauffeur);	//	Chauffeur ist jetzt schon in der Datenbank.
		Taxi	taxi = new Taxi ("BMW", "13", chauffeur);
		getDb ().store (taxi);
		
		chauffeur = new Chauffeur ("Kahn", 39);
		taxi = new Taxi ("VW", "1", chauffeur);
		Taxi	taxi2 = new Taxi ("VW", "1", chauffeur);
		getDb ().store (taxi);	//	Chauffeur ist hiermit auch in der Datenbank.
		getDb ().store (taxi2);	//	Chauffeur ist nicht noch mal in DB.
		
		getDb ().store (new Taxi ("Opel", "33", new Chauffeur ("Gomez", 26)));
		
		System.out.println ("Alle Taxen in der Datenbank:");
		List<Taxi>	resultat = getDb ().query (Taxi.class);
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("und alle Objekte in der Datenbank:");
		resultat = getDb ().queryByExample (new Object ());
		DB.zeigeResultat (resultat);
		}

	public static void	findeTaxenQBE ()
		{
		System.out.println ();
		System.out.println ("Alle Taxen mit Chauffeur Gomez (QBE):");
		Chauffeur	prototypChauffeur = new Chauffeur ("Gomez", 0);
		Taxi	prototypTaxi = new Taxi (null, null, prototypChauffeur);
		List<Taxi>	resultat = getDb ().queryByExample (prototypTaxi);
		DB.zeigeResultat (resultat);
		}

	public static void	findeTaxenNQ ()
		{
		System.out.println ();
		System.out.println ("Alle Taxen mit Chauffeur Gomez (NQ):");
		final String name = "Gomez";
		List<Taxi>	resultat = getDb ().query (new Predicate<Taxi> ()
			{
			public boolean	match (Taxi taxi)
				{
				return taxi.getChauffeur ().getName ().equals (name);
				}
			});
		DB.zeigeResultat (resultat);
		}

	public static void	findeTaxenSODA ()
		{
		System.out.println ();
		System.out.println ("Alle Taxen mit Chauffeur Gomez (SODA):");
		Query	query	= getDb ().query ();
		query.constrain (Taxi.class);
		query.descend ("aChauffeur").descend ("name")
			.constrain ("Gomez");
		List	resultat = query.execute ();
		DB.zeigeResultat (resultat);
		}

	public static void	findeChauffeureSODA ()
		{
		System.out.println ();
		System.out.println ("Alle Chauffeure mit Taxi BMW (SODA):");
		Query	taxiQuery = getDb ().query ();
		taxiQuery.constrain (Taxi.class);
		taxiQuery.descend ("modell").constrain ("BMW");
		Query	chauffeurQuery = taxiQuery.descend ("aChauffeur");
		List	resultat = chauffeurQuery.execute ();
		//taxiQuery.descend ("modell").constrain ("BMW").descend ("aChauffeur");
		//List	resultat = taxiQuery.execute ();
		DB.zeigeResultat (resultat);
		}

	public static void	wechsleChauffeur ()
		{
		List<Taxi>	resultat = getDb ().queryByExample (new Taxi ("BMW", null, null));
		Taxi	taxi = resultat.get (0);
		List<Chauffeur>	res = getDb ().queryByExample (new Chauffeur ("Gomez", 0));
		Chauffeur	chauffeur = res.get (0);
		System.out.println ();
		System.out.println (taxi);
		System.out.println ("   bekommt jetzt " + chauffeur);
		taxi.setChauffeur (chauffeur);
		getDb ().store (taxi);	//	Taxi wird zurückgespeichert.
		
		System.out.println ();
		System.out.println ("Hat das funktioniert? Check mit einer"
			+ " neuen DB-Verbindung:");
		getDb ().close ();
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		System.out.println ("   Alle Taxen in der Datenbank:");
		List<Taxi>	res2 = getDb ().query (Taxi.class);
		DB.zeigeResultat (res2);
		}

	public static void	aendereNamenDesChauffeurs ()
		{
		List<Taxi>	resultat = getDb ().queryByExample (new Taxi ("BMW", null, null));
		Taxi	taxi = resultat.get (0);
		System.out.println ();
		System.out.println (taxi);
		String	name = "Strauss-Kahn";
		System.out.println ("   Der Chauffeur bekommt den Namen " + name);
		taxi.getChauffeur ().setName (name);
		getDb ().store (taxi);	//	Taxi wird zurückgespeichert.
		
		System.out.println ();
		System.out.println ("Hat das funktioniert? Check mit der"
			+ " alten DB-Verbindung:");
		System.out.println ("   Alle Taxen in der Datenbank:");
		List<Taxi>	res2 = getDb ().query (Taxi.class);
		DB.zeigeResultat (res2);
		
		System.out.println ();
		System.out.println ("Hat das wirklich funktioniert? Check mit einer"
			+ " neuen DB-Verbindung:");
		getDb ().close ();
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		System.out.println ("   Alle Taxen in der Datenbank:");
		res2 = getDb ().query (Taxi.class);
		DB.zeigeResultat (res2);
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
			findeTaxenQBE ();
			findeTaxenNQ ();
			findeTaxenSODA ();
			findeChauffeureSODA ();
			wechsleChauffeur ();
			aendereNamenDesChauffeurs ();
			alleObjekteLoeschen ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
