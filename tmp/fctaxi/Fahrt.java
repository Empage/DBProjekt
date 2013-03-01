import	java.util.*;

public class	Fahrt
	{

	private Date	beginn;

	private Date	ende;

	private double[]	entfernung;

	private Chauffeur	aChauffeur;

	//	Constructors and Operations:
	public final Date	getBeginn ()
		{
		return beginn;
		}

	public final void	setBeginn (Date beginn)
		{
		this.beginn = beginn;
		}

	public final Date	getEnde ()
		{
		return ende;
		}

	public final void	setEnde (Date ende)
		{
		this.ende = ende;
		}

	public final double[]	getEntfernung ()
		{
		return entfernung;
		}

	public final void	setEntfernung (double[] entfernung)
		{
		this.entfernung = entfernung;
		}

	public final int	numEntfernung ()
		{
		return getEntfernung ().length;
		}

	public final void	addEntfernung (double entfernung)
		{
		double[]	x = getEntfernung ();
		double[]	y = new double[x.length + 1];
		System.arraycopy (x, 0, y, 0, x.length);
		y[x.length] = entfernung;
		setEntfernung (y);
		}

	public final void	addEntfernung (double[] entfernung)
		{
		double[]	x = getEntfernung ();
		double[]	y = new double[x.length + entfernung.length];
		System.arraycopy (x, 0, y, 0, x.length);
		System.arraycopy (x, 0, y, x.length, entfernung.length);
		setEntfernung (y);
		}

	public final void	rmvEntfernung (int i)
		{
		double[]	x = getEntfernung ();
		double[]	y = new double[x.length - 1];
		System.arraycopy (x, 0, y, 0, i);
		System.arraycopy (x, i + 1, y, i, y.length - i);
		setEntfernung (y);
		}

	public final double	getEntfernung (int i)
		{
		return getEntfernung ()[i];
		}

	public final void	setEntfernung
		(
		int	i,
		double	entfernung
		)
		{
		double[]	x = getEntfernung ();
		x[i] = entfernung;
		setEntfernung (x);
		}

	public final Chauffeur	getChauffeur ()
		{
		return aChauffeur;
		}

	public final void	setChauffeur (Chauffeur aChauffeur)
		{
		this.aChauffeur = aChauffeur;
		}

	public 	Fahrt
		(
		Date	beginn,
		Date	ende,
		double[]	entfernung,
		Chauffeur	aChauffeur
		)
		/**
		*	working constructor
		**/
		{
		setBeginn (beginn);
		setEnde (ende);
		setEntfernung (entfernung);
		setChauffeur (aChauffeur);
		}

	private String	entfernung (double e)
		{
		String	s = "" + e;
		int	n = s.indexOf ('.');
		if (n >= 0) s = s.substring (0, n);
		return s;
		}

	public String	toString ()
		{
		return "Fahrt mit " + getChauffeur ().getName () 
			+ " " + entfernung (getEntfernung ()[0]) + "("
			+ entfernung (getEntfernung ()[1]) + ")km "
			+ getBeginn () + " (" 
			+ ((getEnde ().getTime () - getBeginn ().getTime ())
				/1000/60) + " min)";
		}

	}	//	end Fahrt
