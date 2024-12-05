import 'package:scriptagher/shared/custom_logger.dart';
import 'package:scriptagher/backend/server/api_integration/github_api.dart';
import '../models/bot.dart';
import '../db/bot_database.dart';

class BotGetService {
  final CustomLogger logger = CustomLogger();
  final BotDatabase botDatabase;
  final GitHubApi gitHubApi;

  BotGetService(this.botDatabase, this.gitHubApi);

  /// Fetches the list of all available bots from the server.
  Future<List<Bot>> fetchAvailableBots() async {
    try {
      logger.info('BotService', 'Fetching available bots list.');

      // Fetch the list of bots from GitHub API
      final rawData = await gitHubApi.fetchBotsList();

      // List to collect all bots that will be inserted into the DB
      List<Bot> allBots = [];

      // Iterate over each language and create Bot objects
      for (var language in rawData.keys) {
        for (var botData in rawData[language]) {
          final botName = botData['botName'];
          final path = botData['path'];

          // Create a Bot object and populate it with the data
          Bot bot = Bot(
            botName: botName,
            sourcePath: path,
            language: language,
          );

          // Fetch additional details from the Bot.json file for the individual bot
          final botDetails = await _getBotDetails(language, bot);

          // Add the Bot to the list
          allBots.add(botDetails);
        }
      }

      // Save the list of bots in the database
      await botDatabase.insertBots(allBots);

      logger.info('BotService', 'Successfully saved ${allBots.length} bots to the database.');

      // Return the list of bots with their details
      return allBots;
    } catch (e) {
      logger.error('BotService', 'Error fetching bots: $e');
      rethrow;
    }
  }

  /// Fetches the detailed information of a bot by extracting and reading the Bot.json inside the bot directory.
  Future<Bot> _getBotDetails(String language, Bot bot) async {
    try {
      // Fetch the details from GitHub API
      final botDetailsMap = await gitHubApi.fetchBotDetails(language, bot.botName);

      // Update the Bot object with the fetched details
      bot.description = botDetailsMap['description'] ?? 'No description available';
      bot.startCommand = botDetailsMap['startCommand'];

      return bot;
    } catch (e) {
      logger.error('BotService', 'Error fetching details for ${bot.botName}: $e');
      throw Exception('Failed to load bot details');
    }
  }
}