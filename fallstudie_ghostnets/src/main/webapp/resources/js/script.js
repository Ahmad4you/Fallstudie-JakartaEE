var currentMarker = null;

function handlePointClick(event) {
    if (currentMarker === null) {
        // Koordinaten des geklickten Punkts abrufen
        var lat = event.latLng.lat();
        var lng = event.latLng.lng();

        // Setze die Werte in die versteckten Felder
        document.getElementById('lat').value = lat;
        document.getElementById('lng').value = lng;

        // Erstelle einen neuen Marker
        currentMarker = new google.maps.Marker({
            position: new google.maps.LatLng(lat, lng),
            map: PF('map').getMap() // Setze den Marker auf die Karte
        });

        // Zeige das Dialogfenster
        PF('dlg').show();

        // Kopiere die Werte in die Eingabefelder
        document.getElementById('form:latitude').value = lat; // Höhe
        document.getElementById('form:longitude').value = lng; // Länge

        // Optional: Wenn die Eingabefelder als PrimeFaces-Widgets definiert sind,
        // dann können Sie sie auch so aktualisieren
        PF('latitudeInput').jq.val(lat); // Aktualisiere das PrimeFaces Eingabefeld
        PF('longitudeInput').jq.val(lng); // Aktualisiere das PrimeFaces Eingabefeld

        // Optional: Backend aktualisieren
        PF('growl').renderMessage({
            summary: 'Koordinaten aktualisiert',
            detail: `Breite: ${lat}, Länge: ${lng}`
        });
    }
}

function markerAddComplete() {
    var title = document.getElementById('title');
    if (currentMarker) {
        currentMarker.setTitle(title.value);
        title.value = "";
    }

    currentMarker = null;
    PF('dlg').hide();
}

function cancel() {
    PF('dlg').hide();
    if (currentMarker) {
        currentMarker.setMap(null);
        currentMarker = null;
    }
    return false;
}

