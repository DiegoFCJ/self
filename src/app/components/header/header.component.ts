import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  isPdfToQcmPage: boolean = false;

  constructor(private router: Router) {
    // Ascolta il router per sapere se siamo nella pagina PDF to QCM
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isPdfToQcmPage = event.url === '/pdf-to-qcm';
      });
  }

  navigateHome() {
    this.router.navigate(['/']);
  }
}
