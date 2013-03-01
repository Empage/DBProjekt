import	com.db4o.*;
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

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			List<Object>	alle = getDb ().queryByExample (new Object ());
			for (Object o : alle) { getDb ().delete (o); }
		
			Chauffeur	chauffeurLive = new Chauffeur ("Ballack", 29);
			getDb ().store (chauffeurLive);
		
			List<Chauffeur> rs = getDb ()
				.queryByExample (new Chauffeur ("Ballack", 0));
			Chauffeur	chauffeurDB = rs.get (0); 
			System.out.println ();
			System.out.println ("Live-Ballacks Alter: " + chauffeurLive.getAlter ());
			System.out.println ("DB-Ballacks Alter: " + chauffeurDB.getAlter ());
		
			chauffeurLive.setAlter (55);
			getDb ().store (chauffeurLive);
			getDb ().commit ();
		
			System.out.println ();
			System.out.println ("Nach Änderung auf 55 und commit:");
			System.out.println ("   Live-Ballacks Alter: " + chauffeurLive.getAlter ());
			rs = getDb ().queryByExample (new Chauffeur ("Ballack", 0));
			System.out.println (rs.size () + " Objekte in der DB.");
			chauffeurDB = rs.get (0); 
			System.out.println ("   DB-Ballacks Alter: " + chauffeurDB.getAlter ());
		
			chauffeurLive.setAlter (66);
			getDb ().store (chauffeurLive);
			getDb ().rollback ();
		
			System.out.println ();
			System.out.println ("Nach Änderung auf 66 und rollback:");
			System.out.println ("   Live-Ballacks Alter: " + chauffeurLive.getAlter ());
			rs = getDb ().queryByExample (new Chauffeur ("Ballack", 0));
			chauffeurDB = rs.get (0); 
			System.out.println ("   DB-Ballacks Alter: " + chauffeurDB.getAlter ());
		
			chauffeurLive.setAlter (66);
			getDb ().store (chauffeurLive);
			getDb ().rollback ();
		
			System.out.println ();
			System.out.println ("Nach Änderung auf 66, rollback und refresh:");
			System.out.println ("   Live-Ballacks Alter: " + chauffeurLive.getAlter ());
			getDb ().ext ().refresh (chauffeurLive, Integer.MAX_VALUE);
			System.out.println ("   Refreshed-Ballacks Alter: " + chauffeurLive.getAlter ());
			rs = getDb ().queryByExample (new Chauffeur ("Ballack", 0));
			chauffeurDB = rs.get (0); 
			System.out.println ("   DB-Ballacks Alter: " + chauffeurDB.getAlter ());
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
