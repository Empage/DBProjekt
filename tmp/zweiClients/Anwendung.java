import	com.db4o.*;
import	com.db4o.cs.*;
import	com.db4o.query.*;
import	java.util.*;

public class	Anwendung
	{

	//	Constructors and Operations:
	public static void	main (String[] arg)
		{
		ObjectContainer	ct1 = null;
		ObjectContainer	ct2 = null;
		try
			{
			ct1 = Db4oClientServer.openClient (
				DB.HOST, DB.PORT, "user1", "password1");
		
			ct2 = Db4oClientServer.openClient (
				DB.HOST, DB.PORT, "user2", "password2");
		
			// Zunächst ein paar Daten in die Datenbank:
			ct1.store (new Taxi ("BMW", "13", new Chauffeur ("Ballack", 31)));
			ct1.commit ();
		
			System.out.println ();
			System.out.println ("Wir legen einen neuen Chauffeur Lahm an ");
			System.out.println ("   und geben ihn dem Taxi, das Ballack fährt,");
			System.out.println ("   unter Verwendung des Clients ct1.");
			Chauffeur	lahm = new Chauffeur ("Lahm", 27);
			List<Taxi> rs = ct1.queryByExample (new Taxi ("BMW", "13",
				new Chauffeur ("Ballack", 31)));
			Taxi	taxi = rs.get (0);
			System.out.println ("Taxi: " + taxi);
			System.out.println ();
			taxi.setChauffeur (lahm);
			ct1.store (taxi);
			System.out.println ("Datenbank-Inhalt von ct1 aus gesehen:");
			List<Object> rso = ct1.queryByExample (new Object ());
			DB.zeigeResultat (rso);
			System.out.println ("Datenbank-Inhalt von ct2 aus gesehen:");
			rso = ct2.queryByExample (new Object ());
			DB.zeigeResultat (rso);
		
			System.out.println ();
			if (true)	//	Umschalten zwischen commit und rollback
				{
				System.out.println ("ct1 macht commit!");
				ct1.commit ();
				}
			else
				{
				System.out.println ("ct1 macht rollback!");
				ct1.rollback ();
				}
		
			System.out.println ();
			System.out.println ("Datenbank-Inhalt von ct1 aus gesehen:");
			rso = ct1.queryByExample (new Object ());
			DB.zeigeResultat (rso);
			System.out.println ("Datenbank-Inhalt von ct2 aus gesehen:");
			rso = ct2.queryByExample (new Object ());
			DB.zeigeResultat (rso);
			System.out.println ();
			System.out.println ("Datenbank-Inhalt von ct1 aus gesehen"
				+ " nach Refresh:");
			rso = ct1.queryByExample (new Object ());
			DB.zeigeRefreshedResultat (ct1, rso, 2);
			System.out.println ("Datenbank-Inhalt von ct2 aus gesehen"
				+ " nach Refresh:");
			rso = ct2.queryByExample (new Object ());
			DB.zeigeRefreshedResultat (ct2, rso, 2);
			}
			catch (Exception e) { e.printStackTrace (); }
			finally
				{
				ct1.close ();
				ct2.close ();
				}
		}

	}	//	end Anwendung
