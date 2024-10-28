package com.self.self_project.back.factories.strategy;

import com.self.self_project.constants.LOGS;
import com.self.self_project.utils.logging.CustomLoggerUtils;

/**
 * Interface defining the strategy for different bots.
 */
public interface BotStrategy {
    
    /**
     * Execute the bot's strategy.
     * This method should contain the logic for the specific bot behavior.
     */
    default void execute() {
        CustomLoggerUtils.info(LOGS.BOT_STRATEGY, "Bot strategy executed.");
    }
}
