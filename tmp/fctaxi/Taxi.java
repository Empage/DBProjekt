import	java.util.*;

public class	Taxi
	{

	private String	modell;

	private String	nummer;

	private Chauffeur	aChauffeur;

	private List<Fahrt>	fahrten;

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

	public final List<Fahrt>	getFahrten ()
		{
		return fahrten;
		}

	public final void	setFahrten (List<Fahrt> fahrten)
		{
		this.fahrten = fahrten;
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
		List<Fahrt>	fahrten
		)
		/**
		*	working constructor
		**/
		{
		setModell (modell);
		setNummer (nummer);
		setChauffeur (aChauffeur);
		setFahrten (fahrten);
		}

	public 	Taxi
		(
		String	modell,
		String	nummer,
		Chauffeur	aChauffeur
		)
		{
		this (modell, nummer, aChauffeur, new ArrayList<Fahrt> ());
		}

	public void	erzeugeFahrt ()
		{
		Fahrt	f;
		int	n;
		long	zeit = System.currentTimeMillis () - 3*24*3600*1000;
			//	Drei Tage zurück.
		if ((n = getFahrten ().size ()) > 0)
			{
			f = getFahrten ().get (n - 1);	//	letzte Fahrt
			zeit = f.getEnde ().getTime () + getRandom ().nextInt (3600*1000);
			}
		long	dauer = getRandom ().nextInt (3600*1000);
		long	minuten = dauer /1000/60;
		getFahrten ().add (new Fahrt (
			new Date (zeit), new Date (zeit + dauer),
			new double[] { Math.random () * minuten * 1.5 + 1, 
				Math.random () * minuten * 1.5 + 1 },
			getChauffeur ()));
		}

	public String	toString ()
		{
		String	s = "Taxi " + getModell ()
			+ ":" + getNummer () + " (" + getChauffeur () + ")";
		for (Fahrt f : getFahrten ())
			{
			s = s + "\n      " + f;
			}
		return s;
		}

	}	//	end Taxi
