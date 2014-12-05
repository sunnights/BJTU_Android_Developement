LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_C_INCLUDES +=
LOCAL_PRELINK_MODULE := false
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := \
liblog \
libcutils \
libutils \
libbinder \
libmyservice
LOCAL_SRC_FILES := myclient.cpp
LOCAL_MODULE := myclient
include $(BUILD_EXECUTABLE)
