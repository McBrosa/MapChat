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
//    PF('map').getMap().setCenter(new google.maps.LatLng(latitude, longitude));
    document.getElementById('formId:latitude').value = latitude;
    document.getElementById('formId:longitude').value = longitude;

//    var icon = {
//        //url: document.getElementById('formId:profilePic').value, // url
//        scaledSize: new google.maps.Size(50, 50) // scaled size
//                  origin: new google.maps.Point(0,0), // origin
//                  anchor: new google.maps.Point(0, 0) // anchor
//  };

//    currentMarker = new google.maps.Marker({
//        position:new google.maps.LatLng(latitude, longitude),
//        icon:icon
//    });
//    PF('map').addOverlay(currentMarker);
    update();
}