/*
 * Weather widget using GeoLocation API and OpenWeather API to display on user's profile 
 * @author Corey McQuay
 * @4/24/2015
 */

//Gets the user's geolocation and then uses the latitude and longitude to make 
//and API request to the OpenWeather API 

//Define the global variables
//Current weather URL
var BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
var UrlParams = "&units=imperial&type=accurate&mode=json";
// forecast URL
var Forecast_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
var ForeCast_Params = "&cnt=5&units=imperial&type=accurate&mode=json";
// Image base URL
var IMG_URL = "http://openweathermap.org/img/w/";

/* Initial function call to determine the user location using GeoLocation API Loads it on the webpage*/
window.onload=function getLocation() {
	if (navigator.geolocation) {
		var timeoutVal = 10 * 1000 * 1000; //Timeout error
		navigator.geolocation.getCurrentPosition(getCurrentWeatherData,
				displayError, {
					enableHighAccuracy : true,
					timeout : timeoutVal,
					maximumAge : 0
				});
	} else {
		alert("Geolocation is not supported by this browser"); //Error handling
	}
}
// Get the Current Weather for User location
function getCurrentWeatherData(position) {
	// Build the OpenAPI URL for current Weather
	var WeatherNowAPIurl = BASE_URL + "lat=" + position.coords.latitude
			+ "&lon=" + position.coords.longitude + UrlParams + "&appid=8a6cc5f72854b75f1a3c04a8aaef5a0a";
	var WeatherForecast_url = Forecast_URL + "lat=" + position.coords.latitude
			+ "&lon=" + position.coords.longitude + ForeCast_Params + "&appid=8a6cc5f72854b75f1a3c04a8aaef5a0a";
	// OpenWeather API call for Current Weather
	var xmlhttp = new XMLHttpRequest();
        //Checks the state of the request status then it will start parsing if no errors 
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
	xmlhr.onreadystatechange = function() { //Correct status, pulled data correctly
		if (xmlhr.readyState == 4 && xmlhr.status == 200) {
			var JSobj = JSON.parse(xmlhr.responseText);
			Forecast(JSobj);
		}
	}
	xmlhr.open("GET", WeatherForecast_url, true);
	xmlhr.send();

}
// Error Handler will display to user if when of the following happens
function displayError(error) {
	var errors = {
		1 : 'Permission denied',
		2 : 'Position unavailable',
		3 : 'Request timeout'
	};
	alert("Error: " + errors[error.code]);
}
// Display the current weather and location

//Parses the json obect and puts the data onto the web page
function Parse(obj) {
	// current Location
	document.getElementById("location").innerHTML = "Country: ".bold()
			+ obj.sys.country + "<br>" + "City: ".bold() + obj.name + "<br>"
			+ "Latitude: ".bold() + obj.coord.lat + "<br>" + "Longitude: ".bold()
			+ obj.coord.lon + "<br>";

	// current weather
	document.getElementById("weatherNow").innerHTML = "<img src='" + IMG_URL
			+ obj.weather[0].icon + ".png'> " + "<br> Condition: ".bold()
			+ obj.weather[0].description + "<br>" + "Temp: ".bold() + obj.main.temp
			+ " F<br>" + "Humidity: ".bold() + obj.main.humidity + " hPa <br>"
			+ "Cloudiness: ".bold() + obj.clouds.all + "% <br>" + "Wind: ".bold()
			+ obj.wind.speed + " mps <br>";

}
// Display forecasts for next 5 Days by parsing through json object and putting 
// the data onto the web page.
function Forecast(obj) {
	document.getElementById("day1div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[0].weather[0].icon + ".png'> " + "<br>Min Temp: ".bold()
			+ obj.list[0].temp.min + " F<br>" + "Max Temp: ".bold()
			+ obj.list[0].temp.max + " F<br>" + "Weather: ".bold()
			+ obj.list[0].weather[0].description + "<br>" + "Cloudiness: ".bold()
			+ obj.list[0].clouds + " %<br>" + "Wind: ".bold() + obj.list[0].speed
			+ " mps <br>";

	document.getElementById("day2div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[1].weather[0].icon + ".png'> " + "<br> Min Temp: ".bold()
			+ obj.list[1].temp.min + " F<br>" + "Max Temp: ".bold()
			+ obj.list[1].temp.max + " F<br>" + "Weather: ".bold()
			+ obj.list[1].weather[0].description + "<br>" + "Cloudiness: ".bold()
			+ obj.list[1].clouds + " %<br>" + "Wind: ".bold() + obj.list[1].speed
			+ " mps <br>";
	document.getElementById("day3div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[2].weather[0].icon + ".png'> " + "<br>Min Temp: ".bold()
			+ obj.list[2].temp.min + " F<br>" + "Max Temp: ".bold()
			+ obj.list[2].temp.max + " F<br>" + "Weather: ".bold()
			+ obj.list[2].weather[0].description + "<br>" + "Cloudiness: ".bold()
			+ obj.list[2].clouds + " %<br>" + "Wind: ".bold() + obj.list[2].speed
			+ " mps <br>";
	document.getElementById("day4div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[3].weather[0].icon + ".png'> " + "<br>Min Temp: ".bold()
			+ obj.list[3].temp.min + " F<br>" + "Max Temp: ".bold()
			+ obj.list[3].temp.max + " F<br>" + "Weather: ".bold()
			+ obj.list[3].weather[0].description + "<br>" + "Cloudiness: ".bold()
			+ obj.list[3].clouds + " %<br>" + "Wind: ".bold() + obj.list[3].speed
			+ " mps <br>";
	document.getElementById("day5div").innerHTML = "<img src='" + IMG_URL
			+ obj.list[4].weather[0].icon + ".png'> " + "<br> Min Temp: ".bold()
			+ obj.list[4].temp.min + " F<br>" + "Max Temp: ".bold()
			+ obj.list[4].temp.max + " F<br>" + "Weather: ".bold()
			+ obj.list[4].weather[0].description + "<br>" + "Cloudiness: ".bold()
			+ obj.list[4].clouds + " %<br>" + "Wind: ".bold() + obj.list[4].speed
			+ " mps <br>";
}