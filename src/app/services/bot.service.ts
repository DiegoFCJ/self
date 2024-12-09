import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BotService {
  private readonly APP_NAME = 'scriptagher';
  private readonly BASE_PATH = `https://raw.githubusercontent.com/diegofcj/${this.APP_NAME}`;
  private readonly BASE_SOURCE = `https://github.com/DiegoFCJ/${this.APP_NAME}`;
  private readonly BOTS = 'bots';
  private readonly GH_PAGES = `${this.BASE_PATH}/gh-pages/${this.BOTS}/bots.json`;
  private readonly TREE_BOT_LIST = `${this.BASE_SOURCE}/tree/gh-pages/${this.BOTS}`

  constructor(private http: HttpClient) { }

  botDetailsPath(botName: string, language: string){
    return `${this.TREE_BOT_LIST}/${language}/${botName}/Bot.json`
  }  
  
  /**
   * Fetch the bots configuration from bots.json.
   */
  getBotsConfig(): Observable<any> {
    const botsJsonPath = this.GH_PAGES;
    return this.http.get(botsJsonPath);
  }

  openBot(bot: any) {
    bot.sourcePath = `${this.TREE_BOT_LIST}/${bot.language}/${bot.botName}`
    window.open(bot.sourcePath || '#', '_blank');
  }

  /**
   * Fetch detailed bot information from Bot.json.
   * @param botJsonPath - The URL to the Bot.json file.
   */
  getBotDetails(botJsonPath: string): Observable<any> {
    return this.http.get(botJsonPath);
  }

  /**
   * Fetch and download the bot ZIP file.
   * @param language - The language folder of the bot.
   * @param botName - The bot's name.
   */
  downloadBot(language: string, botName: string): Observable<Blob> {
    const zipPath = `${this.BASE_PATH}/${language}/${botName}/${botName}.zip`;
    return this.http.get(zipPath, { responseType: 'blob' });
  }

  getBotDetailsByName(botName: string) {
    const url = `${this['BASE_PATH']}/bots/${botName}/Bot.json`;
    return this.http.get(url);
  }
}