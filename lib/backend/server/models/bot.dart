class Bot {
  int? id;
  final String botName;
  String? description;
  String? startCommand;
  String? sourcePath;
  String? language;

  Bot({
    this.id,
    required this.botName,
    this.description,
    this.startCommand,
    this.sourcePath,
    this.language,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'botName': botName,
      'description': description,
      'startCommand': startCommand,
      'sourcePath': sourcePath,
      'language': language,
    };
  }

  factory Bot.fromMap(Map<String, dynamic> map) {
    return Bot(
      id: map['id'] as int?,
      botName: map['botName'] as String,
      description: map['description'] as String,
      startCommand: map['startCommand'] as String,
      sourcePath: map['sourcePath'] as String,
      language: map['language'] as String,
    );
  }
}