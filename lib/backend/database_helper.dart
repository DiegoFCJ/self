import 'dart:async';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class DatabaseHelper {
  static final DatabaseHelper _instance = DatabaseHelper._internal();
  factory DatabaseHelper() => _instance;

  DatabaseHelper._internal();

  Database? _database;

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDatabase();
    return _database!;
  }

  Future<Database> _initDatabase() async {
    final dbPath = await getDatabasesPath();
    return openDatabase(
      join(dbPath, 'tasks.db'),
      onCreate: (db, version) {
        return db.execute(
          'CREATE TABLE tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, completed INTEGER)',
        );
      },
      version: 1,
    );
  }

  Future<List<Map<String, dynamic>>> getTasks() async {
    final db = await database;
    return db.query('tasks', orderBy: 'id');
  }

  Future<void> insertTask(Map<String, dynamic> task) async {
    final db = await database;
    await db.insert('tasks', task,
        conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<void> updateTask(int id, int completed) async {
    final db = await database;
    await db.update(
      'tasks',
      {'completed': completed},
      where: 'id = ?',
      whereArgs: [id],
    );
  }
}
