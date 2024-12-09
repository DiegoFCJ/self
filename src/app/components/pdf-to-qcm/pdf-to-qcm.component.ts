import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-pdf-to-qcm',
  templateUrl: './pdf-to-qcm.component.html',
  styleUrls: ['./pdf-to-qcm.component.scss'],
  standalone: true,
  imports: [CommonModule, HeaderComponent],
})
export class PdfToQcmComponent {
  uploadedFile: File | null = null;
  qcmContent: string = '';

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.uploadedFile = input.files[0];
      this.processPDF();
    }
  }

  async processPDF() {
    if (!this.uploadedFile) return;
    this.qcmContent = `Elaborazione in corso per ${this.uploadedFile?.name}...`;

    // Simulazione della conversione
    setTimeout(() => {
      this.qcmContent = `Il file ${this.uploadedFile?.name} è stato convertito correttamente in formato QCM!`;
    }, 1000);
  }
}