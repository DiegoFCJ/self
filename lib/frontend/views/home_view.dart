import 'package:flutter/material.dart';
import '../models/bot.dart';
import 'components/bot_card.dart';
import 'components/bot_detail_view.dart';

class HomeView extends StatelessWidget {
  // Lista di bot simulata
  final List<Bot> bots = [
    Bot(botName: 'Bot 1', description: 'Questo è il primo bot', startCommand: '', sourcePath: '', language: ''),
    Bot(botName: 'Bot 2', description: 'Questo è il secondo bot', startCommand: '', sourcePath: '', language: ''),
    Bot(botName: 'Bot 3', description: 'Questo è il terzo bot', startCommand: '', sourcePath: '', language: ''),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Lista dei Bot'),
      ),
      body: ListView.builder(
        itemCount: bots.length,
        itemBuilder: (context, index) {
          final bot = bots[index];
          return BotCard(
            bot: bot,
            onTap: () {
              // Quando un bot viene selezionato, mostra il dettaglio
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => BotDetailView(bot: bot),
                ),
              );
            },
          );
        },
      ),
    );
  }
}