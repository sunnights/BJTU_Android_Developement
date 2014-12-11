LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := com_homework3_fibonaccinative_FibLib.c  # <1>
LOCAL_MODULE    := com_homework3_fibonaccinative_FibLib    # <2>

include $(BUILD_SHARED_LIBRARY)
