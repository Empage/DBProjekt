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

	public static void	findeNQChauffeur (String name)
		{
		final	String	nname = name;
		long	zeit = System.nanoTime ();
		List<Chauffeur>	chauffeure = getDb ().query (
			new Predicate<Chauffeur> ()
				{
				public boolean	match (Chauffeur chauffeur)
					{
					return chauffeur.getName ().equals (nname);
					}
				});
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ();
		System.out.println ("NQ: Es wurden folgende Chauffeure mit Namen "
			+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Chauffeur	chauffeur : chauffeure)
			{
			System.out.println (chauffeur);
			}
		}

	public static void	findeNQChauffeurMitAlter (int alter)
		{
		final	int	nalter = alter;
		long	zeit = System.nanoTime ();
		List<Chauffeur>	chauffeure = getDb ().query (
			new Predicate<Chauffeur> ()
				{
				public boolean	match (Chauffeur chauffeur)
					{
					return chauffeur.getAlter () == nalter;
					}
				});
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ();
		System.out.println ("NQ: Es wurden folgende Chauffeure mit Alter "
			+ alter + " gefunden (Zeit " + zeit + " ms):");
		int	j = 0;
		for (Chauffeur	chauffeur : chauffeure)
			{
			if (j > 1) { System.out.println ("     ..."); break; }
			System.out.println (chauffeur);
			j++;
			}
		}

	public static void	main (String[] arg)
		{
		setDb (Db4o.openFile (DB.TAXI));
		try
			{
			findeNQChauffeur ("Kahn");
			findeNQChauffeurMitAlter (39);
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
