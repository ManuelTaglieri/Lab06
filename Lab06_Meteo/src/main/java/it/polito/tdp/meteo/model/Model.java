package it.polito.tdp.meteo.model;

import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO dati;
	private List<Citta> citta;
	private List<Rilevamento> rilevamenti;

	public Model() {
		
		this.dati = new MeteoDAO();
		this.citta = new ArrayList<Citta>();
		
		for (String s : dati.getCitta()) {
			citta.add(new Citta(s));
		}
		
		this.rilevamenti = dati.getAllRilevamenti();
		
		for (Citta c : this.citta) {
			List<Rilevamento> rivPerCitta = new ArrayList<Rilevamento>();
			for (Rilevamento r : this.rilevamenti) {
				if (c.getNome().equals(r.getLocalita()))
					rivPerCitta.add(r);
			}
			c.setRilevamenti(rivPerCitta);
		}

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		
		String risStringa="";
		
		for (Citta c : citta) {
			List<Rilevamento> ris = dati.getAllRilevamentiLocalitaMese(mese, c.getNome());
			float somma = 0;
			for (Rilevamento r : ris) {
				somma += r.getUmidita();
			}
			float media = somma/ris.size();
			media = Math.round(media);
			risStringa += c.getNome() + ": " + media + "\n";
		}
	
		return risStringa;
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		return "TODO!";
	}
	

}
