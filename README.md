# Bot List - Documentation

Welcome to the **Bot List**, a curated collection of runnable bots organized by language. 
This repository provides a framework for managing, downloading, and executing bots seamlessly. 
Below you'll find instructions on how to add your own bot to the list, 
the rules to follow, and how bots are structured.

---

## 🚀 How to Add Your Bot

Adding your bot to the repository involves the following steps:

### 1. Fork and Clone the Repository
1. Fork this repository to your GitHub account.
2. Clone your forked repository to your local machine:
   ```bash
   git clone https://github.com/<your-username>/scriptagher.git
   cd scriptagher
   ```

### 2. Create a Folder for Your Bot
- Navigate to the appropriate language folder (e.g., bots/java, bots/python, bots/javascript).
- Create a new folder with your bot's name. For example:
    ```bash
    mkdir bots/python/MyAwesomeBot
    ```

### 3. Add Your Bot Files
- Place your runnable bot file(s) inside this folder.
- Rules for file structure:
    - Single file execution: Your bot must be executable with a single command, such as:
        - Python: python3 `<file>`.py
        - JavaScript: node `<file>`.js
        - Java: java -jar `<file>`.jar
    - For complex bots: Bundle your bot as a runnable JAR file, standalone executable, or other self-contained format.
- Include a JSON metadata file in the folder (see below for the required structure).

### 4. Create a JSON Metadata File
Each bot folder must include a JSON file named `<bot-name>`.json. This file should contain:
```json
{
    "botName": "MyAwesomeBot",
    "description": "This bot automates XYZ tasks.",
    "startCommand": "python3 bots/python/MyAwesomeBot/MyAwesomeBot.py",
    "language": "Python"
}
```
### 5. Add Links in index.html
Update the HTML file (index.html) to include your bot:

1. Add a new `<li>` element under the appropriate language section.
2. Ensure it links to your bot's primary file and includes a brief description.

Example: 
```html
<li>
    <a href="bots/python/MyAwesomeBot/MyAwesomeBot.py" target="_blank">MyAwesomeBot</a>
    - Automates XYZ tasks.
</li>
```

### 🔑 Rules for Adding Bots
1. Independent Execution: Each bot must be self-contained and executable with a single command (no manual setup required).
2. Runnable Formats:
    - Python: Single .py file or a directory with `__main__`.py for execution.
    - JavaScript: Single .js file.
    - Java: A single .jar file (preferred for complex bots).
3. Include Metadata: Add a `<bot-name>`.json file in the same folder as your bot file.
4. Follow File Naming Conventions:
    - Folder and file names should be descriptive and match your bot's purpose.
    - Avoid spaces or special characters in folder/file names.
5. Documentation:
    - Add a description of your bot in the JSON file.
    - Provide clear instructions in your bot's code comments if necessary.

### 📋 Example Bot Structure
```sh
bots/
└── python/
    └── MyAwesomeBot/
        ├── MyAwesomeBot.py
        ├── MyAwesomeBot.json
```

#### MyAwesomeBot.json:
```json
{
    "botName": "MyAwesomeBot",
    "description": "This bot performs automated data scraping for websites.",
    "startCommand": "python3 bots/python/MyAwesomeBot/MyAwesomeBot.py",
    "language": "Python"
}
```

### 🛠 How the System Works
1. Bot Metadata: The backend fetches metadata from the JSON file in each bot folder to list and manage bots.
2. Execution: When a bot is executed, the backend runs the startCommand specified in the JSON file.
3. Bot Hosting: Bots are hosted in this repository and served via GitHub Pages for easy downloading.

### 🌟 Submitting a Pull Request
1. After adding your bot and updating the necessary files, commit your changes:
    ```bash
    git add .
    git commit -m "Added MyAwesomeBot"
    ```

2. Push your changes to your forked repository:
    ```bash
    git push origin main
    ```

3. Open a Pull Request to the original repository with a clear description of your bot.

### 🛡 License
By contributing, you agree that your bot will be open-sourced under the same license as this repository. Ensure you have the rights to share the bot.