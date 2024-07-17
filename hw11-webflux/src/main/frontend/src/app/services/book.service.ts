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
    return this.http.get<Book[]>("api/books");
  }

  getAllAuthors(): Observable<Author[]> {
    return this.http.get<Author[]>("api/authors");
  }

  getAllGenres(): Observable<Genre[]> {
    return this.http.get<Genre[]>("api/genres");
  }

  setBook(book: Book) {
    this.currentBook = book;
  }

  getCurrentBook() {
    return this.currentBook;
  }

  update(book: Book) {
    let genreIds = book.genres.map(genre => genre.id).join(',');
    let httpParams = new HttpParams().append('id', book.id)
    .append('title', book.title)
    .append('authorId', book.author.id)
    .append('genresIds', genreIds);
    return this.http.put("api/books/update", "", {
      params: httpParams
    });
  }

  create(book: Book) {
    let genreIds = book.genres.map(genre => genre.id).join(',');
    let httpParams = new HttpParams()
    .append('title', book.title)
    .append('authorId', book.author.id)
    .append('genresIds', genreIds);
    return this.http.post("api/books", "", {
      params: httpParams
    });
  }

  delete(id: number) {
    const url = `api/books/delete/${id}`;
    return this.http.delete(url);
  }
}
