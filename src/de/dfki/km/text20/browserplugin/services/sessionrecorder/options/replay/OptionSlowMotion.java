package de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption;

/**
 * @author rb
 *
 */
public class OptionSlowMotion implements ReplayOption {
    /**  */
    private static final long serialVersionUID = 4224184820549735162L;
    /**  */
    private int factor = 1;

    /**
     * @param factor stretch factor for replay
     */
    public OptionSlowMotion(int factor) {
        this.factor = factor;
    }

    /**
     * @return .
     */
    public int getFactor() {
        return this.factor;
    }
}
