public class	Taxi
	{

	private String	modell;

	private String	nummer;

	private Chauffeur	aChauffeur;

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

	public 	Taxi
		(
		String	modell,
		String	nummer,
		Chauffeur	aChauffeur
		)
		/**
		*	working constructor
		**/
		{
		setModell (modell);
		setNummer (nummer);
		setChauffeur (aChauffeur);
		}

	public String	toString ()
		{
		return "Taxi " + getModell ()
			+ ":" + getNummer () + " (" + getChauffeur () + ")";
		}

	}	//	end Taxi
