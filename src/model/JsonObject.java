package model;

import org.json.simple.JSONObject;

//an interface for Objects that need to be converted from/to JSON

public interface JsonObject {
	
	public void fromJSON(JSONObject jsonObject);
	
	public JSONObject toJSON();

}
