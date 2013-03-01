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

	public static void	findeChauffeure ()
		{
		Chauffeur	chauffeur = new Chauffeur (null, 29);
		List<Chauffeur>	resultat = getDb ().queryByExample (chauffeur);
		System.out.println ("Mit \"getDb ().queryByExample (new Chauffeur (null, 29))\" finden wir:");
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("Mit \"getDb ().query (Chauffeur.class)\" finden wir:");
		resultat = getDb ().query (Chauffeur.class);
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("Mit \"getDb ().queryByExample (new Chauffeur (null, 0))\" finden wir:");
		resultat = getDb ().queryByExample (new Chauffeur (null, 0));
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("Mit \"getDb ().queryByExample (new Chauffeur (\"Kahn\", 0))\" finden wir:");
		resultat = getDb ().queryByExample (new Chauffeur ("Kahn", 0));
		DB.zeigeResultat (resultat);
		}

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			findeChauffeure ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
