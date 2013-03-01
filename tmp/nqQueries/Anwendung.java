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

	public static void	erzeugeChauffeure ()
		{
		getDb ().store (new Chauffeur ("Ballack", 31));
		getDb ().store (new Chauffeur ("Kahn", 39));
		getDb ().store (new Chauffeur ("Klose", 29));
		getDb ().store (new Chauffeur ("Gomez", 22));
		getDb ().store (new Chauffeur ("Meira", 31));
		getDb ().store (new Chauffeur ("Metzelder", 28));
		List<Chauffeur>	resultat = getDb ().query (Chauffeur.class);
		DB.zeigeResultat (resultat);
		}

	public static void	findeChauffeure ()
		{
		System.out.println ();
		System.out.println ("SELECT * FROM Chauffeur WHERE Alter < 30");
		List<Chauffeur>	list 
			= getDb ().query (new Predicate<Chauffeur> ()
				{
				public boolean	match (Chauffeur chauffeur)
					{
					return chauffeur.getAlter () < 30;
					}
				});
		DB.zeigeResultat (list);
		
		System.out.println ();
		System.out.println ("SELECT * FROM Chauffeur "
			+ "WHERE Alter < 30 AND Name like 'M%'");
		list = getDb ().query (new Predicate<Chauffeur> ()
			{
			public boolean	match (Chauffeur chauffeur)
				{
				return chauffeur.getAlter () < 30
					&& chauffeur.getName ().startsWith ("M");
					}
			});
		DB.zeigeResultat (list);
		
		System.out.println ();
		System.out.println ("SELECT * FROM Chauffeur WHERE Alter = "
			+ "(SELECT MAX(Alter) FROM Chauffeur)");
		list = getDb ().query (new Predicate<Chauffeur> ()
			{
			public boolean	match (Chauffeur chauffeur)
				{
				List<Chauffeur>	l = getDb ().query (Chauffeur.class);
				int	max = -1;
				for (Chauffeur c : l)
					{
					if (c.getAlter () > max) max = c.getAlter ();
					}
				return chauffeur.getAlter () == max;
				}
			});
		DB.zeigeResultat (list);
		
		System.out.println ();
		System.out.println ("SELECT * FROM Chauffeur WHERE Alter = 22 "
			+ "OR Alter = 27 OR Alter = 31");
		final int[]	alter = new int[] { 22, 27, 31 };
		list = getDb ().query (new Predicate<Chauffeur> ()
			{
			public boolean	match (Chauffeur chauffeur)
				{
				for (int i = 0; i < alter.length; i++)
					{
					if (chauffeur.getAlter () == alter[i]) return true;
					}
				return false;
				}
			});
		DB.zeigeResultat (list);
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
			erzeugeChauffeure ();
			findeChauffeure ();
			alleObjekteLoeschen ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
