import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BotService {
  private readonly APP_NAME = 'scriptagher';
  private readonly BASE_PATH = `https://raw.githubusercontent.com/diegofcj/${this.APP_NAME}/gh-pages/bots`;
  private readonly BASE_SOURCE = `https://github.com/diegofcj/${this.APP_NAME}/tree/gh-pages/bots`;

  constructor(private http: HttpClient) { }

  /**
   * Fetch the bots configuration from bots.json.
   */
  getBotsConfig(): Observable<any> {
    const botsJsonPath = `${this.BASE_PATH}/bots.json`;
    console.log('Fetching bots.json from:', botsJsonPath);
    return this.http.get(botsJsonPath);
  }

  /**
   * Fetch detailed bot information from Bot.json.
   * @param botJsonPath - The URL to the Bot.json file.
   */
  getBotDetails(botJsonPath: string): Observable<any> {
    console.log('Fetching Bot.json from:', botJsonPath);
    return this.http.get(botJsonPath);
  }

  /**
   * Fetch and download the bot ZIP file.
   * @param language - The language folder of the bot.
   * @param botName - The bot's name.
   */
  downloadBot(language: string, botName: string): void {
    const zipPath = `${this.BASE_PATH}/${language}/${botName}/${botName}.zip`;
    console.log('Downloading bot ZIP from:', zipPath);

    this.http.get(zipPath, { responseType: 'blob' }).subscribe(
      (blob: Blob) => {
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = `${botName}.zip`;
        link.click();
      },
      (error) => {
        console.error('Error downloading bot:', error);
        alert('Could not download the bot. Please try again.');
      }
    );
  }

  /**
   * Open the bot source code in a new tab.
   * @param bot - Bot object containing language and name.
   */
  openBot(bot: any): void {
    const sourcePath = `${this.BASE_SOURCE}/${bot.language}/${bot.botName}`;
    console.log('Opening bot source at:', sourcePath);
    window.open(sourcePath, '_blank');
  }

  /**
   * Get Bot Details by Name
   * @param botName - The bot's name.
   */
  getBotDetailsByName(botName: string): Observable<any> {
    const botJsonPath = `${this.BASE_PATH}/${botName}/Bot.json`;
    console.log('Fetching Bot.json by name from:', botJsonPath);
    return this.http.get(botJsonPath);
  }
}