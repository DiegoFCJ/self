// Base path for bots
const BASE_PATH = 'bots';

// Function to initialize the bot list on page load
document.addEventListener('DOMContentLoaded', populateBotList);

/**
 * Populate the bot list dynamically by checking the bots.json file.
 */
async function populateBotList() {
    try {
        // Load the bots.json configuration
        const botsConfig = await fetchBotsConfig(BASE_PATH);

        // Access the main container for bot sections
        const botSections = document.getElementById('bot-sections');
        botSections.innerHTML = ''; // Clear existing content

        // Iterate through each language in botsConfig
        for (const language in botsConfig) {
            const bots = botsConfig[language];

            // If there are no bots for this language, skip it
            if (bots.length === 0) continue;

            // Create a section for the language
            const section = document.createElement('section');
            section.id = `${language}-bots`;
            section.innerHTML = `<h2>${capitalize(language)} Bots</h2>`;
            const botContainer = document.createElement('div');
            botContainer.classList.add('bot-container');

            // Add each bot under the corresponding language section
            for (const bot of bots) {
                const botDiv = document.createElement('div');
                botDiv.classList.add('bot');

                botDiv.innerHTML = `
                    <h3>${bot.botName}</h3>
                    <p>Description: ${bot.description || 'No description available'}</p>
                    <a href="${bot.sourcePath || '#'}" target="_blank" class="button">View Source</a>
                    <button onclick="downloadBot('${language}', '${bot.botName}')">Download</button>
                `;
                botContainer.appendChild(botDiv);
            }

            section.appendChild(botContainer);
            botSections.appendChild(section);
        }
    } catch (error) {
        console.error('Error populating bot list:', error);
    }
}

/**
 * Fetch the bots configuration from bots.json.
 * @param {string} basePath - The base path for bots.
 * @returns {Promise<Object>} - The bots configuration object.
 */
async function fetchBotsConfig(basePath) {
    try {
        // Usa il percorso corretto per il file bots.json
        const response = await fetch(`${basePath}/bots.json`);
        if (!response.ok) {
            throw new Error('Failed to fetch bots configuration.');
        }
        const botsConfig = await response.json();
        return botsConfig;
    } catch (error) {
        console.error('Error fetching bots configuration:', error);
        return {}; // Return empty object in case of error
    }
}

async function fetchBots(basePath, language) {
    const bots = [];
    const botFolderPath = `${basePath}/${language}`;

    try {
        // Carica la configurazione dal file bots.json
        const botDirs = await fetchBotDirectories(botFolderPath); // Ottieni le cartelle dei bot dal file bots.json

        // Per ogni bot, carica il file bot.json
        for (const bot of botDirs) {
            const botJsonPath = `${basePath}/${language}/${bot.path}`;  // Usa il percorso corretto per il bot

            try {
                const response = await fetch(botJsonPath);
                if (response.ok) {
                    const botData = await response.json();
                    bots.push({
                        botName: botData.botName,
                        description: botData.description || 'No description available',
                        sourcePath: botData.startCommand.replace('python3 ', '').replace('java -jar ', '').replace('node ', ''),
                    });
                }
            } catch (error) {
                console.error(`No bot.json found for ${bot.botName} in ${language} folder.`);
            }
        }
    } catch (error) {
        console.error(`No bots found in ${language} folder.`);
    }

    return bots;
}

/**
 * Capitalizes the first letter of a string.
 * @param {string} str - The input string.
 * @returns {string} - The capitalized string.
 */
function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

/**
 * Function to handle bot downloads.
 * This interacts with the backend API to download bots dynamically.
 * @param {string} language - The language folder of the bot (e.g., 'java', 'python').
 * @param {string} botName - The name of the bot to download.
 */
function downloadBot(language, botName) {
    const url = `/api/bots/${language}/${botName}/download`;
    fetch(url)
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Error: ${response.statusText}`);
            }
            return response.blob();
        })
        .then((blob) => {
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = `${botName}.zip`;
            link.click();
        })
        .catch((error) => {
            console.error('Failed to download bot:', error);
            alert('Could not download the bot. Please try again.');
        });
}