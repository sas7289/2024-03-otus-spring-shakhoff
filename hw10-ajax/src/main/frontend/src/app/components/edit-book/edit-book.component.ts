import {Component} from "@angular/core";
import {Book} from "../../models/book";
import {BookService} from "../../services/book.service";
import {FormsModule, NgForm} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {Genre} from "../../models/genre";
import {Author} from "../../models/author";
import {HttpParams} from "@angular/common/http";
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
    console.log('constructor')
  }

  ngOnInit() {
    this.book = this.bookService.getCurrentBook();
    this.bookService.getAllGenres().subscribe(value => {
      console.log("genres: " + value)
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
    console.log('updateBook')
    console.log(myBookForm.value)
    console.log(this.selectedGenres)

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
      this.bookService.updateBook(bookToUpdate).subscribe(value => {
        this.router.navigate(['books']).then(r => {
        })
      })
    }
  }

  processCheckBox(event: Event, genre: Genre) {
    const target = event.target as HTMLInputElement;
    console.log('checkboxEvent check:' + target.checked);
    console.log('genre: ' + genre.name);
    if (target.checked) {
      this.selectedGenres.push(genre)
    } else {
      let genreIndex = this.selectedGenres.indexOf(genre);
      this.selectedGenres.splice(genreIndex, 1);
    }
  }

  isGenreCheck(genre: Genre) {
    console.log("genre: " + genre.name + " exist: " + this.book.genres.map(genre => genre.id).includes(genre.id))
    return this.book.genres.map(genre => genre.id).includes(genre.id);
  }

  deleteBook(id: number) {
    this.bookService.deleteBook(id).subscribe(value => {
      this.router.navigate(['books']).then(r => {
      })
    })
  }
}
