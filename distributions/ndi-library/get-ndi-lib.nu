#!/usr/bin/env nu

mkdir build/lib
cp /lib/libndi.so build/lib/libndi.so
chmod +w build/lib/libndi.so
patchelf --set-rpath '$ORIGIN' build/lib/libndi.so
chmod 666 build/lib/libndi.so