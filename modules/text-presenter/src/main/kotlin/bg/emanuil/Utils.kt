package bg.emanuil

object Utils {
    fun fourCC(ch0: Char, ch1: Char, ch2: Char, ch3: Char): Int =
        ch0.code or (ch1.code shl 8) or (ch2.code shl 16) or (ch3.code shl 24)
}