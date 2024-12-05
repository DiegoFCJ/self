import 'dart:async';
import 'package:flutter/material.dart';
import 'package:scriptagher/frontend/views/home_view.dart';
import 'shared/custom_logger.dart';  // Importa la tua classe CustomLogger
import 'backend/server/server.dart';  // Assicurati che il path al server sia corretto

// La tua vista principale di Flutter
Future<void> main() async {
  // Avvio del backend
  await startBackend();

  // Avvio del frontend
  runApp(MyApp());
}

// Funzione per avviare il backend
Future<void> startBackend() async {
  // Crea un'istanza del CustomLogger
  final CustomLogger logger = CustomLogger();

  try {
    // Log di avvio del backend
    logger.info('Avvio del server...', 'Avvio del backend');
    await startServer();  // Qui dovrai avviare il server vero e proprio
    logger.info('Server avviato con successo', 'Avvio del backend');
  } catch (e) {
    logger.error('Errore durante l\'avvio del server: $e', 'Errore nel backend');
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
