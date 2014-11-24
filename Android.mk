LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES:= myHelloWorld.c
LOCAL_MODULE := myHelloWorld
# LOCAL_CFLAGS := -DWITHOUT_IFADDRS -Wno-sign-compare
include $(BUILD_EXECUTABLE)
