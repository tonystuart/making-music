set -x

rm -f com_example_afs_fluidsynth_FluidSynth.o
rm -f libfluidsynth_jni.so

JAVA_HOME=$(readlink -f $(which javac) | sed "s:/bin/javac::")

gcc -c -fPIC \
  -I$JAVA_HOME/include \
  -I$JAVA_HOME/include/linux \
  -o com_example_afs_fluidsynth_FluidSynth.o \
  com_example_afs_fluidsynth_FluidSynth.c

gcc -shared \
  -o libfluidsynth_jni.so \
  com_example_afs_fluidsynth_FluidSynth.o \
  -lfluidsynth

if [[ -d ../lib && -e libfluidsynth_jni.so ]]
then
  cp libfluidsynth_jni.so ../lib
fi
