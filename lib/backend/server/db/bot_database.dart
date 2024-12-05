import 'package:flutter/services.dart'; // Importa il pacchetto per leggere file
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import '../models/bot.dart';
import 'package:scriptagher/shared/custom_logger.dart';

class BotDatabase {
  static final BotDatabase _instance = BotDatabase._internal();
  final CustomLogger logger = CustomLogger();
  Database? _database;

  BotDatabase._internal();

  factory BotDatabase() => _instance;

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDatabase();
    return _database!;
  }

  // Carica il file .sql e restituisci il contenuto
  Future<String> _loadSqlFromAssets() async {
    try {
      return await rootBundle.loadString('assets/sql/queries.sql');
    } catch (e) {
      print("Error loading SQL file: $e");
      rethrow;
    }
  }

  // Esegui il database usando i comandi SQL caricati dal file .sql
  Future<Database> _initDatabase() async {
    final path = join(await getDatabasesPath(), 'bot_database.db');
    print("Database path: $path");

    return openDatabase(path, version: 2, onCreate: (db, version) async {
      try {
        // Carica il contenuto del file .sql
        String sqlQueries = await _loadSqlFromAssets();
        print("SQL Queries: $sqlQueries");

        // Esegui le query caricate
        List<String> queries = sqlQueries.split(';');
        for (var query in queries) {
          if (query.trim().isNotEmpty) {
            print('Executing query: $query');
            await db.execute(query.trim());
          }
        }
        print('Bots table created successfully');
      } catch (e) {
        print('Error during database creation: $e');
      }
    }, onUpgrade: (db, oldVersion, newVersion) async {
      print("Upgrading database from version $oldVersion to $newVersion");
      if (oldVersion < 2) {
        try {
          String sqlQueries = await _loadSqlFromAssets();
          List<String> queries = sqlQueries.split(';');
          for (var query in queries) {
            if (query.trim().isNotEmpty) {
              await db.execute(query.trim());
            }
          }
          print("Database upgraded successfully");
        } catch (e) {
          print('Error during database upgrade: $e');
        }
      }
    });
  }

  Future<void> insertBot(Bot bot) async {
    try {
      final db = await database;
      await db.insert(
        'bots',
        bot.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace,
      );
      logger.info('BotDatabase', 'Bot ${bot.botName} inserted into DB.');
    } catch (e) {
      logger.error('BotDatabase', 'Error inserting bot into DB: $e');
      throw Exception('Failed to insert bot into database');
    }
  }

  Future<void> insertBots(List<Bot> bots) async {
    for (var bot in bots) {
      await insertBot(bot); // Usa l'inserimento per ogni bot singolarmente
    }
  }

  Future<List<Bot>> getAllBots() async {
    final db = await database;
    final List<Map<String, dynamic>> maps = await db.query('bots');
    return List.generate(maps.length, (i) {
      return Bot.fromMap(maps[i]);
    });
  }

  Future<void> deleteBot(int id) async {
    final db = await database;
    await db.delete('bots', where: 'id = ?', whereArgs: [id]);
  }

  // Controlla se la tabella 'bots' esiste
  Future<void> checkIfTableExists(Database db) async {
    final result = await db.rawQuery(
        "SELECT name FROM sqlite_master WHERE type='table' AND name='bots';");
    if (result.isEmpty) {
      print("Table 'bots' does not exist!");
    } else {
      print("Table 'bots' exists.");
    }
  }
}