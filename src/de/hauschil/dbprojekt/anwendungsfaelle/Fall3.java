package de.hauschil.dbprojekt.anwendungsfaelle;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hauschil.dbprojekt.controller.DB_Controller;
import de.hauschil.dbprojekt.controller.Index;
import de.hauschil.dbprojekt.model.Anruf;

public class Fall3 {
	private long[] anfangszeit;
	private long[] endzeit;
	private DB_Controller db;
	
	public Fall3(DB_Controller db) {
		this.db = db;
		anfangszeit = new long[2];
		endzeit = new long[2];
	}
	
	public void run(boolean indexed) {
		db.initDBConnection(new Index[] {
			new Index(Anruf.class, "datum", indexed)
		});

		anfangszeit[indexed ? 1 : 0] = System.nanoTime();

		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.set(2012, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.set(2012, cal1.get(Calendar.MONTH), cal1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
		db.deleteAnrufe(cal1.getTimeInMillis(), cal2.getTimeInMillis());
		
		db.closeDBConncetion();
		
		endzeit[indexed ? 1 : 0] = System.nanoTime();
	}
	
	public long getTime(int which) {
		return (endzeit[which] - anfangszeit[which]) / 1000 / 1000;
	}

	@Override
	public String toString() {
		return "Fall3 ohne Index: " + getTime(0) + " ms\n" +
			   "Fall3  mit Index: " + getTime(1) + " ms";
	}
}
