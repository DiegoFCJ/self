package Bot1;

public class Bot {
    public static void main(String[] args) {
        try {
            System.out.println("Bot1 ha iniziato l'esecuzione...");

            // Simula un'operazione lunga 30 secondi
            System.out.println("Fase 1: Attesa per 30 secondi...");
            Thread.sleep(30000);
            System.out.println("Fase 1 completata!");

            // Simula un'altra operazione lunga 30 secondi
            System.out.println("Fase 2: Attesa per 30 secondi...");
            Thread.sleep(30000);
            System.out.println("Fase 2 completata!");

            // Simula una fase finale
            System.out.println("Fase 3: Attesa per 1 minuto...");
            Thread.sleep(60000);
            System.out.println("Fase 3 completata!");

            System.out.println("Bot1 ha completato l'esecuzione.");
        } catch (InterruptedException e) {
            System.err.println("Errore durante l'esecuzione del bot.");
        }
    }
}