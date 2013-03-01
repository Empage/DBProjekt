package de.hauschil.dbprojekt.controller;

public interface DB_Controller {
	public void initDBConnection();
	public void storeObject(Object o);
	public void commit();
	public void closeDBConncetion();
}
