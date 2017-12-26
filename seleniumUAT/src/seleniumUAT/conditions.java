package seleniumUAT;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class conditions {
	public static Map<String, String> statMap = new HashMap<String, String>();
	
	public conditions() {
		statMap.put("startDate", null);
		statMap.put("endDate", null);
		statMap.put("startTime", null);
		statMap.put("endTime", null);
		statMap.put("unit", null);
		statMap.put("targetTenant", null);
		statMap.put("targetNode", null);
		statMap.put("format", null);
		statMap.put("formatUnit", null);
		statMap.put("formatTime", null);
		statMap.put("holidayCondition", null);
		statMap.put("dnZone", null);
		statMap.put("endPoint", null);
		statMap.put("CTIQ", null);
		statMap.put("GDNType", null);
		statMap.put("GDNName", null);
		statMap.put("skillSet", null);
		statMap.put("counselorGroup", null);
		statMap.put("counselor", null);
		statMap.put("media", null);
	}

	public void clear() {
		for (String key : statMap.keySet()) {
			statMap.put(key, null);
		}
	}
	
	public String getStat() {
	    	String ret = null;
	    	for (Entry<String, String> entry : statMap.entrySet()) {
	    	    String key = entry.getKey();
	    	    String value = entry.getValue();
	    	    if (value != null) {
	    		if (ret != null) ret = ret + ",";
	    		ret = ret + key + ":" + value;
	    	    }
	    	}
	    	return ret;
	}
}
