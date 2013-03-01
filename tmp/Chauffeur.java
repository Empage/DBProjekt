public class	Chauffeur
	{

	private String	name;

	private int	alter;

	//	Constructors and Operations:
	public final String	getName ()
		{
		return name;
		}

	public final void	setName (String name)
		{
		this.name = name;
		}

	public final int	getAlter ()
		{
		return alter;
		}

	public final void	setAlter (int alter)
		{
		this.alter = alter;
		}

	public 	Chauffeur
		(
		String	name,
		int	alter
		)
		/**
		*	working constructor
		**/
		{
		setName (name);
		setAlter (alter);
		}

	public String	toString ()
		{
		return "Chauffeur " + getName () + " (Alter " + getAlter () + ")";
		}

	}	//	end Chauffeur
