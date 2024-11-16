package com.ahmad.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.ahmad.controller.UserSession;
import com.ahmad.model.Ghostnet;
import com.ahmad.service.GhostnetService;
import com.ahmad.util.GhostnetStatus;
import com.ahmad.util.GoogleMapAPI;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Für die Integration einer Google Maps-Karte und das Erfassen und Anzeigen der Geisternetzdaten in der Web-App gibt es eine einfache, 
 * effektive Lösung, die PrimeFaces-Komponenten und Google Maps nutzt. Die Karte zeigt Geisternetz-Standorte und ermöglicht es dem Benutzer, 
 * per Klick Standorte zu wählen, welche dann automatisch in die Eingabefelder für die Klasseneigenschaften 
 * (z. B. breite, laenge, size, sichtungDatum, status, retriever) übernommen werden. Zusätzlich können wir es so gestalten, 
 * dass Eingaben in die Felder auf der Karte aktualisiert werden.
 * 
 * Die JSF-Seite nutzt PrimeFaces-Komponenten für Eingabefelder und eine Google Maps-Integration. 
 * Bei Klick auf die Karte werden die Koordinaten automatisch in die Eingabefelder kopiert und das Geisternetz-Objekt aktualisiert.
 * 
 * @author Ahmad Alrefai
 */
@Named
@ViewScoped
public class GhostnetBean implements Serializable {
    
	private static final long serialVersionUID = 4745178773161299527L;
	 private Ghostnet ghostnet;
	    private double breite;
	    private double laenge;

	    @Inject
	    private transient GhostnetService ghostnetService;

	    @Inject
	    private UserSession userSession; // Injected Session Scoped Bean for logged-in user
	    
	    @Inject
		private DashboardBean dashboardBean;
	    
	    @Inject
	    private GoogleMapAPI googleMapAPI;

	    @PostConstruct
	    public void init() {
	    	ghostnet = new Ghostnet();
	        ghostnet.setSichtungDatum(new Date());
	     // beim Marker adding sollte Tabellen neu geladen werden.
	        dashboardBean.init();
	    }

	    public void saveGhostnet() {
	    	FacesContext context = FacesContext.getCurrentInstance();
	    	
	        try {
	            ghostnetService.saveGhostnet(ghostnet); // Speichert das Ghostnet in der DB
	            
	            init();
	            googleMapAPI.loadMarkers();
	            
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
	                    ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
	                        .getString("custom.elfolgreich.speichern"), null));
	        } catch (Exception e) {
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	                    ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
	                        .getString("custom.error.speichern.scheitert"), null));
	            e.printStackTrace();
	        }
	    }
	    
	    // Methode zum Aktualisieren der Koordinaten per Kartenklick
	    public void updateCoordinates() {
	        this.ghostnet.setBreite(this.breite);
	        this.ghostnet.setLaenge(this.laenge);
	        System.out.println("Koordinaten aktualisiert: Breite = " + breite + ", Länge = " + laenge);
	    }
	    
	 // Methode zur Rückgabe aller Enum-Werte für die Anzeige im Menü
	    public List<GhostnetStatus> getStatusValues() {
	        return Arrays.asList(GhostnetStatus.values());
	    }
	    
//	    // Methode zum Zurücksetzen des Formulars
//	    public void resetForm() {
//	        this.ghostnet = new Ghostnet(); // Erzeugt eine neue Instanz, um das Formular zu leeren
//	    }
	    
	    public Ghostnet getGhostnet() {
	        return ghostnet;
	    }

	    public void setGhostnet(Ghostnet ghostnet) {
	        this.ghostnet = ghostnet;
	    }

	    public double getBreite() {
	        return breite;
	    }

	    public void setBreite(double breite) {
	        this.breite = breite;
	    }

	    public double getLaenge() {
	        return laenge;
	    }

	    public void setLaenge(double laenge) {
	        this.laenge = laenge;
	    }
	    
}
