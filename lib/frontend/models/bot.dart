class Bot {
  int? id;
  final String botName;
  final String description;
  final String startCommand;
  final String sourcePath;
  final String language;

  Bot({
    this.id,
    required this.botName,
    required this.description,
    required this.startCommand,
    required this.sourcePath,
    required this.language,
  });

  Map<String, dynamic> toJson() {
    return {
      'botName': botName,
      'description': description,
      'startCommand': startCommand,
      'sourcePath': sourcePath,
      'language': language,
    };
  }

  static Bot fromJson(Map<String, dynamic> json) {
    return Bot(
      botName: json['botName'],
      description: json['description'],
      startCommand: json['startCommand'],
      sourcePath: json['sourcePath'],
      language: json['language'],
    );
  }
}