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
	private Map<String, Citta> citta;
	private List<Rilevamento> rilevamenti;
	private int costoMin;
	private boolean nuovaCitta;
	private int contatore;
	private List<Citta> soluzioneFinale;

	public Model() {
		
		this.dati = new MeteoDAO();
		this.citta = new HashMap<String, Citta>();
		
		for (String s : dati.getCitta()) {
			citta.put(s, new Citta(s));
		}
		
		this.rilevamenti = dati.getAllRilevamenti();
		
		for (Citta c : this.citta.values()) {
			List<Rilevamento> rivPerCitta = new ArrayList<Rilevamento>();
			for (Rilevamento r : this.rilevamenti) {
				if (c.getNome().equals(r.getLocalita()))
					rivPerCitta.add(r);
			}
			c.setRilevamenti(rivPerCitta);
		}
		
		this.nuovaCitta = true;
		this.contatore = 0;

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		
		String risStringa="";
		
		for (Citta c : citta.values()) {
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
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> opzioni = new ArrayList<Citta>();
		
		for (Citta c : citta.values()) {
			opzioni.add(new Citta(c.getNome(), c.getRilevamentiMese(mese)));
		}
	
		List<Citta> soluzione = new ArrayList<Citta>();
		
		this.costoMin = 400 + 15*100;
		this.nuovaCitta = true;
		this.contatore = 0;
		this.soluzioneFinale = new ArrayList<Citta>();
		ricorsivo(0, soluzione, opzioni, 0, null);
		System.out.println(costoMin);
		
		return soluzioneFinale;
	}

	private void ricorsivo(int giorno, List<Citta> soluzione, List<Citta> opzioni, int costo, Citta cittaCorrente) {
		if (giorno>=NUMERO_GIORNI_TOTALI) {
			if (costo<costoMin) {
				this.costoMin = costo;
				this.soluzioneFinale = new ArrayList<Citta>(soluzione);
			}
			return;
		} else {
			for (Citta c : opzioni) {
				if (giorno>2) {
					if (soluzione.get(giorno-1).equals(soluzione.get(giorno-2)) && soluzione.get(giorno-2).equals(soluzione.get(giorno-3)))
						nuovaCitta = true;
					else
						nuovaCitta = false;
				}
				else if (giorno == 0)
					nuovaCitta = true;
				else
					nuovaCitta = false;
				if (c.equals(cittaCorrente)) {
					if (c.getCounter()<NUMERO_GIORNI_CITTA_MAX && (c.getRilevamenti().get(giorno).getUmidita() + costo) < costoMin) {
						soluzione.add(c);
						c.increaseCounter();
						ricorsivo(giorno + 1, soluzione, opzioni, c.getRilevamenti().get(giorno).getUmidita() + costo, c);
						c.decreaseCounter();
						soluzione.remove(soluzione.size()-1);
					}
				} else {
					if (c.getCounter()<NUMERO_GIORNI_CITTA_MAX && (c.getRilevamenti().get(giorno).getUmidita() + costo + COST) < costoMin && nuovaCitta) {
						soluzione.add(c);
						c.increaseCounter();
						ricorsivo(giorno + 1, soluzione, opzioni, c.getRilevamenti().get(giorno).getUmidita() + costo + COST, c);
						c.decreaseCounter();
						soluzione.remove(soluzione.size()-1);
					}
				}
			}
		}
	}
	
}

