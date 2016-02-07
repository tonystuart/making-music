#include <jni.h>
#include <fluidsynth.h>

JNIEXPORT void JNICALL Java_com_example_afs_fluidsynth_FluidSynth_deleteFluidAudioDriver
  (JNIEnv *env, jclass this, jlong driver) {
  	delete_fluid_audio_driver(driver);
}

JNIEXPORT void JNICALL Java_com_example_afs_fluidsynth_FluidSynth_deleteFluidSynth
  (JNIEnv *env, jclass this, jlong synth) {
  	delete_fluid_synth(synth);
}

JNIEXPORT void JNICALL Java_com_example_afs_fluidsynth_FluidSynth_deleteFluidSettings
  (JNIEnv *env, jclass this, jlong settings) {
  	delete_fluid_settings(settings);
}

JNIEXPORT void JNICALL Java_com_example_afs_fluidsynth_FluidSynth_fluidSettingsSetstr
  (JNIEnv *env, jclass this, jlong settings, jstring name, jstring str) {
	fluid_settings_setstr(settings, (*env)->GetStringUTFChars(env, name, NULL), (*env)->GetStringUTFChars(env, str, NULL));
}  	

JNIEXPORT jint JNICALL Java_com_example_afs_fluidsynth_FluidSynth_fluidSynthCc
  (JNIEnv *env, jclass this, jlong synth, jint chan, jint num, jint val) {
  	return fluid_synth_cc(synth, chan, num, val);
}

JNIEXPORT jint JNICALL Java_com_example_afs_fluidsynth_FluidSynth_fluidSynthNoteoff
  (JNIEnv *env, jclass this, jlong synth, jint chan, jint key) {
  	return fluid_synth_noteoff(synth, chan, key);
}

JNIEXPORT jint JNICALL Java_com_example_afs_fluidsynth_FluidSynth_fluidSynthNoteon
  (JNIEnv *env, jclass this, jlong synth, jint chan, jint key, jint vel) {
  	return fluid_synth_noteon(synth, chan, key, vel);
}

JNIEXPORT jint JNICALL Java_com_example_afs_fluidsynth_FluidSynth_fluidSynthProgramChange
  (JNIEnv *env, jclass this, jlong synth, jint chan, jint program) {
  	return fluid_synth_program_change(synth, chan, program);
}

JNIEXPORT jlong JNICALL Java_com_example_afs_fluidsynth_FluidSynth_fluidSynthSfload
  (JNIEnv *env, jclass this, jlong synth, jstring filename, jint reset_presets) {
  	return fluid_synth_sfload(synth, (*env)->GetStringUTFChars(env, filename, NULL), reset_presets);
}

JNIEXPORT jlong JNICALL Java_com_example_afs_fluidsynth_FluidSynth_newFluidAudioDriver
  (JNIEnv *env, jclass this, jlong settings, jlong synth) {
  	return new_fluid_audio_driver(settings, synth);
}

JNIEXPORT jlong JNICALL Java_com_example_afs_fluidsynth_FluidSynth_newFluidSettings
  (JNIEnv *env, jclass this) {
  	return new_fluid_settings();
}

JNIEXPORT jlong JNICALL Java_com_example_afs_fluidsynth_FluidSynth_newFluidSynth
  (JNIEnv *env, jclass this, jlong settings) {
	return new_fluid_synth(settings);
}

