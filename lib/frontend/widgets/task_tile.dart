import 'package:flutter/material.dart';

class TaskTile extends StatelessWidget {
  final String title;
  final bool completed;
  final Function(bool?) onChanged;

  TaskTile({required this.title, required this.completed, required this.onChanged});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(title),
      trailing: Checkbox(value: completed, onChanged: onChanged),
    );
  }
}