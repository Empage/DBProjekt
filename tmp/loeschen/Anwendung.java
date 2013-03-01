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

	public static void	loescheChauffeure ()
		{
		System.out.println ("Löschen von Ballack:");
		List<Chauffeur>	resultat 
			= getDb ().queryByExample (new Chauffeur ("Ballack", 0));
		Chauffeur	gefunden = resultat.get (0);
		getDb ().delete (gefunden);
		resultat = getDb ().query (Chauffeur.class);
		DB.zeigeResultat (resultat);
		
		System.out.println ("Löschen von Kahn:");
		resultat = getDb ().queryByExample (new Chauffeur ("Kahn", 0));
		gefunden = resultat.get (0);
		getDb ().delete (gefunden);
		resultat = getDb ().query (Chauffeur.class);
		DB.zeigeResultat (resultat);
		
		System.out.println ("Löschen von allen Objekten:");
		alleObjekteLoeschen ();
		}

	public static void	alleObjekteLoeschen ()
		{
		List<Object>	resultat 
			= getDb ().query (Object.class);
			//= getDb ().queryByExample (new Object ());
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
			loescheChauffeure ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
