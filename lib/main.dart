import 'package:flutter/material.dart';
import 'shared/custom_logger.dart';  // Assicurati di importare il CustomLogger

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  final CustomLogger logger = CustomLogger();  // Crea un'istanza del CustomLogger

  @override
  Widget build(BuildContext context) {
    // Log di esempio all'avvio dell'app
    logger.info('frontend', 'App started');
    
    return MaterialApp(
      title: 'Flutter Logging Example',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(logger: logger),  // Passa il logger alla HomePage
    );
  }
}

class MyHomePage extends StatelessWidget {
  final CustomLogger logger;  // Riceve il logger come parametro

  MyHomePage({required this.logger});

  @override
  Widget build(BuildContext context) {
    // Log di esempio nella HomePage
    logger.debug('frontend', 'HomePage loaded');
    
    return Scaffold(
      appBar: AppBar(
        title: Text('Flutter Logging Example'),
      ),
      body: Center(
        child: ElevatedButton(
          onPressed: () {
            // Logga l'azione dell'utente
            logger.info('frontend', 'User pressed the button');
            showDialog(
              context: context,
              builder: (_) => AlertDialog(
                title: Text('Button Pressed'),
                content: Text('You pressed the button!'),
                actions: [
                  TextButton(
                    child: Text('OK'),
                    onPressed: () {
                      // Logga la chiusura del dialogo
                      logger.info('frontend', 'User closed the dialog');
                      Navigator.of(context).pop();
                    },
                  ),
                ],
              ),
            );
          },
          child: Text('Press me'),
        ),
      ),
    );
  }
}
