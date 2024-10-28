package com.self.self_project.back.factories;

import com.self.self_project.back.factories.strategy.BotStrategy;
import com.self.self_project.constants.LOGS;
import com.self.self_project.utils.logging.CustomLoggerUtils;

/**
 * Factory class for creating instances of various bot strategies.
 */
public class BotFactory {

    /**
     * Creates a bot based on the specified type.
     *
     * @param botType The type of bot to create (e.g., "TimeManager", "University", "JobSearch", "CvFix").
     * @return An instance of the created bot.
     * @throws IllegalArgumentException if the bot type is not supported.
     */
    public static BotStrategy createBot(String botType) {
        CustomLoggerUtils.info(LOGS.BOT_FACTORY, "Creating bot of type: " + botType);

        switch (botType.toLowerCase()) {
            case "timemanager":
                // Create and return a TimeManagerBot instance
                CustomLoggerUtils.info(LOGS.BOT_FACTORY, "TimeManagerBot created.");
                return null; // new TimeManagerBot();

            case "university":
                // Create and return a UniversityBot instance
                CustomLoggerUtils.info(LOGS.BOT_FACTORY, "UniversityBot created.");
                return null; // new UniversityBot();

            case "jobsearch":
                // Create and return a JobSearchBot instance
                CustomLoggerUtils.info(LOGS.BOT_FACTORY, "JobSearchBot created.");
                return null; // new JobSearchBot();

            case "cvfix":
                // Create and return a CvFixBot instance
                CustomLoggerUtils.info(LOGS.BOT_FACTORY, "CvFixBot created.");
                return null; // new CvFixBot();

            default:
                String errorMessage = "Bot type not supported: " + botType;
                CustomLoggerUtils.error(LOGS.BOT_FACTORY, errorMessage);
                throw new IllegalArgumentException(errorMessage);
        }
    }
}