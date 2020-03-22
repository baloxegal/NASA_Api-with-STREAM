package provider;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.time.LocalDate;

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
        
        for (LocalDate dateOf = start; dateOf.isBefore(end.plusDays(1)); dateOf = dateOf.plusDays(1)) {
        	for(int asteroidArrayIndex = 0; asteroidArrayIndex < data.getJSONObject("near_earth_objects")
        															 .getJSONArray(dateOf.toString()).length();
        																										++asteroidArrayIndex) {
        		String name = data.getJSONObject("near_earth_objects")
        						  .getJSONArray(dateOf.toString())
        						  .getJSONObject(asteroidArrayIndex)
        						  .getString("name");
  		
        		String distanceString = data.getJSONObject("near_earth_objects")
        						  	 		.getJSONArray(dateOf.toString())
        						  	 		.getJSONObject(asteroidArrayIndex)
        						  	 		.getJSONArray("close_approach_data")
        						  	 		.getJSONObject(0)
        						  	 		.getJSONObject("miss_distance")
        						  	 		.getString("kilometers");
        		
        		String distanceWithoutPoint = distanceString.substring(0, distanceString.indexOf("."));
        		Integer distanceInt = Integer.parseInt(distanceWithoutPoint);
        		Float distance = distanceInt/1000000f;

        		Float diameter = data.getJSONObject("near_earth_objects")
        						  	 .getJSONArray(dateOf.toString())
        						  	 .getJSONObject(asteroidArrayIndex)
        						  	 .getJSONObject("estimated_diameter")
        						  	 .getJSONObject("kilometers")
        						  	 .getFloat("estimated_diameter_min");

        		Boolean ifHazardous = data.getJSONObject("near_earth_objects")
        						  		  .getJSONArray(dateOf.toString())
        						  		  .getJSONObject(asteroidArrayIndex)
        						  		  .getBoolean("is_potentially_hazardous_asteroid");

        		System.out.printf("%s - %-19s: %-6.2f mln km from Earth, %-7.3f km is minimal diameter, %-17s;%n",dateOf.toString(), name,
        												distance, diameter, (ifHazardous == true ? "it is hazardous" : "it is't hazardous"));
         	}
        }
	}
}
