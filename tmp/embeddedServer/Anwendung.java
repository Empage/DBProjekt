import	com.db4o.*;
import	com.db4o.query.*;
import	com.db4o.cs.*;
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
		getDb ().store (taxi);	//	Chauffeur ist hiermit auch in der Datenbank.
		
		getDb ().store (new Taxi ("Mercedes", "32", new Chauffeur ("Gomez", 22)));
		
		System.out.println ("Alle Taxen in der Datenbank:");
		List<Taxi>	resultat = getDb ().query (Taxi.class);
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("und alle Objekte in der Datenbank:");
		resultat = getDb ().queryByExample (new Object ());
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
			}
			finally
				{
				getDb ().close ();
				}
		
		//	Embedded Server:
		ObjectServer	server = Db4oClientServer.openServer (
			Db4oClientServer.newServerConfiguration (), DB.TAXI, 0);
		try
			{
			ObjectContainer	ct1 = server.openClient ();
			ObjectContainer	ct2 = server.openClient ();
			System.out.println ();
			System.out.println ("Wir legen einen neuen Chauffeur Lahm an ");
			System.out.println ("   und geben ihn dem Taxi, das Ballack fährt,");
			System.out.println ("   unter Verwendung des Clients ct1.");
			Chauffeur	lahm = new Chauffeur ("Lahm", 27);
			List<Taxi> rs = ct1.queryByExample (new Taxi ("BMW", "13",
				new Chauffeur ("Ballack", 31)));
			Taxi	taxi = rs.get (0);
			taxi.setChauffeur (lahm);
			ct1.store (taxi);
			System.out.println ("Datenbank-Inhalt von ct1 aus gesehen:");
			List<Object> rso = ct1.queryByExample (new Object ());
			DB.zeigeResultat (rso);
			System.out.println ("Datenbank-Inhalt von ct2 aus gesehen:");
			rso = ct2.queryByExample (new Object ());
			DB.zeigeResultat (rso);
		
			System.out.println ();
			if (true)	//	Umschalten zwischen commit und rollback
				{
				System.out.println ("ct1 macht commit!");
				ct1.commit ();
				}
			else
				{
				System.out.println ("ct1 macht rollback!");
				ct1.rollback ();
				}
		
			System.out.println ();
			System.out.println ("Datenbank-Inhalt von ct1 aus gesehen:");
			rso = ct1.queryByExample (new Object ());
			DB.zeigeResultat (rso);
			System.out.println ("Datenbank-Inhalt von ct2 aus gesehen:");
			rso = ct2.queryByExample (new Object ());
			DB.zeigeResultat (rso);
			System.out.println ();
			System.out.println ("Datenbank-Inhalt von ct1 aus gesehen"
				+ " nach Refresh:");
			rso = ct1.queryByExample (new Object ());
			DB.zeigeRefreshedResultat (ct1, rso, 2);
			System.out.println ("Datenbank-Inhalt von ct2 aus gesehen"
				+ " nach Refresh:");
			rso = ct2.queryByExample (new Object ());
			DB.zeigeRefreshedResultat (ct2, rso, 2);
		
			ct1.close ();
			ct2.close ();
			}
			finally
				{
				server.close ();
				}
		}

	}	//	end Anwendung
