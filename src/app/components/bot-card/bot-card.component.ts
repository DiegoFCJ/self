import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-bot-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './bot-card.component.html',
  styleUrl: './bot-card.component.scss'
})
export class BotCardComponent {
  @Input() bot: any;
  @Input() language: string = '';
  @Output() download = new EventEmitter<void>();

  constructor(private router: Router) {}

  openBot() {
    this.bot.sourcePath = `https://github.com/DiegoFCJ/scriptagher/tree/bot-list/bots/${this.bot.language}/${this.bot.botName}`
    console.log('this.bot.sourcePath', this.bot.sourcePath)
    window.open(this.bot.sourcePath || '#', '_blank');
  }

  downloadBot() {
    this.download.emit();
  }

  navigateToPdfToQcm() {
    try {
      this.router.navigate(['/pdf-to-qcm']);
    } catch (error) {
      console.error('Errore durante la navigazione verso la pagina PDF to QCM:', error);
    }
  }
}
