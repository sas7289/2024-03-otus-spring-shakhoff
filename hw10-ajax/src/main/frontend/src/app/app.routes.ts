import { Routes } from '@angular/router';
import {BookComponent} from "./components/book/book.component";
import {EditBookComponent} from "./components/edit-book/edit-book.component";

export const routes: Routes = [
  {path: 'books', component: BookComponent},
  {path: 'edit-book', component: EditBookComponent},
  {path: '', redirectTo: 'books', pathMatch: 'full'}
];
