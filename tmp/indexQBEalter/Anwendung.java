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

	public static void	findeQBEChauffeurMitAlter (int alter)
		{
		long	zeit = System.nanoTime ();
		List<Chauffeur>	chauffeure 
			= getDb ().queryByExample (new Chauffeur (null, alter));
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ();
		System.out.println ("QBE: Es wurden folgende Chauffeure mit Alter "
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
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			findeQBEChauffeurMitAlter (39);
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
