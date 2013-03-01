import	com.db4o.*;
import	com.db4o.cs.*;
import	com.db4o.messaging.*;

public class	StartServer
	implements	MessageRecipient
	{

	private boolean	stop;

	//	Constructors and Operations:
	public final boolean	isStop ()
		{
		return stop;
		}

	private final void	setStop (boolean stop)
		{
		this.stop = stop;
		}

	public 	StartServer (boolean stop)
		/**
		*	working constructor
		**/
		{
		setStop (stop);
		}

	public synchronized void	runServer ()
		{
		ObjectServer	server = Db4oClientServer.openServer (
			Db4oClientServer.newServerConfiguration (), DB.TAXI, DB.PORT);
		try
			{
			server.grantAccess ("stopUser", "stopPassword");
			server.grantAccess ("user1", "password1");
			server.grantAccess ("user2", "password2");
		
			server.ext ().configure ().clientServer ().setMessageRecipient (this);
				//	this.processMessage erhält die Botschaften (insbesondere stop)
		
			Thread.currentThread ().setName (this.getClass ().getName ());
				//	um den Thread in einem Debugger zu identifizieren
		
			Thread.currentThread ().setPriority (Thread.MIN_PRIORITY);
				//	server hat eigenen Thread. Daher genügt hier ganz niedrige Prio.
		
			try
				{
				System.out.println ("db4o-Server wurde gestartet.");
				while (!isStop ())				
					{
					this.wait ();
						//	Warte, bis du gestoppt wirst.
					}
				}
				catch (InterruptedException e) { e.printStackTrace (); }
			}
			finally
				{
				server.close ();
				System.out.println ("db4o-Server wurde gestoppt.");
				}
		}

	public void	processMessage
		(
		MessageContext	aMessageContext,
		Object	message
		)
		{
		if (message instanceof StopServer)
			{
			close ();
			}
		}

	public synchronized void	close ()
		{
		setStop (true);
		this.notify ();
		}

	}	//	end StartServer
