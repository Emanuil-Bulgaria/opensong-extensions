package org.example

enum class NDIFourCCVideoType(val code: Int) {
    UYVY(Utils.fourCC('U', 'Y', 'V', 'Y')),
    UYVA(Utils.fourCC('U', 'Y', 'V', 'A')),
    P216(Utils.fourCC('P', '2', '1', '6')),
    PA16(Utils.fourCC('P', 'A', '1', '6')),
    YV12(Utils.fourCC('Y', 'V', '1', '2')),
    I420(Utils.fourCC('I', '4', '2', '0')),
    NV12(Utils.fourCC('P', '2', '1', '6')),
    BGRA(Utils.fourCC('B', 'G', 'R', 'A')),
    BGRX(Utils.fourCC('B', 'G', 'R', 'X')),
    RGBA(Utils.fourCC('R', 'G', 'B', 'A')),
    RGBX(Utils.fourCC('R', 'G', 'B', 'X')),
}
