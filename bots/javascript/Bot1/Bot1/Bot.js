console.log("Bot2 ha iniziato l'esecuzione...");

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}

async function executeBot() {
    // Fase 1
    console.log("Fase 1: Attesa per 30 secondi...");
    await delay(30000);
    console.log("Fase 1 completata!");

    // Fase 2
    console.log("Fase 2: Attesa per 30 secondi...");
    await delay(30000);
    console.log("Fase 2 completata!");

    // Fase 3
    console.log("Fase 3: Attesa per 1 minuto...");
    await delay(60000);
    console.log("Fase 3 completata!");

    console.log("Bot2 ha completato l'esecuzione.");
}

executeBot();