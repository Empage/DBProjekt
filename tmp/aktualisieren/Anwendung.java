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

	public static void	aktualisiereChauffeure ()
		{
		List<Chauffeur>	resultat = getDb ().query (Chauffeur.class);
		DB.zeigeResultat (resultat);
		
		System.out.println ("Aktualisierung: Ballack um 1 Jahr älter:");
		
		resultat = getDb ().queryByExample (new Chauffeur ("Ballack", 0));
		Chauffeur	gefunden = resultat.get (0);
		gefunden.setAlter (gefunden.getAlter () + 1);
		getDb ().store (gefunden);
		
		resultat = getDb ().query (Chauffeur.class);
		DB.zeigeResultat (resultat);
		}

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			aktualisiereChauffeure ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
