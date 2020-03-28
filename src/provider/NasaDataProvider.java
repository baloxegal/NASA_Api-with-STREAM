package provider;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.time.LocalDate;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class NasaDataProvider {
	
	private final static String ACCESS_KEY = "Ju3eZzO17pcRDwoaDYLhp471lH5XkxmOMuxMAAGz";
	private final static String NEO_ENDPOINT = "https://api.nasa.gov/neo/rest/v1/feed";
	
	public void getNeoAsteroids(LocalDate start, LocalDate end) throws Exception{
		
		//1. Connect to NASA API
		
		URL nasa = new URL(NEO_ENDPOINT + "?start_date=" + start + "&end_date=" + end + "&api_key=" + ACCESS_KEY);
        BufferedReader in = new BufferedReader(new InputStreamReader(nasa.openStream()));
        
        //2. Read data
        
        String inputLine;
        String stringData = "";
        while ((inputLine = in.readLine()) != null){
        	System.out.println(inputLine.replaceAll(",", ",\n"));
        	stringData += inputLine;
        }
        in.close();
        		
		//3. parse JSON
        
        JSONObject data = new JSONObject(stringData);
        
		//4. test some data
        
        int count = data.getInt("element_count");
        System.out.println("Found " + count + " results");
        
        System.out.println("Period is: " + start + " - " + end);
        
        List <Asteroid> asteroidList = new ArrayList<Asteroid>();
        
        data.getJSONObject("near_earth_objects").toMap().values().stream().collect(Collectors.toList()).stream()
          	.forEach(date -> ((JSONObject) date).toMap().values().stream().collect(Collectors.toList()).stream()		
          	.forEach(asteroid -> asteroidList.add(
          		new Asteroid(LocalDate.parse(((JSONObject) asteroid).getJSONArray("close_approach_data")
          					 										.getJSONObject(0)
          					 										.getString("close_approach_date")),
          									 ((JSONObject) asteroid).getString("name"),
          									 ((JSONObject) asteroid).getJSONArray("close_approach_data")
          									 						.getJSONObject(0)
          									 						.getJSONObject("miss_distance")
          									 						.getString("kilometers"),
          									 ((JSONObject) asteroid).getJSONObject("estimated_diameter")
                						  	 						.getJSONObject("kilometers")
                						  	 						.getFloat("estimated_diameter_min"),
          									 ((JSONObject) asteroid).getBoolean("is_potentially_hazardous_asteroid")))));		

        for(Asteroid a : asteroidList) {
        	System.out.println(a);
        }
	}
}
