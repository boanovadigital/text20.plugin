package de.dfki.km.text20.browserplugin.services.sessionrecorder.options.createrecorder;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.CreateRecorderOption;

/**
 * @author rb
 *
 */
public class OptionFakeStartDate implements CreateRecorderOption {

    /** */
    private static final long serialVersionUID = 7045264790144268252L;

    /** */
    private final long startdate;

    /**
     * @param startdate 
     * 
     */
    public OptionFakeStartDate(long startdate) {
        this.startdate = startdate;
    }

    /**
     * @return the startdate
     */
    public long getStartDate() {
        return this.startdate;
    }

}
