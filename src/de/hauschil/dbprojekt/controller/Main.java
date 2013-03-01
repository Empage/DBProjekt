package de.hauschil.dbprojekt.controller;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import static de.hauschil.dbprojekt.controller.Helper.*;

public class Main {
	public static void main(String[] args) {
		Kunde[] kunden = Kunde.generateKunden(ANZ_KUNDEN);
		Anruf[] anrufe = Anruf.generateAnrufe(kunden, ANZ_ANRUFPM);
		
		System.out.println(kunden.length);
		System.out.println(anrufe.length);
		for (int i = 0; i < kunden.length; i++) {
			System.out.println(kunden[i]);
		}
//		for (int i = 0; i < anrufe.length; i++) {
//			System.out.println(anrufe[i]);
//		}
	}

}
