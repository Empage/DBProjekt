import	com.db4o.*;

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

	public static void	erzeugeUndSpeichereChauffeure ()
		{
		Chauffeur	chauffeur = new Chauffeur ("Ballack", 29);
		getDb ().store (chauffeur);
		System.out.println (chauffeur + " wurde gespeichert.");
		chauffeur = new Chauffeur ("Kahn", 39);
		getDb ().store (chauffeur);
		getDb ().store (chauffeur);
		System.out.println (chauffeur + " wurde gespeichert.");
		}

	public static void	main (String[] arg)
		{
		setDb (Db4oEmbedded.openFile (Db4oEmbedded.newConfiguration (),
			DB.TAXI));
		try
			{
			erzeugeUndSpeichereChauffeure ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
