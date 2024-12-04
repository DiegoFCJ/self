class Task {
  final int? id;
  final String title;
  final bool completed;

  Task({this.id, required this.title, this.completed = false});

  // Metodo per convertire un oggetto Task in una mappa (utile per SQLite)
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'title': title,
      'completed': completed ? 1 : 0,
    };
  }

  // Metodo statico per creare un oggetto Task da una mappa (SQLite -> Dart)
  static Task fromMap(Map<String, dynamic> map) {
    return Task(
      id: map['id'],
      title: map['title'],
      completed: map['completed'] == 1,
    );
  }
}