public class	LastTaxi
	extends		Taxi
	{

	private double	aufschlag;

	//	Constructors and Operations:
	public final double	getAufschlag ()
		{
		return aufschlag;
		}

	public final void	setAufschlag (double aufschlag)
		{
		this.aufschlag = aufschlag;
		}

	public 	LastTaxi
		(
		String	modell,
		String	nummer,
		Chauffeur	aChauffeur,
		double	aufschlag
		)
		/**
		*	working constructor
		**/
		{
		super (modell, nummer, aChauffeur);
		setAufschlag (aufschlag);
		}

	public void	erzeugeFahrt ()
		{
		super.erzeugeFahrt ();
		Fahrt	f = getFahrten ().get (getFahrten ().size () - 1);
		for (int i = 0; i < f.getEntfernung ().length; i++)
			{
			f.getEntfernung ()[i] = f.getEntfernung ()[i] / getAufschlag ();
			}
		}

	public String	toString ()
		{
		String	s = super.toString ();
		s = "Last[" + getAufschlag () + "]" + s;
		return s;
		}

	}	//	end LastTaxi
