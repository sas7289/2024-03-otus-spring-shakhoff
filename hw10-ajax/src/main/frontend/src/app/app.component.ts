import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {BookComponent} from "./components/book/book.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, BookComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'hw10-ajax';
}
