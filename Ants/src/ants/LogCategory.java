package ants;

/**
 * Enumeration of log categories for the Ants project
 * 
 * @author kases1, kustl1
 * 
 */
public enum LogCategory implements logging.LogCategory {
    ORDERS(false),
    PERFORMANCE(false),
    SETUP(false),
    TURN(false),
    WORLD(false);
    private boolean useCustomLogFile;

    private LogCategory(boolean useCustomLogFile) {
        this.useCustomLogFile = useCustomLogFile;
    }

    @Override
    public boolean useCustomLogFile() {
        return useCustomLogFile;
    }
}
