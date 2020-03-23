package provider;

import java.time.LocalDate;

public class Asteroid {
	private LocalDate date;
	private String name;
	private String distance;
	private Float diameter;
	private Boolean hazardous;
	
	protected Asteroid(LocalDate dateOf, String name, String distanceString, Float diameter, Boolean ifHazardous) {
		super();
		this.date = dateOf;
		this.name = name;
		this.distance = distanceString;
		this.diameter = diameter;
		this.hazardous = ifHazardous;
	}
	
	@Override
	public String toString() {
		
		String distanceWithoutPoint = distance.substring(0, distance.indexOf("."));
		Integer distanceInt = Integer.parseInt(distanceWithoutPoint);
		Float distanceFloat = distanceInt/1000000f;
		
		return String.format("%s - %-24s: %-6.2f mln km from Earth, %-7.3f km is minimal diameter, %-17s;", date.toString(), name, distanceFloat, diameter,
				                                                                             (hazardous == true ? "it is hazardous" : "it is't hazardous"));
	}

	
	
	protected LocalDate getDate() {
		return date;
	}

	protected void setDate(LocalDate date) {
		this.date = date;
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getDistance() {
		return distance;
	}

	protected void setDistance(String distance) {
		this.distance = distance;
	}

	protected Float getDiameter() {
		return diameter;
	}

	protected void setDiameter(Float diameter) {
		this.diameter = diameter;
	}

	protected Boolean getHazardous() {
		return hazardous;
	}

	protected void setHazardous(Boolean hazardous) {
		this.hazardous = hazardous;
	}
}
