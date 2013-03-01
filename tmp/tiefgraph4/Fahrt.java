import	java.util.*;
import	com.db4o.activation.*;
import	com.db4o.ta.*;

public class	Fahrt
	implements	Activatable
	{

	transient private Activator	aActivator;

	private Date	beginn;

	private Date	ende;

	private double[]	entfernung;

	private Chauffeur	aChauffeur;

	private Fahrt	nextFahrt;

	//	Constructors and Operations:
	private final Activator	getActivator ()
		{
		return aActivator;
		}

	private final void	setActivator (Activator aActivator)
		{
		this.aActivator = aActivator;
		}

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

	public Fahrt	getNextFahrt ()
		{
		activate (ActivationPurpose.READ);
		return nextFahrt;
		}

	public final void	setNextFahrt (Fahrt nextFahrt)
		{
		this.nextFahrt = nextFahrt;
		}

	public 	Fahrt
		(
		Activator	aActivator,
		Date	beginn,
		Date	ende,
		double[]	entfernung,
		Chauffeur	aChauffeur,
		Fahrt	nextFahrt
		)
		/**
		*	working constructor
		**/
		{
		setActivator (aActivator);
		setBeginn (beginn);
		setEnde (ende);
		setEntfernung (entfernung);
		setChauffeur (aChauffeur);
		setNextFahrt (nextFahrt);
		}

	public 	Fahrt
		(
		Date	beginn,
		Date	ende,
		double[]	entfernung,
		Chauffeur	chauffeur
		)
		{
		this (null, beginn, ende, entfernung, chauffeur, null); 
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
		activate (ActivationPurpose.READ);
		String	s = "Fahrt mit ";
		if (getChauffeur () == null) s = s + "Chauffeur=null";
		else s = s + getChauffeur ().getName () ;
		if (getEntfernung () == null) s = s + " Entfernung=null";
		else
			{
			s = s + " " + entfernung (getEntfernung ()[0]) + "(";
			s = s	+ entfernung (getEntfernung ()[1]) + ")km ";
			}
		if (getBeginn () == null) s = s + " Beginn=null";
		else s = s	+ getBeginn () + " (" ;
		if (getEnde () == null || getBeginn () == null) s = s + " Ende=null";
		else s = s	+ ((getEnde ().getTime () - getBeginn ().getTime ())
				/1000/60) + " min)";
		return s;
		}

	public void	bind (Activator aActivator)
		{
		if (getActivator () == aActivator) return;
		
		if (getActivator () != null && aActivator != null)
			{
			System.err.println ("getActivator : " + getActivator ());
			System.err.println ("aActivator : " + aActivator);
			throw new IllegalStateException ();
			}
		setActivator (aActivator);
		}

	public void	activate (ActivationPurpose aActivationPurpose)
		{
		if (getActivator () != null) 
			{
			getActivator ().activate (aActivationPurpose);
			}
		}

	}	//	end Fahrt
