package ants.bot;

import java.io.IOException;
import java.util.Random;

import logging.LogLevel;
import logging.Logger;
import logging.LoggerFactory;
import logging.LoggingConfig;
import ants.LogCategory;
import ants.entities.Ant;
import ants.state.Ants;
import ants.util.LiveInfo;
import api.entities.Aim;

/**
 * Sample Bot implementation. Extend this as needed.
 * 
 * @author kases1, kustl1
 */
public class SampleBot extends Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogCategory.TURN);
    private static final Logger LOGGER_PERFORMANCE = LoggerFactory.getLogger(LogCategory.PERFORMANCE);

    /**
     * Main method executed by the game engine for starting the bot.
     * 
     * @param args
     *            command line arguments
     * 
     * @throws IOException
     *             if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        initLogging();
        new SampleBot().readSystemInput();
    }

    /**
     * This is the main loop of the bot.
     */
    @Override
    public void doTurn() {
        // TODO implement your logic here; for now this just issues a random order for all ants
        /*
         * To get started, you may want to play through the Tutorial first
         * (http://www.aichallenge.org/ants_tutorial.php). Then have a look at the classes in the ants.state package.
         * They offer a representation of the world and everything in it. Make use of the search and pathfinding
         * algorithms from the Search project to find your way around.
         */
        for (Ant ant : Ants.getPopulation().getMyAnts()) {
            int aim = new Random().nextInt(Aim.values().length);
            Ants.getOrders().issueOrder(ant, Aim.values()[aim], "Random");
        }
        // finish the turn by writing all orders to the output stream
        Ants.getOrders().issueOrders();
    }

    private static void initLogging() {
        LoggingConfig.configure(LogCategory.PERFORMANCE, LogLevel.INFO);
        LoggingConfig.configure(LogCategory.TURN, LogLevel.INFO);
    }

    @Override
    protected void cleanup() {
        LOGGER.info("Cleaning up");
        LiveInfo.close();
        LoggerFactory.cleanup();
    }

    @Override
    protected void doFinishTurn() {
        LOGGER_PERFORMANCE.info("Finished in %1$s ms with %2$s ms remaining.", System.currentTimeMillis()
                - Ants.getAnts().getTurnStartTime(), Ants.getAnts().getTimeRemaining());

    }
}