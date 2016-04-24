/*
 * Weather widget using GeoLocation API and OpenWeather API
 * @author Corey McQuay
 * @4/24/2015
 */
// Note: This example requires that you consent to location sharing when
// prompted by your browser. If you see a blank space instead of the map, this
// is probably because you have denied permission for location sharing.

//define the global variables
//current weather URL
var BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
var UrlParams = "&units=imperial&type=accurate&mode=json";
// forecast URL
var Forecast_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
var ForeCast_Params = "&cnt=5&units=imperial&type=accurate&mode=json";
// Image base URL
var IMG_URL = "http://openweathermap.org/img/w/";

/* Initial function call to determine the user location using GeoLocation API */
window.onload=function getLocation() {
	if (navigator.geolocation) {
		var timeoutVal = 10 * 1000 * 1000;
		navigator.geolocation.getCurrentPosition(getCurrentWeatherData,
				displayError, {
					enableHighAccuracy : true,
					timeout : timeoutVal,
					maximumAge : 0
				});
	} else {
		alert("Geolocation is not supported by this browser");
	}
}
// get the Current Weather for User location
function getCurrentWeatherData(position) {
	// Build the OpenAPI URL for current Weather
	var WeatherNowAPIurl = BASE_URL + "lat=" + position.coords.latitude
			+ "&lon=" + position.coords.longitude + UrlParams + "&appid=8a6cc5f72854b75f1a3c04a8aaef5a0a";
	var WeatherForecast_url = Forecast_URL + "lat=" + position.coords.latitude
			+ "&lon=" + position.coords.longitude + ForeCast_Params + "&appid=8a6cc5f72854b75f1a3c04a8aaef5a0a";
	// OpenWeather API call for Current Weather
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var JSONobj = JSON.parse(xmlhttp.responseText);
			Parse(JSONobj);
		}
	}
	xmlhttp.open("GET", WeatherNowAPIurl, true);
	xmlhttp.send();

	// OpenWeather API call for Forecast Weather
	var xmlhr = new XMLHttpRequest();
	xmlhr.onreadystatechange = function() {
		if (xmlhr.readyState == 4 && xmlhr.status == 200) {
			var JSobj = JSON.parse(xmlhr.responseText);
			Forecast(JSobj);
		}
	}
	xmlhr.open("GET", WeatherForecast_url, true);
	xmlhr.send();

}
// Error Handler
function displayError(error) {
	var errors = {
		1 : 'Permission denied',
		2 : 'Position unavailable',
		3 : 'Request timeout'
	};
	alert("Error: " + errors[error.code]);
}
// display the current weather and location

function Parse(obj) {
	// current Location
	document.getElementById("location").innerHTML = "Country: "
			+ obj.sys.country + "<br>" + "City: " + obj.name + "<br>"
			+ "Latitude: " + obj.coord.lat + "<br>" + "Longitude: "
			+ obj.coord.lon + "<br>";

	// current weather
	document.getElementById("weatherNow").innerHTML = "<img src='" + IMG_URL
			+ obj.weather[0].icon + ".png'> " + "<br> Condition: "
			+ obj.weather[0].description + "<br>" + "Temp: " + obj.main.temp
			+ " F<br>" + "Humidity: " + obj.main.humidity + " hPa <br>"
			+ "Cloudiness: " + obj.clouds.all + "% <br>" + "Wind: "
			+ obj.wind.speed + " mps <br>";

}
// display forecasts for next 5 Days
function Forecast(obj) {
	document.getElementById("day1div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[0].weather[0].icon + ".png'> " + "<br>Min Temp: "
			+ obj.list[0].temp.min + " F<br>" + "Max Temp: "
			+ obj.list[0].temp.max + " F<br>" + "Weather: "
			+ obj.list[0].weather[0].description + "<br>" + "Cloudiness: "
			+ obj.list[0].clouds + " %<br>" + "Wind: " + obj.list[0].speed
			+ " mps <br>";

	document.getElementById("day2div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[1].weather[0].icon + ".png'> " + "<br> Min Temp: "
			+ obj.list[1].temp.min + " F<br>" + "Max Temp: "
			+ obj.list[1].temp.max + " F<br>" + "Weather: "
			+ obj.list[1].weather[0].description + "<br>" + "Cloudiness: "
			+ obj.list[1].clouds + " %<br>" + "Wind: " + obj.list[1].speed
			+ " mps <br>";
	document.getElementById("day3div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[2].weather[0].icon + ".png'> " + "<br>Min Temp: "
			+ obj.list[2].temp.min + " F<br>" + "Max Temp: "
			+ obj.list[2].temp.max + " F<br>" + "Weather: "
			+ obj.list[2].weather[0].description + "<br>" + "Cloudiness: "
			+ obj.list[2].clouds + " %<br>" + "Wind: " + obj.list[2].speed
			+ " mps <br>";
	document.getElementById("day4div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[3].weather[0].icon + ".png'> " + "<br>Min Temp: "
			+ obj.list[3].temp.min + " F<br>" + "Max Temp: "
			+ obj.list[3].temp.max + " F<br>" + "Weather: "
			+ obj.list[3].weather[0].description + "<br>" + "Cloudiness: "
			+ obj.list[3].clouds + " %<br>" + "Wind: " + obj.list[3].speed
			+ " mps <br>";
	document.getElementById("day5div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[4].weather[0].icon + ".png'> " + "<br> Min Temp: "
			+ obj.list[4].temp.min + " F<br>" + "Max Temp: "
			+ obj.list[4].temp.max + " F<br>" + "Weather: "
			+ obj.list[4].weather[0].description + "<br>" + "Cloudiness: "
			+ obj.list[4].clouds + " %<br>" + "Wind: " + obj.list[4].speed
			+ " mps <br>";
}