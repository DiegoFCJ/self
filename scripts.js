// Base path for bots
const BASE_PATH = 'bots';

// Function to initialize the bot list on page load
document.addEventListener('DOMContentLoaded', populateBotList);

/**
 * Populate the bot list dynamically by checking existing language directories.
 */
async function populateBotList() {
    try {
        const languages = await fetchLanguages(BASE_PATH);

        // Access the main container for bot sections
        const botSections = document.getElementById('bot-sections');
        botSections.innerHTML = ''; // Clear existing content

        for (const language of languages) {
            // Fetch bots in each language folder
            const bots = await fetchBots(BASE_PATH, language);

            // If there are no bots, skip this language
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
                    <p>Description: ${bot.description}</p>
                    <a href="${bot.sourcePath}" target="_blank" class="button">View Source</a>
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
 * Fetches the list of language directories (folders) in the bots directory.
 * It checks if a folder contains any bot JSON files by attempting to fetch a sample bot JSON.
 * @param {string} basePath - Base path for bots.
 * @returns {Promise<string[]>} - Array of language folder names.
 */
async function fetchLanguages(basePath) {
    const languages = [];
    const languageDirs = ['java', 'javascript', 'python']; // Static list of possible languages

    // Check each language directory to see if it exists by attempting to fetch a bot JSON file
    for (const language of languageDirs) {
        const pathToCheck = `${basePath}/${language}/Bot1/bot.json`; // Sample bot file to check existence

        try {
            const response = await fetch(pathToCheck);
            if (response.ok) {
                languages.push(language); // Add language to list if folder exists
            }
        } catch (error) {
            console.log(`${language} folder does not exist.`);
        }
    }

    return languages;
}

/**
 * Fetches bots for a specific language by reading their JSON files.
 * @param {string} basePath - Base path for bots.
 * @param {string} language - Language folder name.
 * @returns {Promise<Object[]>} - Array of bot objects.
 */
async function fetchBots(basePath, language) {
    const bots = [];
    const botFolderPath = `${basePath}/${language}`;

    try {
        const response = await fetch(`${botFolderPath}/Bot1/bot.json`); // Assuming each bot has a bot.json
        if (response.ok) {
            const botData = await response.json();
            bots.push({
                botName: botData.botName,
                description: botData.description,
                sourcePath: botData.startCommand.replace('python3 ', '').replace('java -jar ', '').replace('node ', ''),
            });
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