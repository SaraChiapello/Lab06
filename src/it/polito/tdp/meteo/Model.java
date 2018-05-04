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
	private double costoMinimo=1500.0;
	public Model() {
		MDAO=new MeteoDAO();
	}
	
	public List<Citta> listaCitta(){
		return MDAO.listaCitta();
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
		for(Citta c:listaCitta()) {
//			c.setRilevamenti(getAllRilevamentiMese15(mese));
			System.out.println(c.getRilevamenti().toString());
		}
		List<SimpleCity> parziale=new ArrayList<SimpleCity>();
		
		List<SimpleCity> candidata=new ArrayList<SimpleCity>();
		calcola(parziale,mese,0,candidata);
		sequenza=candidata.toString();
		return sequenza;
	}



	private void calcola(List<SimpleCity> parziale, int mese, int passo, List<SimpleCity> candidata) {
		// TODO Auto-generated method stub
		
		if(passo==15) {
			if(controllaParziale(parziale)) {
				
				if(punteggioSoluzione(parziale)<costoMinimo) {
					costoMinimo=punteggioSoluzione(parziale);
					candidata.removeAll(candidata);
					candidata.addAll(parziale);
				}
			}
				
			return;
		}
		for(int i=0;i<15;i++) {

			for(int j=0;j<3;j++) {

				if(listaCitta().get(j).getCounter()<=6){
					System.out.println(listaCitta().get(j).getNome());
					System.out.println(listaCitta().get(j).getRilevamenti().get(i).getUmidita());
				parziale.add(new SimpleCity(listaCitta().get(j).getNome(),listaCitta().get(j).getRilevamenti().get(i).getUmidita()));

				listaCitta().get(j).increaseCounter();
				calcola(parziale, mese, passo+1, candidata);
				parziale.subList(0, parziale.size()-1);
				}
			}
		}
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;
		for(SimpleCity s:soluzioneCandidata)
			score+=s.getCosto();
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
