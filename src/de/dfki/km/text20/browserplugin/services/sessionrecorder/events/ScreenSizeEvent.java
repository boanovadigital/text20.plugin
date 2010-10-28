package de.dfki.km.text20.browserplugin.services.sessionrecorder.events;

import java.awt.Dimension;

/**
 * @author rb
 *
 */
public class ScreenSizeEvent extends AbstractSessionEvent {

    /**  */
    private static final long serialVersionUID = 4041413093224109621L;

    /**  */
    public Dimension screenSize;

    /**
     * @param screenSize
     */
    public ScreenSizeEvent(Dimension screenSize) {
        this.screenSize = screenSize;
    }
}
