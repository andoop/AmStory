LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_LDLIBS :=-llog

LOCAL_MODULE := data-process-jni
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	empty.c \
	data-process-jni.c \

LOCAL_C_INCLUDES += G:\sdk\project\XXX\amstory\src\main\jni

include $(BUILD_SHARED_LIBRARY)
