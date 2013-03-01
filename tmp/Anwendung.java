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

	public static void	findeSODAChauffeur (String name)
		{
		long	zeit = System.nanoTime ();
		Query	query = getDb ().query ();
		query.constrain (Chauffeur.class);
		query.descend ("name").constrain ("Kahn");
		ObjectSet	chauffeure = query.execute ();
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ();
		System.out.println ("SODA: Es wurden folgende Chauffeure mit Namen "
			+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Object	chauffeur : chauffeure)
			{
			System.out.println (chauffeur);
			}
		}

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			findeSODAChauffeur ("Kahn");
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
