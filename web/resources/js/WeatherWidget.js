/*
 * Gets the user's latitude and longitude from the either the logged-in user bean
 * or user being viewed  bean to make 
 * an API request to the OpenWeather API to display real time data to the user
 * on their profile page
 * 
 * @author Corey McQuay
 * @4/24/2015
 */

//Define the global variables
//Current weather URL
var BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
var UrlParams = "&units=imperial&type=accurate&mode=json";
// forecast URL
var Forecast_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
var ForeCast_Params = "&cnt=5&units=imperial&type=accurate&mode=json";
// Image base URL
var IMG_URL = "http://openweathermap.org/img/w/";

//Put your api key for open weather here.
var api_key = "8a6cc5f72854b75f1a3c04a8aaef5a0a"


/**
 * "Main method" that grabs the user's location and then calls
 * on the method to get the current weather data for ths user's location
 * the .onload allows this function to be invoked when the user loads the 
 * profile.xhtml page 
 */


/**
 * Get the Current Weather for User location
 * 
 * @param latitude The latitude coordinate of the user's location
 * @param longitude The longitude coordinate of the user's location
 */
function getCurrentWeatherData(latitude, longitude) {

    // Build the OpenAPI URL for current Weather for an api call by longitude and latitude
    var WeatherNowAPIurl = BASE_URL + "lat=" + latitude
            + "&lon=" + longitude + UrlParams + "&appid=" + api_key;
    var WeatherForecast_url = Forecast_URL + "lat=" + latitude
            + "&lon=" + longitude + ForeCast_Params + "&appid=" + api_key;
    // OpenWeather API call for Current Weather
    var xmlhttp = new XMLHttpRequest();
    //Checks the state of the request status then it will start parsing  the current user info if no errors 
    xmlhttp.onreadystatechange = function () { //correct status
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            //Storing the api results url into a json object then parsing it to the
            var JSONobj = JSON.parse(xmlhttp.responseText);
            Parse(JSONobj); //Goes to the parse function to pick elements from the data
        }
    }

    xmlhttp.open("GET", WeatherNowAPIurl, true);
    xmlhttp.send();

    // Does the same procedure for the forecast data by opening a xml http request
    var xmlhr = new XMLHttpRequest();
    xmlhr.onreadystatechange = function () { //Correct status, pulled data correctly
        if (xmlhr.readyState == 4 && xmlhr.status == 200) {
            var JSobj = JSON.parse(xmlhr.responseText);
            Forecast(JSobj);
        }
    }
    //Get request so 
    xmlhr.open("GET", WeatherForecast_url, true);
    xmlhr.send();
}
/**
 * Error Handler will display to user if when of the following happens
 * 
 * @param  error the error code that should display if a error does occur.
 */
function displayError(error) {
    var errors = {//error types
        1: 'Permission denied',
        2: 'Position unavailable',
        3: 'Request timeout'
    };
    alert("Error: " + errors[error.code]);
}
// Display the current weather and location

/**
 * Parses the json obect for the user's location and current weather and puts the data onto the web page
 * @param  obj the json object that is recieved from the api with realtime data
 */
function Parse(obj) {
    // Current Location and display it onto the users weather tab on the profile page.
    document.getElementById("location").innerHTML = "Country: ".bold()
            + obj.sys.country + "<br />" + "City: ".bold() + obj.name + "<br />"
            + "Latitude: ".bold() + obj.coord.lat + "<br />" + "Longitude: ".bold()
            + obj.coord.lon + "<br />";

    // Current weather and display it onto the user's weather table cell on the profile page
    document.getElementById("weatherNow").innerHTML = "<img src='" + IMG_URL
            + obj.weather[0].icon + ".png'> " + "<br /> Condition: ".bold()
            + obj.weather[0].description + "<br />" + "Temp: ".bold() + obj.main.temp
            + " F<br />" + "Humidity: ".bold() + obj.main.humidity + " hPa <br />"
            + "Cloudiness: ".bold() + obj.clouds.all + "% <br />" + "Wind: ".bold()
            + obj.wind.speed + " mps <br />";
}

/**
 * Display forecasts for next 5 Days by parsing through json object and putting  the data onto the web page
 * based on the id's in the div tags.
 * 
 * @param obj The json object array (The weather forcast array)
 */
function Forecast(obj) {

    //Display the data based on days in the json object forecast array
    //Current day data and display it into the table cell
    document.getElementById("day1div").innerHTML = "<img src='" + IMG_URL
            + obj.list[0].weather[0].icon + ".png'> " + "<br />Min Temp: ".bold()
            + obj.list[0].temp.min + " F<br />" + "Max Temp: ".bold()
            + obj.list[0].temp.max + " F<br />" + "Weather: ".bold()
            + obj.list[0].weather[0].description + "<br />" + "Cloudiness: ".bold()
            + obj.list[0].clouds + " %<br />" + "Wind: ".bold() + obj.list[0].speed
            + " mps <br />";

    //2nd day data in the respective table cell in the xhtml file
    document.getElementById("day2div").innerHTML = "<img src='" + IMG_URL
            + obj.list[1].weather[0].icon + ".png'> " + "<br /> Min Temp: ".bold()
            + obj.list[1].temp.min + " F<br />" + "Max Temp: ".bold()
            + obj.list[1].temp.max + " F<br />" + "Weather: ".bold()
            + obj.list[1].weather[0].description + "<br />" + "Cloudiness: ".bold()
            + obj.list[1].clouds + " %<br />" + "Wind: ".bold() + obj.list[1].speed
            + " mps <br />";

    //2nd day data in the respective table cell in the xhtml file                
    document.getElementById("day3div").innerHTML = "<img src='" + IMG_URL
            + obj.list[2].weather[0].icon + ".png'> " + "<br>Min Temp: ".bold()
            + obj.list[2].temp.min + " F<br />" + "Max Temp: ".bold()
            + obj.list[2].temp.max + " F<br />" + "Weather: ".bold()
            + obj.list[2].weather[0].description + "<br />" + "Cloudiness: ".bold()
            + obj.list[2].clouds + " %<br />" + "Wind: ".bold() + obj.list[2].speed
            + " mps <br />";

    //4th day data in the respective table cell in the xhtml file       
    document.getElementById("day4div").innerHTML = "<img src='" + IMG_URL
            + obj.list[3].weather[0].icon + ".png'> " + "<br />Min Temp: ".bold()
            + obj.list[3].temp.min + " F<br />" + "Max Temp: ".bold()
            + obj.list[3].temp.max + " F<br />" + "Weather: ".bold()
            + obj.list[3].weather[0].description + "<br />" + "Cloudiness: ".bold()
            + obj.list[3].clouds + " %<br />" + "Wind: ".bold() + obj.list[3].speed
            + " mps <br />";

    //5th day data in the respective table cell in the xhtml file 
    document.getElementById("day5div").innerHTML = "<img src='" + IMG_URL
            + obj.list[4].weather[0].icon + ".png'> " + "<br> Min Temp: ".bold()
            + obj.list[4].temp.min + " F<br />" + "Max Temp: ".bold()
            + obj.list[4].temp.max + " F<br />" + "Weather: ".bold()
            + obj.list[4].weather[0].description + "<br />" + "Cloudiness: ".bold()
            + obj.list[4].clouds + " %<br />" + "Wind: ".bold() + obj.list[4].speed
            + " mps <br />";
    
}