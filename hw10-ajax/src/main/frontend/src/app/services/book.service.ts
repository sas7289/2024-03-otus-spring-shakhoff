import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Book} from "../models/book";
import {Genre} from "../models/genre";
import {Author} from "../models/author";


@Injectable({
  providedIn: 'root'
})
export class BookService {
  currentBook: Book = {
    author: {id: 0, fullName: ""},
    genres: [],
    id: 0,
    title: ""
  };

  constructor(private http: HttpClient) {
  }

  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>("/books");
  }

  getAllAuthors(): Observable<Author[]> {
    return this.http.get<Author[]>("/authors");
  }

  getAllGenres(): Observable<Genre[]> {
    return this.http.get<Genre[]>("/genres");
  }

  setBook(book: Book) {
    this.currentBook = book;
  }

  getCurrentBook() {
    return this.currentBook;
  }

  updateBook(book: Book) {
    let genreIds = book.genres.map(genre => genre.id).join(',');
    console.log("genres: " + JSON.stringify(genreIds))
    let httpParams = new HttpParams().append('id', book.id)
    .append('title', book.title)
    .append('authorId', book.author.id)
    .append('genresIds', genreIds);
    return this.http.post("/books/update", "", {
      params: httpParams
    });
  }

  deleteBook(id: number) {
    const url = `books/delete/${id}`;
    // let httpParams = new HttpParams().append('id', book.id)
    // .append('title', book.title)
    // .append('authorId', book.author.id)
    // .append('genresIds', genreIds);
    return this.http.delete(url);
  }
}
