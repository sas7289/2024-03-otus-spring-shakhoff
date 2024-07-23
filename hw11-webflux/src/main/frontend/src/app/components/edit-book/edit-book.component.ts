import {Component} from "@angular/core";
import {Book} from "../../models/book";
import {BookService} from "../../services/book.service";
import {FormsModule, NgForm} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {Genre} from "../../models/genre";
import {Author} from "../../models/author";
import {Router} from "@angular/router";

@Component({
  standalone: true,
  selector: 'edit-book',
  templateUrl: './edit-book.component.html',
  imports: [
    FormsModule,
    NgForOf
  ]
})
export class EditBookComponent {

  currentBook: Book = {
    author: {id: '', fullName: ""},
    genres: [],
    id: '',
    title: ""
  };
  authors: Author[] = [];
  genres: Genre[] = [];
  selectedGenres: Genre[] = []

  constructor(private bookService: BookService,
              private router: Router) {
  }

  ngOnInit() {
    this.currentBook = this.bookService.getCurrentBook();
    this.bookService.getAllGenres().subscribe(value => {
      this.genres = value;
      this.genres.forEach(genre => {
        if (this.currentBook.genres.map(genre => genre.id).includes(genre.id)) {
          this.selectedGenres.push(genre)
        }
      })
    })

    this.bookService.getAllAuthors().subscribe(value => {
      this.authors = value;
    })
  }

  updateBook(myBookForm: NgForm) {
    if (myBookForm.valid) {
      let selectedAuthor = this.authors.find(author => author.id === myBookForm.value.author);
      if (selectedAuthor) {
        let bookToUpdate = {
          id: this.currentBook.id,
          title: myBookForm.value.title,
          author: {
            id: selectedAuthor.id,
            fullName: selectedAuthor.fullName
          },
          genres: this.selectedGenres
        };
        this.bookService.update(bookToUpdate).subscribe(value => {
          this.navigateToAllBooks()
        })
      }
    }
  }

  processCheckBox(event: Event, genre: Genre) {
    const target = event.target as HTMLInputElement;
    if (target.checked) {
      this.selectedGenres.push(genre)
    } else {
      let genreIndex = this.selectedGenres.indexOf(genre);
      this.selectedGenres.splice(genreIndex, 1);
    }
  }

  isGenreCheck(genre: Genre) {
    return this.currentBook.genres.map(genre => genre.id).includes(genre.id);
  }

  deleteBook(id: string) {
    this.bookService.delete(id).subscribe(value => {
      this.navigateToAllBooks();
    })
  }

  navigateToAllBooks() {
    this.router.navigate(['books']).then(r => {
    })
  }

  isAuthorSelected(id: string) {
    let b = this.currentBook.author.id === id;
    console.log(`author selected. id: ${id} : ${b}`)
    return this.currentBook.author.id === id;
  }
}
