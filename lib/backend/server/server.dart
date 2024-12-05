import 'dart:async';
import 'package:shelf/shelf.dart';
import 'package:shelf/shelf_io.dart' as io;
import 'package:scriptagher/shared/custom_logger.dart';
import 'package:scriptagher/shared/constants/LOGS.dart';

Future<void> startServer() async {
  // Crea un'istanza del CustomLogger
  final CustomLogger logger = CustomLogger();

  // Usa il middleware di logging per tracciare le richieste
  final handler = const Pipeline()
      .addMiddleware(logRequests()) // Middleware per il log delle richieste
      .addMiddleware(_logCustomRequests(logger)) // Usa il Custom Logger per le richieste
      .addHandler(_echoRequest); // Handler delle richieste

  try {
    // Avvio del server sulla porta 8080
    final server = await io.serve(handler, 'localhost', 8080);
    logger.info(LOGS.serverStart, 'Server running at http://${server.address.host}:${server.port}');
  } catch (e) {
    // Log dell'errore se il server non riesce ad avviarsi
    logger.error(LOGS.serverError, 'Error starting server: $e');
  }
}

// Middleware personalizzato che usa il Custom Logger
Middleware _logCustomRequests(CustomLogger logger) {
  return (Handler innerHandler) {
    return (Request request) async {
      // Log di richiesta personalizzato
      logger.info(LOGS.requestReceived, '${request.method} ${request.requestedUri}');
      return innerHandler(request);
    };
  };
}

// Handler per rispondere a tutte le richieste
Response _echoRequest(Request request) {
  return Response.ok('Hello, world!');
}
