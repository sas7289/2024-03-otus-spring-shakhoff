import { Routes } from '@angular/router';
import {BookComponent} from "./components/book/book.component";
import {EditBookComponent} from "./components/edit-book/edit-book.component";
import {CreateBookComponent} from "./components/create-book/create-book.component";

export const routes: Routes = [
  {path: 'books', component: BookComponent},
  {path: 'edit-book', component: EditBookComponent},
  {path: 'create-book', component: CreateBookComponent},
  {path: '', redirectTo: 'books', pathMatch: 'full'}
];
