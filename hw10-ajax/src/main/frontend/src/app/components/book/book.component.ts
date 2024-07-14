import {Component} from "@angular/core";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book";
import {NavigationEnd, Router} from "@angular/router";
import {NgForOf} from "@angular/common";
import {filter, Subscription} from "rxjs";

@Component({
  standalone: true,
  selector: 'app-book',
  templateUrl: './book.component.html',
  imports: [
    NgForOf
  ]
})
export class BookComponent {
  books: Book[] = [];
  private routerSubscription: Subscription | null = null;

  constructor(private bookService: BookService,
              private router: Router) {
  }

  ngOnInit() {
    this.initBooks();
    this.routerSubscription = this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.initBooks();
    });
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  initBooks(): void {
    this.bookService.getAllBooks().subscribe(books => {
      console.log('books lenght: ' + books.length)
      this.books = books
    })
  }

  openBook(book: Book) {
    this.bookService.setBook(book)
    this.router.navigate(['edit-book']).then(r => {
    })
  }
}
