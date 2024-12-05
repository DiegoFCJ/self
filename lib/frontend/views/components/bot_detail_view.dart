import 'package:flutter/material.dart';
import '../../models/bot.dart';

class BotDetailView extends StatelessWidget {
  final Bot bot;

  BotDetailView({required this.bot});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(bot.name),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Nome: ${bot.name}',
              style: Theme.of(context).textTheme.headlineMedium,  // Cambiato headline5 in headlineMedium
            ),
            SizedBox(height: 10),
            Text(
              'Descrizione: ${bot.description}',
              style: Theme.of(context).textTheme.bodyLarge,  // Cambiato bodyText1 in bodyLarge
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                // Azione quando si vuole eseguire l'operazione sul bot
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Esegui bot: ${bot.name}')),
                );
              },
              child: Text('Esegui Bot'),
            ),
          ],
        ),
      ),
    );
  }
}