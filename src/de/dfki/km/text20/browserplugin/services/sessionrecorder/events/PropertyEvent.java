package de.dfki.km.text20.browserplugin.services.sessionrecorder.events;

/**
 * @author rb
 *
 */
public class PropertyEvent extends AbstractSessionEvent {

    /**  */
    private static final long serialVersionUID = -7347266021220906309L;

    /**  */
    public String key;

    /**  */
    public String value;

    /**
     * @param key
     * @param value
     */
    public PropertyEvent(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
