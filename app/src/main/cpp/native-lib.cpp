#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_maishuo_tingshuohenhaowan_common_encrypt_EncryptNDK_getAesKey(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "5%h==n_n_~h$k33!";
    return env->NewStringUTF(hello.c_str());
    //com.maishuo.tingshuohenhaowan.common.encrypt
}
