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

	public static void	findeQBEChauffeur (String name)
		{
		long	zeit = System.nanoTime ();
		List<Chauffeur>	chauffeure 
			= getDb ().queryByExample (new Chauffeur (name, 0));
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ();
		System.out.println ("QBE: Es wurden folgende Chauffeure mit Namen "
			+ name + " gefunden (Zeit " + zeit + " ms):");
		for (Chauffeur	chauffeur : chauffeure)
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
			findeQBEChauffeur ("Kahn");
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
