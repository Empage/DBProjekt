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

	public static void	erzeugeChauffeureUndTaxen ()
		{
		System.out.println ();
		System.out.println ("Wir erzeugen einen Chauffeur und ein Taxi"
			+ " mit 9 Fahrten:");
		Chauffeur	chauffeur = new Chauffeur ("Gomez", 22);
		Taxi	taxi = new Taxi ("BMW", "13", chauffeur);
		for (int i = 0; i < 9; i++)
			{
			taxi.erzeugeFahrt ();
			}
		getDb ().store (taxi);
		
		System.out.println ("Alle Taxen in der Datenbank:");
		ObjectSet	resultat = getDb ().query (Taxi.class);
		DB.zeigeResultat (resultat);
		}

	public static void	findeObjekte ()
		{
		System.out.println ();
		System.out.println ("Wir schließen die Datenbank und öffnen sie neu:");
		getDb ().close ();
		
		System.out.println ("Wir konfigurieren für Fahrt cascadeOnActivate.");
		
		EmbeddedConfiguration	conf = Db4oEmbedded.newConfiguration ();
		conf.common ().objectClass (Fahrt.class).cascadeOnActivate (true);
		
		setDb (Db4oEmbedded.openFile (conf, DB.TAXI));
		
		System.out.println ("Alle Taxen in der Datenbank:");
		ObjectSet	resultat = getDb ().query (Taxi.class);
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
			erzeugeChauffeureUndTaxen ();
			findeObjekte ();
			alleObjekteLoeschen ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
