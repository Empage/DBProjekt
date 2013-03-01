package de.hauschil.dbprojekt.controller;

import java.util.List;

public interface DB_Controller {
	public void initDBConnection();
	public void storeObject(Object o);
	public void commit();
	public List query(Object o);
	public void closeDBConncetion();
}
