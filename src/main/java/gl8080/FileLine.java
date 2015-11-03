package gl8080;

import java.util.HashMap;
import java.util.Map;

public class FileLine {
    
    private Map<String, String> map = new HashMap<String, String>();
    
    public String getElement(String name) {
        return this.map.get(name);
    }

    public void addElement(FieldDefinition definition, String value) {
        this.map.put(definition.getName(), value);
    }

    public int getElementAsInt(String name) {
        return Integer.parseInt(this.getElement(name));
    }

}
