package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO MDAO;
	private Citta citta;
	private List<Citta> leCitta;
	private List<Citta> best;
	private double costoMinimo=1500.0;
	public Model() {
		MDAO=new MeteoDAO();
		this.leCitta=MDAO.listaCitta();
	}
	



	public String getUmiditaMedia(int mese) {
	
		String umidita="";
		List<Citta> localita=MDAO.listaCitta();
		for(Citta s:localita) {
			umidita+=String.format("%-20s", s);
			umidita+=String.format("%-10s", MDAO.getAvgRilevamentiLocalitaMese(mese, s.getNome()));
			umidita+="\n";
			
		}
	

		return umidita;
	}

	public String trovaSequenza(int mese) {
		String sequenza="";
		for(Citta c:leCitta) {
			c.setRilevamenti(MDAO.getAllRilevamentiLocalitaMese15(mese,c.getNome()));
		
		}

		List<SimpleCity> parziale=new ArrayList<SimpleCity>();
		
		List<SimpleCity> candidata=new ArrayList<SimpleCity>();
		calcola(parziale,mese,0,candidata);
		sequenza=candidata.toString();
		return sequenza;
	}



	private void calcola(List<SimpleCity> parziale, int mese, int passo, List<SimpleCity> candidata) {
		
		if(parziale.size()==NUMERO_GIORNI_TOTALI) {
			if(controllaParziale(parziale)) {
				if(punteggioSoluzione(parziale)<costoMinimo) {
					costoMinimo=punteggioSoluzione(parziale);
					candidata.removeAll(candidata);
					candidata.addAll(parziale);
				}
			}
			return;
		}
	
		for(Citta c:leCitta) {
			System.out.println(c.getCounter());
				if(c.getCounter()<6){
					parziale.add(new SimpleCity(c.getNome(),c.getRilevamenti().get(parziale.size()).getUmidita()));
					System.out.println(parziale);
					c.increaseCounter();
					calcola(parziale, mese, passo+1, candidata);
					citta=new Citta(parziale.get(parziale.size()-1).getNome());
					citta.decreaseCounter();
					parziale.remove(parziale.get(parziale.size()-1));
					System.out.println("siamo qua " +parziale);

				}
				
			
			
//			citta=new Citta(parziale.get(parziale.size()-1).getNome());
//			citta.decreaseCounter();
//			parziale.subList(0, parziale.size()-2);
		
//			System.out.println("siamo qua " +parziale);

		
		}
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;
//		for(SimpleCity s:soluzioneCandidata) {
//			score+=s.getCosto();
//			
//		}
		for(int i=0;i<NUMERO_GIORNI_TOTALI;i++) {
			score+=soluzioneCandidata.get(i).getCosto();
			if(i!=NUMERO_GIORNI_TOTALI-1 && soluzioneCandidata.get(i).getNome().equals(soluzioneCandidata.get(i+1).getNome()))
				score+=100;
		}
			
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		int count=0;
		List<String> lista=new ArrayList<String>();

		for(int i=0;i<12;i++) {
			if(!lista.contains(parziale.get(i).getNome())) {
				count=1;
				if(parziale.get(i).getNome().equals(parziale.get(i+1).getNome())) {
					count=2;
					if(parziale.get(i).getNome().equals(parziale.get(i+2).getNome())) {
						count=3;
						lista.add(parziale.get(i).getNome());
						i+=1;
					}
					i++;
				}
			}
		}
		if(lista.size()==3)
			return true;
		else
			return false;
	}


}
