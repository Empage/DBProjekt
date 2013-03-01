import	java.util.*;

public class	Taxi
	{

	private String	modell;

	private String	nummer;

	private Chauffeur	aChauffeur;

	private Fahrt	aFahrt;

	private int	anzFahrten;

	private static Random	aRandom;
		static
		{
		setRandom (new Random ());
		}

	//	Constructors and Operations:
	public final String	getModell ()
		{
		return modell;
		}

	public final void	setModell (String modell)
		{
		this.modell = modell;
		}

	public final String	getNummer ()
		{
		return nummer;
		}

	public final void	setNummer (String nummer)
		{
		this.nummer = nummer;
		}

	public final Chauffeur	getChauffeur ()
		{
		return aChauffeur;
		}

	public final void	setChauffeur (Chauffeur aChauffeur)
		{
		this.aChauffeur = aChauffeur;
		}

	public final Fahrt	getFahrt ()
		{
		return aFahrt;
		}

	public final void	setFahrt (Fahrt aFahrt)
		{
		this.aFahrt = aFahrt;
		}

	public final int	getAnzFahrten ()
		{
		return anzFahrten;
		}

	public final void	setAnzFahrten (int anzFahrten)
		{
		this.anzFahrten = anzFahrten;
		}

	private static final Random	getRandom ()
		{
		return aRandom;
		}

	private static final void	setRandom (Random aRandom)
		{
		Taxi.aRandom = aRandom;
		}

	public 	Taxi
		(
		String	modell,
		String	nummer,
		Chauffeur	aChauffeur,
		Fahrt	aFahrt,
		int	anzFahrten
		)
		/**
		*	working constructor
		**/
		{
		setModell (modell);
		setNummer (nummer);
		setChauffeur (aChauffeur);
		setFahrt (aFahrt);
		setAnzFahrten (anzFahrten);
		}

	public 	Taxi
		(
		String	modell,
		String	nummer,
		Chauffeur	aChauffeur
		)
		{
		this (modell, nummer, aChauffeur, null, 0);
		}

	public void	erzeugeFahrt ()
		{
		Fahrt	f;
		long	zeit = System.currentTimeMillis () - 3*24*3600*1000;
			//	Drei Tage zurück.
		if (getAnzFahrten () > 0)
			{
			f = getFahrt ();
			while (f.getNextFahrt () != null) f = f.getNextFahrt (); // finde letzte Fahrt
			zeit = f.getEnde ().getTime () 
				+ getRandom ().nextInt (3600*1000);
			}
		long	dauer = getRandom ().nextInt (3600*1000);
		long	minuten = dauer /1000/60;
		Fahrt	fahrt = new Fahrt (
			new Date (zeit), new Date (zeit + dauer),
			new double[] { Math.random () * minuten * 1.5 + 1, 
				Math.random () * minuten * 1.5 + 1 },
			getChauffeur ());
		f = getFahrt ();
		if (f == null)
			{
			setFahrt (fahrt);
			}
		else
			{
			while (f.getNextFahrt () != null) f = f.getNextFahrt ();
			f.setNextFahrt (fahrt);
			}
		setAnzFahrten (getAnzFahrten () + 1);
		}

	public String	toString ()
		{
		String	s = "Taxi " + getModell ()
			+ ":" + getNummer () + " (" + getChauffeur () + ")";
		s = s + "\n      Anzahl Fahrten: " + getAnzFahrten ();
		Fahrt	f = getFahrt ();
		int	i = 0;
		while (f != null) 
			{
			i++;
			s = s + "\n      " + i + ". " + f;
			f = f.getNextFahrt ();
			}
		return s;
		}

	}	//	end Taxi
