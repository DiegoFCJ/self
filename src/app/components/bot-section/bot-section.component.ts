import { Component, Input, OnInit } from '@angular/core';
import { BotCardComponent } from '../bot-card/bot-card.component';
import { CommonModule } from '@angular/common';
import { BotService } from '../../services/bot.service';

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
export class BotSectionComponent implements OnInit{
  @Input() language: string = '';
  @Input() bots: any[] = [];

  constructor(private botService: BotService) {}
  ngOnInit(): void {
    console.log('bots', this.bots);
  }

  downloadBot(bot: any) {
    bot.language = this.language;
    this.botService.downloadBot(bot).subscribe({
      next: (blob: Blob) => {
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = `${bot.botName}.zip`;
        link.click();
      },
      error: (error: any) => {
        console.error('Error downloading bot:', error);
        alert('Could not download the bot. Please try again.');
      }
    })
  }
    
  capitalize(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }
}
