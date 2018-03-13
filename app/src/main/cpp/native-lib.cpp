#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_shenhua_opencv_1android_helper_ProcessHelper_gray2(JNIEnv *env, jobject instance,
                                                            jintArray pix_, jint width,
                                                            jint height) {
    jint *cbuf;
    cbuf = env->GetIntArrayElements(pix_, JNI_FALSE);
    if (cbuf == NULL) {
        return 0;
    }
}
