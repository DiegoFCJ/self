import { Routes } from '@angular/router';
import { BotListComponent } from './components/bot-list/bot-list.component';
import { PdfToQcmComponent } from './components/pdf-to-qcm/pdf-to-qcm.component';

export const routes: Routes = [
  {
    path: '', // Rotta di default
    component: BotListComponent
  },
  {
    path: 'pdf-to-qcm',
    component: PdfToQcmComponent
  },
  {
    path: '**', // Rotta per errori 404
    loadComponent: () => import('./components/not-found/not-found.component').then(m => m.NotFoundComponent)
  }
];