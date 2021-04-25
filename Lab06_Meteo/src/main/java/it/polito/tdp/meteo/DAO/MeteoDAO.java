package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		String sql = "SELECT Umidita, Data "
				+ "FROM situazione "
				+ "WHERE Localita=? AND MONTH(Data)=?";
		
		try {
			
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql); 
			st.setString(1, localita);
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();
			
			List<Rilevamento> risultato = new ArrayList<Rilevamento>();
			
			while (rs.next()) {
				LocalDate data = rs.getDate("Data").toLocalDate();
				risultato.add(new Rilevamento(localita, data, rs.getInt("Umidita")));
			}
			
			rs.close();
			st.close();
			conn.close();
			
			return risultato;
			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<String> getCitta() {
		String sql = "SELECT DISTINCT Localita "
				+ "FROM situazione";
		
		try {
			
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql); 
			ResultSet rs = st.executeQuery();
			
			List<String> risultato = new ArrayList<String>();
			
			while (rs.next()) {
				risultato.add(rs.getString("Localita"));
			}
			
			rs.close();
			st.close();
			conn.close();
			
			return risultato;
			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	

}
