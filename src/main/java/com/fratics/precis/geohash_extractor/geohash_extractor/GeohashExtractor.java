package com.fratics.precis.geohash_extractor.geohash_extractor;

/**
 * Hello world!
 *
 */
public class GeohashExtractor 
{
    private double latitude  = 0;
    private double longitude = 0;
    String [] possibleDelimiters = {",", ";", ":", "	", "::", " "};

    public GeohashExtractor (double lat, double longit) {
    	latitude = lat;
    	longitude = longit;
    }
    
    
    private double parseTheLatOrLong(String s) throws Exception {
    	StringBuilder sb_latOrLongit = new StringBuilder();
    	double multiplicationFactor = 1.0;
    	for (int i = 0; i < s.length(); i ++ ) {
    		char c = s.charAt(i);
    		if (    ( ((int)c >= 48 ) && ((int)c <= 57) )  || 
    				  ((int)c == 46)  || ((int)c == 45) ) {
    			sb_latOrLongit.append(c);
    		}else if ((int)c == 110) {
    			//look for 'n' for positive latitude
    			multiplicationFactor *= 1.0;
    		} else if ((int)c == 115) {
    			//look for 's' for negative latitude
    			multiplicationFactor *= -1.0;
    		} else if ((int)c == 119) {
    			//look for 'w' for negative longitude
    			multiplicationFactor *= -1.0;
    		} else if ((int)c == 101) {
    			//look for 'e' for positive longitude
    			multiplicationFactor *= 1.0;
    		}
    	}
    	String latOrLongitS = sb_latOrLongit.toString();
    	return ( Double.parseDouble(latOrLongitS) * multiplicationFactor );
    }
    
    private void setLatLong(String lat, String longit) throws Exception{
    	latitude = parseTheLatOrLong(lat);
    	longitude = parseTheLatOrLong(longit);
    }
    
    /*
     * Positive latitude is above the equator (N), and negative latitude is below the equator (S). 
     * Positive longitude is east of the prime meridian, while negative longitude is west of the 
     * prime meridian (a north-south line that runs through a point in England).Dec 27, 2010
     */
    
    public GeohashExtractor (String latLong)  throws Exception {

    	String lat = null;
    	String longit = null;
    	String [] splitter = null;
    	
    	//33.7782° N, 76.5762° E
    	String s = latLong.toLowerCase();

    	for (String delimiter : possibleDelimiters) {
        	splitter = s.split(delimiter, -1);
    		if (splitter.length == 2) {
        		String potentialLat = splitter[0].trim();
        		String potentialLongit = splitter[1].trim();
	    		if ((potentialLat.contains("n") || potentialLat.contains("s") ) && 
	        		(potentialLongit.contains("e") || potentialLongit.contains("w")) ) {
	        			//example "12.65656 N, 77.345812 E" or "12.65656 N, 77.345812E" or "12.65656N, 77.345812E"
	        			lat = potentialLat;
	        			longit = potentialLongit;
	    		} else if ((potentialLat.contains("e") || potentialLat.contains("w") ) && 
	            		   (potentialLongit.contains("n") || potentialLongit.contains("s"))) {
	        			//example "12.65656 E, 77.345812 N" or //example "12.65656E, 77.345812N"
	        			//swap
	        			lat = potentialLongit;
	        			longit = potentialLat;
	        	} else {
	        			// example "12.65656, 77.345812"
	        			lat = potentialLat;
	        			longit = potentialLongit;
	        	}
	    		break;
    		}
    	}
    	if ( (splitter == null) || (splitter.length != 2) ) {
    		throw new Exception ("Error parsing latitude and longitude.");
    	}
    	setLatLong(lat, longit);
    }
    
    
    public String getGeoHashOfLen(int len) {
    	String ret = null;
    	ret = GeoHash.encodeGeohash(latitude, longitude, len);
    	return ret;
    }
    
    
    public static void main( String[] args ) throws Exception
    {
    	//GeohashExtractor pt = new GeohashExtractor(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
    	//12.9716° N, 77.5946° E
    	GeohashExtractor pt = new GeohashExtractor(12.9716, 77.5946);
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("12.9716 ° N, 77.5946°   E");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("12.9716 ° N, 77.5946°   E");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("12.9716N,77.5946E");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("12.9716°N;77.5946°E");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("12.9716°N:77.5946°E");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("12.9716°N::77.5946°E");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("12.9716°S\t77.5946°E");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
        System.out.println("----------------------");
        pt = new GeohashExtractor("- 12.9716	-77.5946");
        System.out.println(pt.getGeoHashOfLen(6));
        System.out.println(pt.getGeoHashOfLen(8));
        System.out.println(pt.getGeoHashOfLen(12));
    }
    
    
}
