package bg.emanuil.opensong

import java.io.File
import kotlin.test.Test

class OpenSongTest {

    @Test
    fun test() {
        val getVerse = OpenSongVerseSearcher(File("C:\\Users\\Presenter\\Documents\\OpenSong tutorial"))
        val verse = getVerse("А Йоас каза на всички, които стояха срещу него: Вие ли ще защитите Ваал? Или вие ли ще го избавите? Който го защити, да бъде умъртвен до сутринта. Ако е бог, нека сам се защити, след като Гедеон е съборил олтара му.")
        println(verse)
    }
}