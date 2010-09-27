package de.dfki.km.text20.browserplugin.services.sessionrecorder.options.specialcommand;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.SpecialCommandOption;

/**
 * @author rb
 *
 */
public class OptionFakeNextDate implements SpecialCommandOption {

    /** */
    private static final long serialVersionUID = 7045264790144268252L;

    /** */
    private final long startdate;

    /**
     * @param next 
     * 
     */
    public OptionFakeNextDate(long next) {
        this.startdate = next;
    }

    /**
     * @return the startdate
     */
    public long getDate() {
        return this.startdate;
    }

}
