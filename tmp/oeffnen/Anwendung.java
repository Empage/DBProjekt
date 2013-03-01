import	com.db4o.*;

public class	Anwendung
	{

	//	Constructors and Operations:
	public static void	main (String[] arg)
		{
		ObjectContainer	db = Db4oEmbedded.openFile (
			Db4oEmbedded.newConfiguration (),
			DB.TAXI);
		System.out.println ("Datenbank " + DB.TAXI + " wurde geöffnet.");
		try
			{
			//	Hier tun wir etwas mit der Datenbank.
			}
			finally
				{
				db.close ();
				}
		}

	}	//	end Anwendung
