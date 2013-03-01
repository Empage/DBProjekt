import	com.db4o.*;
import	java.util.*;

public class	DB
	{

	public static final String	TAXI = System.getProperty ("user.home") + "/tmp/taxi.db4o";

	public static final String	HOST = "localhost";

	public static final int	PORT = 0xdb40;

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
