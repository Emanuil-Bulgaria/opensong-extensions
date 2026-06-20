package org.example

enum class NDIFrameFormatType(val code: Int) {
    PROGRESSIVE(1),
    INTERLEAVED(0),
    FIELD_0(2),
    FIELD_1(3),
}