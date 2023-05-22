package org.java.sql.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		
		List<Integer> idList = new ArrayList<>();
		
		try(Scanner sc = new Scanner(System.in); Connection con = DriverManager.getConnection(url, user, password)) {
			
			System.out.println("Esegui una ricerca:");
			String searchField = sc.nextLine();

			String sql = "SELECT countries.name, countries.country_id, regions.name, continents.name "
					+ "FROM countries "
					+ "JOIN regions "
					+ "ON countries.region_id = regions.region_id "
					+ "JOIN continents "
					+ "ON regions.continent_id = continents.continent_id "
					+ "WHERE countries.name LIKE ?"
					+ "ORDER BY countries.name;";
			
			
			try(PreparedStatement ps = con.prepareStatement(sql)){
				
				ps.setString(1, "%" + searchField + "%");
				
				try(ResultSet rs = ps.executeQuery()) {
					
					System.out.println("Nazione - ID - Regione - Continente");
					
					while(rs.next()) {
						final String countryName = rs.getString(1);
						final int countryId = rs.getInt(2);
						idList.add(countryId);
						final String regionName = rs.getString(3);
						final String continentName = rs.getString(4);
						System.out.println(countryName + " - " + countryId + " - " + regionName + " - " + continentName);
					}
					
				} catch (SQLException ex) {
					System.err.println("ERROR");
				}
				
				System.out.println("Scegli un id");
				int choosenId = sc.nextInt();
				sc.nextLine();
				
				if (idList.contains(choosenId)) {
					
					List<String> languagesList = new ArrayList<>();
					String nation = null;
					String year = null;
					String population = null;
					String gdp = null;
					
					
					String languagesQuery = "SELECT countries.name, languages.language\r\n"
							+ "FROM countries\r\n"
							+ "JOIN country_languages\r\n"
							+ "ON countries.country_id = country_languages.country_id \r\n"
							+ "JOIN languages\r\n"
							+ "ON country_languages.language_id = languages.language_id\r\n"
							+ "WHERE countries.country_id = ?";
					
					try(PreparedStatement ps2 = con.prepareStatement(languagesQuery)){
						
						ps2.setInt(1, choosenId);
						
						try(ResultSet rs2 = ps2.executeQuery()){
							
							while(rs2.next()) {
								nation = rs2.getString(1);
								final String language = rs2.getString(2);
								languagesList.add(language);
							}
							
						}
						
						
					} catch(SQLException ex) {
						System.out.println("Query Error");
					}
					
					String detailsQuery = "SELECT * "
							+ "FROM country_stats "
							+ "WHERE country_id = ? "
							+ "GROUP BY country_stats.`year` "
							+ "ORDER BY country_stats.`year` DESC "
							+ "LIMIT 1 ";
					
					try(PreparedStatement ps3 = con.prepareStatement(detailsQuery)){
						
						ps3.setInt(1, choosenId);
						
						try(ResultSet rs3 = ps3.executeQuery()){
							
							while(rs3.next()) {
								year = rs3.getString(2);
								population = rs3.getString(3);
								gdp = rs3.getString(4);
							}
							
						}
						
						
					} catch(SQLException ex) {
						System.out.println("Query Error");
					}
					
					String languages = "";
					
					for(int i = 0; i < languagesList.size(); i++) {
						
						if(i == languagesList.size() - 1) {
							languages += languagesList.get(i) + ".";
						} else {
							languages += languagesList.get(i) + ", ";
						}
						
					}
					
					System.out.println("Dettagli per " + nation);
					System.out.println("languages: " + languages);
					System.out.println("Most recent info: " + year);
					System.out.println("Population: " + population);
					System.out.println("GDP: " + gdp);
					
					
				} else {
					System.out.println("Id non incluso");
				}
				
			} catch (SQLException ex) {
				System.err.println("Query Error");
			}
			
			
			
			
			
		}catch (SQLException ex){
			System.err.println("Connection error");
		}

	}

}
