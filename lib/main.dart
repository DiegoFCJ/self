import 'dart:async';
import 'package:flutter/material.dart';
import 'package:scriptagher/frontend/views/home_view.dart';
import 'shared/custom_logger.dart';
import 'backend/server/server.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';
import 'package:scriptagher/shared/constants/LOGS.dart';
import 'backend/server/db/bot_database.dart';

// La tua vista principale di Flutter
Future<void> main() async {
  // Inizializza il binding di Flutter
  WidgetsFlutterBinding.ensureInitialized();

  // Crea un'istanza del CustomLogger
  final CustomLogger logger = CustomLogger();

  await startDB(logger);
  // Avvio del backend
  await startBackend(logger);

  // Avvio del frontend
  runApp(MyApp());
}

Future<void> startDB(CustomLogger logger) async {
  // Inizializza il databaseFactory per sqflite_common_ffi
  databaseFactory = databaseFactoryFfi;

  final botDatabase = BotDatabase();

  // Inizializza il database (chiamando il getter della database)
  try {
    logger.info(LOGS.serverStart, 'Attempting to initialize database...');
    await botDatabase.database;
    logger.info(LOGS.serverStart, 'Database initialized successfully');
  } catch (e) {
    logger.error(LOGS.serverError, 'Error initializing database: $e');
    return; // Fermare l'avvio del server in caso di errore nel database
  }
}

// Funzione per avviare il backend
Future<void> startBackend(CustomLogger logger) async {
  try {
    // Log di avvio del backend
    logger.info('Avvio del server...', 'Avvio del backend');
    await startServer(); // Qui dovrai avviare il server vero e proprio
    logger.info('Server avviato con successo', 'Avvio del backend');
  } catch (e) {
    logger.error(
        'Errore durante l\'avvio del server: $e', 'Errore nel backend');
  }
}

// Classe principale dell'app Flutter
class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Scriptagher',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: HomeView(),
    );
  }
}
