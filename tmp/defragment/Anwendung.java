import	com.db4o.defragment.*;

public class	Anwendung
	{

	//	Constructors and Operations:
	public static void	main (String[] arg)
		{
		try
			{
			Defragment.defrag (DB.TAXI);
			}
			catch (Exception e)
				{
				e.printStackTrace ();
				}
		}

	}	//	end Anwendung
