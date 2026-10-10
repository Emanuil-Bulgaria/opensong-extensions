package bg.emanuil.opensong

import bg.emanuil.opensong.model.Bible
import bg.emanuil.opensong.model.Book
import bg.emanuil.opensong.model.Chapter
import bg.emanuil.opensong.model.Verse
import bg.emanuil.opensong.model.x_bible_format.OpenBibleFormat

fun Bible.toVerseMap() = this.books.flatMap { book ->
    book.chapters.flatMap { chapter ->
        chapter.verses.map { verse ->
            verse.text to "${book.name} ${chapter.number}:${verse.number}"
        }
    }
}.toMap()

fun OpenBibleFormat.toBible(): Bible {
    return Bible(this.books.map { book ->
        val content = book.chapters.map { chapter ->
            val content = chapter.verses.map { verse ->
                Verse(verse.number, verse.text)
            }
            Chapter(chapter.number, content)
        }
        Book(book.name, content)
    })
}