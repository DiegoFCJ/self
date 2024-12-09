import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BotService {
  private readonly BASE_PATH = 'https://raw.githubusercontent.com/diegofcj/scriptagher/gh-pages/bots';

  constructor(private http: HttpClient) { }

  /**
   * Fetch the bots configuration from bots.json.
   */
  getBotsConfig(): Observable<any> {
    const botsJsonPath = `${this.BASE_PATH}/bots.json`;
    console.log('botsJsonPath getBotsConfig: ', botsJsonPath)
    return this.http.get(botsJsonPath);
  }

  /**
   * Fetch detailed bot information from Bot.json.
   * @param botJsonPath - The URL to the Bot.json file.
   */
  getBotDetails(botJsonPath: string): Observable<any> {
    console.log('botJsonPath getBotDetails: ', botJsonPath)
    return this.http.get(botJsonPath);
  }

  /**
   * Fetch and download the bot ZIP file.
   * @param language - The language folder of the bot.
   * @param botName - The bot's name.
   */
  downloadBot(language: string, botName: string): Observable<Blob> {
    const zipPath = `${this.BASE_PATH}/${language}/${botName}/${botName}.zip`;
    console.log('zipPath downloadBot: ', zipPath)
    return this.http.get(zipPath, { responseType: 'blob' });
  }

  getBotDetailsByName(botName: string) {
    const url = `${this['BASE_PATH']}/bots/${botName}/Bot.json`;
    console.log('url getBotDetailsByName: ', url)
    return this.http.get(url);
  }
}