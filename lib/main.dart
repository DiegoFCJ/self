import 'package:flutter/material.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart'; // Importa il pacchetto FFI
import 'frontend/pages/home_page.dart';

void main() {
  // Imposta la factory del database per l'uso con FFI su desktop
  databaseFactory = databaseFactoryFfi;

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Task Manager',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: HomePage(),
    );
  }
}