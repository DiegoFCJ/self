CREATE TABLE IF NOT EXISTS bots (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  bot_name TEXT NOT NULL,
  description TEXT,
  start_command TEXT,
  source_path TEXT,
  language TEXT NOT NULL
);


ALTER TABLE bots ADD COLUMN description TEXT;
ALTER TABLE bots ADD COLUMN start_command TEXT;
ALTER TABLE bots ADD COLUMN source_path TEXT;