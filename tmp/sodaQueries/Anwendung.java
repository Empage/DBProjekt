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
		Query	query = getDb ().query ();
		query.constrain (Chauffeur.class);
		query.descend ("alter").constrain (new Integer (30)).smaller ();
		List	resultat = query.execute ();
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("SELECT * FROM Chauffeur"
			+ "WHERE Alter < 30 AND Name like 'M%'");
		query = getDb ().query ();
		query.constrain (Chauffeur.class);
		query.descend ("alter").constrain (new Integer (30)).smaller ();
		query.descend ("name").constrain ("M").startsWith (true);
		//query.descend ("name").constrain ("M%").like ();
			//	unklar, wie das funktioniert
		resultat = query.execute ();
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("SELECT * FROM Chauffeur WHERE Alter = "
			+ "(SELECT MAX(Alter) FROM Chauffeur)");
		query = getDb ().query ();
		query.constrain (Chauffeur.class);
		List	zwischen = query.execute ();
		int	max = -1;
		for (Object c : zwischen)
			{
			if (((Chauffeur) c).getAlter () > max) 
				max = ((Chauffeur) c).getAlter ();
			}
		query = getDb ().query ();
		query.constrain (Chauffeur.class);
		query.descend ("alter").constrain (new Integer (max));
		resultat = query.execute ();
		DB.zeigeResultat (resultat);
		
		System.out.println ();
		System.out.println ("SELECT * FROM Chauffeur WHERE Alter = 22 "
			+ "OR Alter = 27 OR Alter = 31 ORDER BY Name");
		final Integer[]	alter = new Integer[] { 22, 27, 31 };
		query = getDb ().query ();
		query.constrain (Chauffeur.class);
		Constraint	c0 = query.descend ("alter").constrain (alter[0]);
		Constraint	c1 = query.descend ("alter").constrain (alter[1]).or (c0);
		query.descend ("alter").constrain (alter[2]).or (c1);
		query.descend ("name").orderAscending ();
		resultat = query.execute ();
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
