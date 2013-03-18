package de.hauschil.dbprojekt.controller;

import java.util.ArrayList;

import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

public interface DB_Controller {
	public void initDBConnection(Index... indizes);
	public void createTables();
	public void dropTables();
	public void storeObject(Object o);
	public void commit();
	public Query query();
	public void closeDBConncetion();
	public void storeKunden(Kunde[] k);
	public void storeAnrufe(Anruf[] a);
	public void closePStatement();
	
	public ArrayList<Kunde> getKunden(String vorname, String nachname);
	public ArrayList<Anruf> getAnrufe(Telefon anrufer, Telefon angerufener, Long datum1, Long datum2);
	public void deleteAnrufe(long datum1, long datum2);
	public Kunde getKundeByNumber(String number);
}
