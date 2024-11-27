// Base path for GitHubusercontent
const BASE_PATH = 'https://raw.githubusercontent.com/diegofcj/scriptagher/bot-list/bots';

// Function to initialize the bot list on page load
document.addEventListener('DOMContentLoaded', populateBotList);

/**
 * Populate the bot list dynamically by checking the bots.json file and individual bot.json files.
 */
async function populateBotList() {
    try {
        // Load the bots.json configuration
        const botsConfig = await fetchBotsConfig();

        // Access the main container for bot sections
        const botSections = document.getElementById('bot-sections');
        botSections.innerHTML = ''; // Clear existing content

        // Iterate through each language in botsConfig
        for (const language in botsConfig) {
            const bots = botsConfig[language];

            // If there are no bots for this language, skip it
            if (!bots || bots.length === 0) continue;

            // Create a section for the language
            const section = document.createElement('section');
            section.id = `${language}-bots`;
            section.innerHTML = `<h2>${capitalize(language)} Bots</h2>`;
            const botContainer = document.createElement('div');
            botContainer.classList.add('bot-container');

            // Add each bot under the corresponding language section
            for (const bot of bots) {
                const botPath = `${BASE_PATH}/${language}/${bot.botName}`;
                const botJsonPath = `${botPath}/Bot.json`;

                try {
                    // Fetch additional details from Bot.json
                    const botDetails = await fetchBotDetails(botJsonPath);

                    const botDiv = document.createElement('div');
                    botDiv.classList.add('bot');

                    botDiv.innerHTML = `
                        <h3>${botDetails.botName}</h3>
                        <p>Description: ${botDetails.description || 'No description available'}</p>
                        <p>Start Command <br>${botDetails.startCommand || 'No start commands provided'}</p>
                        <button onclick="window.open('${botDetails.sourcePath || '#'}', '_blank');" class="button">View Source</button>
                        <button onclick="downloadBot('${language}', '${bot.botName}')">Download</button>
                    `;

                    botContainer.appendChild(botDiv);
                } catch (error) {
                    console.error(`Error fetching details for bot: ${bot.botName}`, error);
                }
            }

            section.appendChild(botContainer);
            botSections.appendChild(section);
        }
    } catch (error) {
        console.error('Error populating bot list:', error);
    }
}

/**
 * Fetch the bots configuration from bots.json hosted on GitHubusercontent.
 * @returns {Promise<Object>} - The bots configuration object.
 */
async function fetchBotsConfig() {
    const botsJsonPath = `${BASE_PATH}/bots.json`;

    try {
        const response = await fetch(botsJsonPath);
        if (!response.ok) {
            throw new Error('Failed to fetch bots.json configuration.');
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching bots.json:', error);
        return {}; // Return empty object in case of error
    }
}

/**
 * Fetch detailed bot information from its Bot.json file.
 * @param {string} botJsonPath - The URL to the Bot.json file.
 * @returns {Promise<Object>} - The parsed JSON data of the bot.
 */
async function fetchBotDetails(botJsonPath) {
    try {
        const response = await fetch(botJsonPath);
        if (!response.ok) {
            throw new Error('Failed to fetch Bot.json');
        }
        return await response.json();
    } catch (error) {
        console.error(`Error fetching bot details from ${botJsonPath}:`, error);
        return {}; // Return empty object in case of error
    }
}

/**
 * Function to handle bot downloads.
 * This directly downloads the ZIP file of the bot.
 * @param {string} language - The language folder of the bot (e.g., 'java', 'python').
 * @param {string} botName - The name of the bot to download.
 */
function downloadBot(language, botName) {
    const zipPath = `${BASE_PATH}/${language}/${botName}/${botName}.zip`;
    fetch(zipPath)
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

/**
 * Capitalizes the first letter of a string.
 * @param {string} str - The input string.
 * @returns {string} - The capitalized string.
 */
function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}