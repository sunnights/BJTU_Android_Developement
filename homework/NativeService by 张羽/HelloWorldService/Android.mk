LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := main_HWService.cpp

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../libHelloWorldService/include

LOCAL_MODULE := HelloWorldService
LOCAL_MODULE_TAGS := optional

LOCAL_PRELINK_MODULE := false

LOCAL_SHARED_LIBRARIES += liblog
LOCAL_SHARED_LIBRARIES += libutils libui
LOCAL_SHARED_LIBRARIES += libbinder
LOCAL_SHARED_LIBRARIES += libHelloWorldService

include $(BUILD_EXECUTABLE)
