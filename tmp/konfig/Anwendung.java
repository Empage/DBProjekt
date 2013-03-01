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
		List<Taxi>	resultat = getDb ().query (Taxi.class);
		DB.zeigeResultat (resultat);
		}

	public static void	aktualisierungsTiefe ()
		{
		System.out.println ();
		System.out.println ("Wir schließen die Datenbank und öffnen sie neu:");
		getDb ().close ();
		
		System.out.println ("Wir konfigurieren für Taxi Aktualisierungstiefe 5.");
		
		EmbeddedConfiguration	conf = Db4oEmbedded.newConfiguration ();
		conf.common ().objectClass (Taxi.class).updateDepth (5);
		
		setDb (Db4oEmbedded.openFile (conf, DB.TAXI));
		
		System.out.println ("Alle Taxen in der Datenbank:");
		List<Taxi>	resultat = getDb ().query (Taxi.class);
		Taxi	t = resultat.get (0);
		Fahrt	f = t.getFahrt ();
		while (f != null)
			{
			getDb ().activate (f, 1);
			f = f.getNextFahrt ();
			}
		System.out.println (t);
		System.out.println ("Die Entfernung jeder Fahrt wird verdoppelt"
			+ " und dann das Taxi zurückgespeichert:");
		f = t.getFahrt ();
		while (f != null)
			{
			getDb ().activate (f, 1);
			f.getEntfernung ()[0] = 2 * f.getEntfernung ()[0];
			f = f.getNextFahrt ();
			}
		getDb ().store (t);
		System.out.println (t);
		System.out.println ("Wir schließen die Datenbank und öffnen sie neu:");
		getDb ().close ();
		
		conf = Db4oEmbedded.newConfiguration ();
		conf.common ().objectClass (Taxi.class).updateDepth (5);
			//	Ein Konfiguration darf man offenbar nicht mehrmals verwenden?
		
		setDb (Db4oEmbedded.openFile (conf, DB.TAXI));
		resultat = getDb ().query (Taxi.class);
		t = resultat.get (0);
		f = t.getFahrt ();
		while (f != null)
			{
			getDb ().activate (f, 1);
			f = f.getNextFahrt ();
			}
		System.out.println (t);
		System.out.println ("Wir sehen: Die Verdopplung geht nur bis zur 4. Fahrt.");
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
			aktualisierungsTiefe ();
			alleObjekteLoeschen ();
			}
			finally
				{
				getDb ().close ();
				}
		}

	}	//	end Anwendung
