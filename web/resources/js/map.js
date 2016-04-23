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
    document.getElementById('map-form:latitude').value = latitude;
    document.getElementById('map-form:longitude').value = longitude;
    update();
}