import { Component, Input } from '@angular/core';
import { BotCardComponent } from '../bot-card/bot-card.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-bot-section',
  standalone: true,
  imports: [
    CommonModule,
    BotCardComponent
  ],
  templateUrl: './bot-section.component.html',
  styleUrl: './bot-section.component.scss'
})
export class BotSectionComponent {
  @Input() language: string = '';
  @Input() bots: any[] = [];

  downloadBot(bot: any) {
    console.log(`Downloading ${bot.botName}...`);
  }

  capitalize(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }
}
