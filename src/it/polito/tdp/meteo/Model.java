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
		List<Citta> parziale=new ArrayList<Citta>();
		
		cerca(parziale,0);
//		sequenza=candidata.toString();
		return best.toString();//+" "+punteggioSoluzione(best);
	}



	private void cerca(List<Citta> parziale, int livello) {
		
		if(parziale.size()==NUMERO_GIORNI_TOTALI) {
//			System.out.println(parziale);
			Double costo=punteggioSoluzione(parziale);
			System.out.println(costo+" "+parziale);

			if(best==null||costo<punteggioSoluzione(best)) {

				best=new ArrayList<>(parziale);
				System.out.println("migliore   "+parziale);

			}
			return;
		}
	
		for(Citta c:leCitta) {

			if(aggiuntaValida(c,parziale)) {
				parziale.add(c);
				cerca(parziale,livello+1);
				parziale.remove(parziale.size()-1);
			}
		
		}
	}

	private boolean aggiuntaValida(Citta c, List<Citta> parziale) {
		// TODO Auto-generated method stub
		int count=0;
		if(parziale.size()==0)
			return true;
		int i=parziale.size()-1;
		for(Citta l:parziale) {
			if(c.equals(l))
				count++;
			
			
		}
		if(count>=NUMERO_GIORNI_CITTA_MAX)
			return false;

		if(i<3 )
			return parziale.get(0).equals(c);
		if(i==13||i==12) //così ho considerato che anche al giorno 15 debba essere il terzo giorno dello stesso posto
			return parziale.get(i).equals(c);
		if(parziale.get(i).equals(parziale.get(i-1)) &&parziale.get(i).equals(parziale.get(i-2)))
			return true;
		if(parziale.get(i).equals(parziale.get(i-1)) &&!parziale.get(i).equals(parziale.get(i-2)))
			return parziale.get(i).equals(c);
		if(parziale.get(i).equals(c))
			return true;
		
		return false;
	}




	private Double punteggioSoluzione(List<Citta> soluzioneCandidata) {
		double score = 0.0;
//		for(SimpleCity s:soluzioneCandidata) {
//			score+=s.getCosto();
//			
//		}
		for(int i=0;i<NUMERO_GIORNI_TOTALI;i++) {
			score+=soluzioneCandidata.get(i).getRilevamenti().get(i).getUmidita();
			if(i!=NUMERO_GIORNI_TOTALI-1 && !soluzioneCandidata.get(i).getNome().equals(soluzioneCandidata.get(i+1).getNome()))
				score+=100;
		}
			
		return score;
	}

	

}
