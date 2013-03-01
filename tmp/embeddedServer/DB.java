import	com.db4o.*;
import	java.util.*;

public class	DB
	{

	public static final String	TAXI = "../taxi.db4o";

	//	Constructors and Operations:
	public static void	zeigeResultat (List<?> aList)
		{
		System.out.println ("Anzahl Objekte: " + aList.size ());
		for (Object o : aList)
			{
			System.out.println ("   " + o);
			}
		}

	public static void	zeigeRefreshedResultat
		(
		ObjectContainer	aObjectContainer,
		List<?>	aList,
		int	tiefe
		)
		{
		System.out.println ("Anzahl Objekte: " + aList.size ());
		for (Object o : aList)
			{
			aObjectContainer.ext ().refresh (o, tiefe);
			System.out.println ("   " + o);
			}
		}

	}	//	end DB
