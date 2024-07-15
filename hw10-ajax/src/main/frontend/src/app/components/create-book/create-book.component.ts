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
  selector: 'create-book',
  templateUrl: './create-book.component.html',
  imports: [
    FormsModule,
    NgForOf
  ]
})
export class CreateBookComponent {

  book: Book = {
    author: {id: 0, fullName: ""},
    genres: [],
    id: 0,
    title: ""
  };
  authors: Author[] = [];
  genres: Genre[] = [];
  selectedGenres: Genre[] = []

  constructor(private bookService: BookService,
              private router: Router) {
  }

  ngOnInit() {
    this.book = this.bookService.getCurrentBook();
    this.bookService.getAllGenres().subscribe(value => {
      this.genres = value;
      this.genres.forEach(genre => {
        if (this.book.genres.map(genre => genre.id).includes(genre.id)) {
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
      let bookToUpdate = {
        id: this.book.id,
        title: myBookForm.value.title,
        author: {
          id: myBookForm.value.author.id,
          fullName: myBookForm.value.author.fullName
        },
        genres: this.selectedGenres
      };
      this.bookService.update(bookToUpdate).subscribe(value => {
        this.router.navigate(['books']).then(r => {
        })
      })
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
    return this.book.genres.map(genre => genre.id).includes(genre.id);
  }
}
