import {Genre} from "./genre";
import {Author} from "./author";

export interface Book {
  id: number
  title: string
  genres: Genre[]
  author: Author
}
