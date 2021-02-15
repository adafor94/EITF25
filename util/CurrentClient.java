package util;

import java.util.HashMap;
import java.util.Map;

public class CurrentClient {
    private HashMap<String, String> attributes = new HashMap<>();

    public CurrentClient(String s) {
        String[] attributes = s.split(",");
        for (String attribute : attributes) {
            String[] keyAndValue = attribute.split("=");
            this.attributes.put(keyAndValue[0].trim(), keyAndValue[1].trim());
        }
    }

    public String getAttribute(String s) {
        return this.attributes.getOrDefault(s, "NO VALUE");
    }

    public void print() {
        for (Map.Entry<String,String> entry : attributes.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}