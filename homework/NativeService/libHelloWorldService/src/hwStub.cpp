#include <string.h>
#include <cutils/atomic.h>
#include <utils/Errors.h>
#include <binder/IServiceManager.h>
#include <utils/String16.h>
#include <utils/String8.h>
#include <binder/IPCThreadState.h>
#include "hwStub.h"

namespace android {

void HelloWorldService::instantiate() {
        defaultServiceManager()->addService(IHelloWorld::descriptor, new HelloWorldService());
}

void HelloWorldService::hellothere(const char *str) {
        printf("hello: %s\n", str);
}

status_t HelloWorldService::onTransact(uint32_t code,
                                        const Parcel &data,
                                        Parcel *reply,
                                        uint32_t flags)
{
        printf("OnTransact(%u, %u)", code, flags);
        switch(code) {
        case HW_HELLOTHERE: {
                String16 str = data.readString16();
                hellothere(String8(str).string());
                return NO_ERROR;
        } break;
        default:
                return BBinder::onTransact(code, data, reply, flags);
                ;
        }
        return NO_ERROR;
}

}
