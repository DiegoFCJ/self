import 'database_helper.dart';
import 'task_model.dart';

class TaskRepository {
  final DatabaseHelper _dbHelper = DatabaseHelper();

  Future<List<Task>> getAllTasks() async {
    final maps = await _dbHelper.getTasks();
    return maps.map((map) => Task.fromMap(map)).toList();
  }

  Future<void> addTask(Task task) async {
    await _dbHelper.insertTask(task.toMap());
  }

  Future<void> updateTask(Task task) async {
    await _dbHelper.updateTask(task.id!, task.completed ? 1 : 0);
  }
}