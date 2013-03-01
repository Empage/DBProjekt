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

	public static void	erzeugeIndex (String feld)
		{
		long	zeit = System.nanoTime ();
		getDb ().close ();
		EmbeddedConfiguration	conf = Db4oEmbedded.newConfiguration ();
		conf.common ().objectClass (Chauffeur.class)
			.objectField (feld).indexed (true);
		setDb (Db4oEmbedded.openFile (conf, DB.TAXI));
		System.out.println ();
		getDb ().queryByExample (new Chauffeur (null, 0));
		zeit = (System.nanoTime () - zeit) / 1000 / 1000;
		System.out.println ("Index über " + feld + " wurde erzeugt."
			+ " (Zeit " + zeit + " ms)");
		}

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			erzeugeIndex ("name");
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
