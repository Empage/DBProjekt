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
		try
			{
			setDb (Db4o.openClient (DB.HOST, DB.PORT, "user1", "password1"));
			try
				{
				alleObjekteLoeschen ();
				erzeugeChauffeureUndTaxen ();
				}
				finally
					{
					getDb ().close ();
					}
			}
			catch (Exception e) { e.printStackTrace (); }
		}

	}	//	end Anwendung
