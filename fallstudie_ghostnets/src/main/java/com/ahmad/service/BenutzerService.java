package com.ahmad.service;

import java.util.List;

import com.ahmad.dao.BenutzerDAO;
import com.ahmad.model.Benutzer;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

/**
 * 
 * @author Ahmad Alrefai
 */

@Stateless
public class BenutzerService {
	
	@Inject
	private BenutzerDAO benutzerDAO;
	
	 public void saveBenutzer(Benutzer benutzer) {
		 benutzerDAO.saveBenutzer(benutzer);
	 }
	 
	 public Benutzer getBenutzerByEmail(String email) {
		 return benutzerDAO.getBenutzerByEmail(email);
	 }

	 
	 public List<Benutzer> getAllBenutzer() {
		 return benutzerDAO.getAllBenutzer();
	 }

}
