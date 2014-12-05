LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
# our own branch needs these headers
LOCAL_C_INCLUDES +=
LOCAL_PRELINK_MODULE := false
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := \
liblog \
libcutils \
libutils \
libbinder \
libmyservice
LOCAL_SRC_FILES := myserver.cpp
LOCAL_MODULE := myserver
include $(BUILD_EXECUTABLE)
