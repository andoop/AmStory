/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <andoop_android_amstory_jni_AudioDataProcessor.h>
#include <android/log.h>

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
//jstring
//Java_com_bazhangkeji_MainActivity_stringFromJNI( JNIEnv* env,
//                                                  jobject thiz )
//{
//    return (*env)->NewStringUTF(env, "Hello from JNI !");
//}
#define TAG "######" // 这个是自定义的LOG的标识
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)


JNIEXPORT jstring JNICALL
Java_andoop_android_amstory_jni_AudioDataProcessor_test(JNIEnv *env, jobject instance) {

//    // TODO
//
//
//    return (*env)->NewStringUTF(env, returnValue);
    return (*env)->NewStringUTF(env, "Hello from JNI !");
}

JNIEXPORT jshortArray JNICALL
Java_andoop_android_amstory_jni_AudioDataProcessor_reduceToPercent(JNIEnv *env, jclass instance, jshortArray arr, jint percent){
    jshort *carr;
    carr = (*env)->GetShortArrayElements(env, arr,0);
    //2.获取数组长度
    int len = (*env)->GetArrayLength(env,arr);
    int i;
    for(i=0;i<len;i++){
       // LOGE("########## i = %d",carr[i]);
        carr[i]=carr[i]*percent/100;
       // LOGE("########## i = %d",carr[i]);
    }

    //把carr复制给arr,从第0位开始,长度位len
    (*env)->SetShortArrayRegion(env,arr,0,len,carr);
    (*env)->ReleaseShortArrayElements(env,arr,carr, 0);

    return arr;
}