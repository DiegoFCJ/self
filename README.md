<div align="left" style="display: flex; align-items: center;">
    <img src="app-icon.png" alt="Self Project Icon" width="30" height="30" style="margin-right: 10px;">
    <h1 style="margin: 0; display: inline; vertical-align: middle;"> SELF - DFCJ Project</h1>
</div>


Welcome to the **Self Project**, a personal management tool built using Java, Spring Boot, and various popular frameworks and libraries. This project is designed for self-management tasks, including time management automation through bots. 

## 🧭 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Technologies](#technologies)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

---

## 🔍 Project Overview

This project is focused on **self-management** using automation and bot-driven interactions. The system is primarily built around time management and logging activities using a bot interface.

The core of the application consists of a **TimeManagerBot** which helps in tracking activities, generating logs, and handling user interactions.

### Key Information:
- **Version**: 0.1
- **Group ID**: `com.dfcj`
- **Artifact ID**: `self_project`
- **Parent**: Spring Boot Starter Parent (`2.7.18`)

---

## 🚀 Features

- **Time Management Automation** using a custom-built bot
- **Logging**: Tracks all interactions with detailed timestamped logs
- **UI Integration** with JavaFX for a smooth user experience
- **Highly Customizable**: Includes constants, factories, and utilities for easy customization
- **Stylized UI**: Custom stylesheets for buttons, headers, and menus

---

## 🛠️ Project Structure

```bash
.
├── DB
│   └── menu.json
├── LOGS
│   └── YYYY-MM-DD
│       └── BASE-APP
│           ├── BACK
│           │   └── self-manager-backend_YYYY-MM-DD_HH-MM-SS.log
│           ├── DB
│           │   └── self-manager-database_YYYY-MM-DD_HH-MM-SS.log
│           ├── DRIVER
│           │   └── self-manager-driver_YYYY-MM-DD_HH-MM-SS.log
│           ├── FRONT
│           │   └── self-manager-frontend_YYYY-MM-DD_HH-MM-SS.log
│           └── GENERAL
│               └── self-manager-general_YYYY-MM-DD_HH-MM-SS.log
├── mvnw
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── self
│   │   │           └── self_project
│   │   │               ├── bots
│   │   │               ├── constants
│   │   │               ├── factories
│   │   │               ├── ui
│   │   │               └── utils
│   ├── plugins
│   └── resources
│       ├── icons
│       ├── styles
│   └── test
└── target
```

### Key Components:

1. **Main Application**: `MainApp.java`
2. **Bot Logic**: `TimeManagerBot.java`
3. **Constants & Utilities**: For handling paths, regex, logging, etc.
4. **Custom UI**: Built with JavaFX for a rich user interface

---

## 🔧 Versions Used

- **Java**: `11`
- **JavaFX**: `11.0.2`
- **Maven**: `3.8.7`
- **Maven Clean**: `3.1.0`
- **Maven Exec**: `3.0.0`
- **Selenium**: `4.11.0`
- **Log4j**: `2.20.0`
- **SLF4J**: `1.7.36`
- **JUnit**: `5.8.2`
- **Mockito**: `4.8.1`
- **AssertJ**: `3.21.0`
- **Jsoup**: `1.16.1`
- **Apache Commons**: `4.5.13`

## 🚀 Technologies Used

- **Java 11** for the core language
- **JavaFX 11.0.2** for GUI
- **Maven 3.8.7** for dependency management
- **Selenium 4.11.0** for browser automation (Brave Browser)
- **JUnit 5.8.2** for unit testing
- **Log4j 2.20.0** and **SLF4J 1.7.36** for logging
- **Jsoup 1.16.1** for parsing HTML


---

## 📦 Installation and Setup

### Prerequisites

- **Java 11+**
- **Maven 3.8.7+**
- **Linux** (Tested on Linux)
- **Brave Browser** for bot automation

### Steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/DiegoFCJ/self_project.git
   cd self_project
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Set JAVA_HOME environment
   ```bash
   export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
   export PATH=$JAVA_HOME/bin:$PATH
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ./mvnw javafx:run
   ```

   The application will start and open a GUI window.

## 📖 Usage

### Running the TimeManagerBot:

After the application starts, the **TimeManagerBot** automatically tracks activities and logs them under the `LOGS/` directory. Each log entry is timestamped for reference.

You can view logs here:

### Customization:

To modify bot behavior, UI elements, or logging:
- **Edit constants** in `src/main/java/com/self/self_project/constants/`
- **Customize styles** in `src/main/resources/styles/`

---

## 🤝 Contributing

If you'd like to contribute to this project:

1. Fork the repository
2. Create a new branch for your feature (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/my-feature`)
5. Create a Pull Request

---
## 🗂️ Project overview
![alt text](image.png)

---
## 📄 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for more details.
