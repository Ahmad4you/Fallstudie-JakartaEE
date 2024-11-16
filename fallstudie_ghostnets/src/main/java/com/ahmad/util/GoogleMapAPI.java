package com.ahmad.util;

import java.io.Serializable;
import java.util.List;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.ahmad.bean.GhostnetBean;
import com.ahmad.model.Ghostnet;
import com.ahmad.service.GhostnetService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * 
 * @author Ahmad Alrefai
 */

@Named
@ViewScoped
public class GoogleMapAPI implements Serializable {

	private static final long serialVersionUID = -7404502900872973245L;
	private String googleMapsApiKey;
	private List<Ghostnet> ghostnetList;
	private MapModel<Long> mapModel;
	private Marker<Long> marker;
	private MapModel<Object> emptyModel;
	private String title;
	    private double lat;
	    private double lng;

	@Inject
	private transient GhostnetService ghostnetService;
	
	 @Inject
	 private GhostnetBean ghostnetBean;

	public GoogleMapAPI() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void init() {
		emptyModel = new DefaultMapModel<>();
		googleMapsApiKey = "zaSyDdnyXQqA5q1y9I7uvFtQLVPzDBZq_LFA8";
		
		loadMarkers();
    }
	
	public void onMarkerSelect(OverlaySelectEvent<Long> event) {
        marker = (Marker) event.getOverlay();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Marker " + marker.getId() + " Selected" + 
                " Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng(), marker.getTitle()) );
    }
	
	public void addMarker() {
        Marker<Object> marker = new Marker<>(new LatLng(lat, lng), title);
        emptyModel.addOverlay(marker);
        
     // Setzt die Koordinaten in GhostnetBean (im Eingabeformular)
        ghostnetBean.getGhostnet().setBreite(lat);
        ghostnetBean.getGhostnet().setLaenge(lng);
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));
    }
	
	public void loadMarkers() {
//        mapModel.getMarkers().clear(); // Entfernt alle aktuellen Marker

        ghostnetList = loadGhostnets();
        mapModel = new DefaultMapModel<Long>();
        
        // Fügen für jedes Ghostnet einen Marker zum MapModel hinzu.
        // die noch nicht geborgenen Netze auf einer Weltkarte zu sehen
        for (Ghostnet ghostnet : ghostnetList) {
            if (ghostnet.getBreite() != null && ghostnet.getLaenge() != null && ghostnet.getStatus() != GhostnetStatus.GEBORGEN) {
                // Erstellen Sie die Beschreibung für das InfoWindow
                String infoContent = "Größe: " + ghostnet.getSize() + " m² |" +
                                     "Status: " + ghostnet.getStatus() + "|" +
                                     "Sichtungsdatum: " + ghostnet.getSichtungDatum();
                
                // Marker hinzufügen und InfoContent im Data-Attribut speichern
                marker = new Marker<Long>(new LatLng(ghostnet.getBreite(), ghostnet.getLaenge()), 
                                           "Ghostnet ID: " + ghostnet.getId());
                marker.setTitle(infoContent);
                mapModel.addOverlay(marker);
            }
        }
    }

	private List<Ghostnet> loadGhostnets() {
		
		return ghostnetService.getAllGhostnets();
	}
	
	public String getGoogleMapsApiKey() {
		return googleMapsApiKey;
	}
	
	public MapModel<Long> getMapModel() {
        return mapModel;
    }
	
	 
	 public MapModel<Object> getEmptyModel() {
	        return emptyModel;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public double getLat() {
	        return lat;
	    }

	    public void setLat(double lat) {
	        this.lat = lat;
	    }

	    public double getLng() {
	        return lng;
	    }

	    public void setLng(double lng) {
	        this.lng = lng;
	    }
}
