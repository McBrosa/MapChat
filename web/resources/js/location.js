/* 
 * Created by Nathan Rosa on 2016.03.26  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
var currentMarker = null;
if (navigator.geolocation) {
    checkGeolocationByHTML5();
} else {
    checkGeolocationByLoaderAPI(); // HTML5 not supported! Fall back to Loader API.
}

function checkGeolocationByHTML5() {
    navigator.geolocation.getCurrentPosition(function(position) {
        setMapCenter(position.coords.latitude, position.coords.longitude);
    }, function() {
        checkGeolocationByLoaderAPI(); // Error! Fall back to Loader API.
    });
}

function checkGeolocationByLoaderAPI() {
    if (google.loader.ClientLocation) {
        setMapCenter(google.loader.ClientLocation.latitude, google.loader.ClientLocation.longitude);
    } else {
        // Unsupported! Show error/warning?
    }
}

function setMapCenter(latitude, longitude) {
    PF('map').getMap().setCenter(new google.maps.LatLng(latitude, longitude));
    document.getElementById('formId:latitude').value = latitude;
    document.getElementById('formId:longitude').value = longitude;
}

function handlePointClick(event) {
    if(currentMarker === null) {
        document.getElementById('lat').value = event.latLng.lat();
        document.getElementById('lng').value = event.latLng.lng();

        currentMarker = new google.maps.Marker({
            position:new google.maps.LatLng(event.latLng.lat(), event.latLng.lng())
        });

        PF('map').addOverlay(currentMarker);

        PF('dlg').show();
    }   
}

function markerAddComplete() {
    var title = document.getElementById('title');
    currentMarker.setTitle(title.value);
    title.value = "";

    currentMarker = null;
    PF('dlg').hide();
}

function cancel() {
    PF('dlg').hide();
    currentMarker.setMap(null);
    currentMarker = null;

    return false;
}

