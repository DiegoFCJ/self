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
      'bot_name': botName,
      'description': description,
      'start_command': startCommand,
      'source_path': sourcePath,
      'language': language,
    };
  }

  factory Bot.fromMap(Map<String, dynamic> map) {
    return Bot(
      id: map['id'] as int?,
      botName: map['bot_name'] as String,
      description: map['description'] as String?,
      startCommand: map['start_command'] as String?,
      sourcePath: map['source_path'] as String?,
      language: map['language'] as String?,
    );
  }
}