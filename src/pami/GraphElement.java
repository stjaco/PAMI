/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jacopo
 */
public class GraphElement {

    final static String EDGE_SEPARATOR = "::";

    protected String id;
    HashMap<String, String> attributes;
    AtomicInteger weight = new AtomicInteger(1);
    
    /**
     * @param key of the attribute to be added
     * @param value of the attribute to be added
     */


    public void setAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        attributes.put(key, value);
    }

    /**
     * @param attributes the attribute map for this element
     */
    public void setAttributes(HashMap attributes) {
        this.attributes = attributes;
    }

    /**
     * @param key the String key attribute key
     * @return the String attribute value
     */
    protected String getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Returns an XML-formatted representation of the element
     *
     * @return the XML-formatted representation of the element
     */
    String toGraphML() {
        String ret = "";
        if (attributes != null) {
            Iterator<Entry<String, String>> it = attributes.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                ret += "<data key=\"" + pairs.getKey() + "\">" + pairs.getValue() + "</data>\n";
            }
        } else {
            ret += "<data key=\"weight\">" + weight.get() + "</data>\n";
        }
        return ret;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
}
