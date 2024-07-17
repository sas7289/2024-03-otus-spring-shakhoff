import {Genre} from "./genre";
import {Author} from "./author";

export interface Book {
  id: string
  title: string
  genres: Genre[]
  author: Author
}
