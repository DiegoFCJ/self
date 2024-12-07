import 'package:flutter/material.dart';
import 'package:bitsdojo_window/bitsdojo_window.dart';

class WindowTitleBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: const Color.fromARGB(34, 34, 34, 221),
      height: 40,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          // Titolo della finestra
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10.0),
            child: Text(
              "Scriptagher",
              style: TextStyle(
                color: Colors.white,
                fontSize: 18,
              ),
            ),
          ),
          // Pulsanti
          Row(
            children: [
              MinimizeButton(),
              MaximizeButton(),
              CloseButton(),
            ],
          ),
        ],
      ),
    );
  }
}

// Pulsante Minimizza
class MinimizeButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return IconButton(
      icon: Icon(Icons.remove, color: Colors.white),
      onPressed: () {
        appWindow.minimize();
      },
    );
  }
}

// Pulsante Massimizza
class MaximizeButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return IconButton(
      icon: Icon(
        appWindow.isMaximized ? Icons.crop_square : Icons.check_box_outline_blank,
        color: Colors.white,
      ),
      onPressed: () {
        if (appWindow.isMaximized) {
          appWindow.restore();
        } else {
          appWindow.maximize();
        }
      },
    );
  }
}

// Pulsante Chiudi
class CloseButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return IconButton(
      icon: Icon(Icons.close, color: Colors.white),
      onPressed: () {
        appWindow.close();
      },
    );
  }
}