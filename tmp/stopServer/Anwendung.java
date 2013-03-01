import	com.db4o.*;
import	com.db4o.cs.*;
import	com.db4o.messaging.*;

public class	Anwendung
	{

	//	Constructors and Operations:
	public static void	main (String[] aString)
		{
		ObjectContainer	cont = null;
		try
			{
			cont = Db4oClientServer.openClient (
				DB.HOST, DB.PORT, "stopUser", "stopPassword");
			}
			catch (Exception e ) { e.printStackTrace (); }
		if (cont != null)
			{
			MessageSender	sender = cont.ext ().configure ().clientServer ()
				.getMessageSender ();
			sender.send (new StopServer ());
			cont.close ();
			}
		}

	}	//	end Anwendung
