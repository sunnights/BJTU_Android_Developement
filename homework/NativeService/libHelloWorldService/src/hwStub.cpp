#include <binder/IServiceManager.h>
#include <utils/String16.h>
#include <utils/String8.h>

#include "hwStub.h"

HelloWorldService::HelloWorldService()
{
        printf("--- HelloWorldService created ---\n");
}

HelloWorldService::~HelloWorldService()
{
        printf("--- HelloWorldService destroyed ---\n");
}

void HelloWorldService::instantiate() {
        android::defaultServiceManager()->addService(IHelloWorld::descriptor, new HelloWorldService());
}

void HelloWorldService::hellothere(const char *str) {
        printf("# HelloWorldService : %s #\n", str);
}

android::status_t BnHelloWorld::onTransact(uint32_t code,
                                        const android::Parcel &data,
                                        android::Parcel *reply,
                                        uint32_t flags)
{
        printf("OnTransact(%u, %u)\n", code, flags);
        switch(code) {
        case HW_HELLOTHERE: {
                const char *str = data.readCString();
                printf("str:%s\n",data.readCString());
                hellothere(str);
                return android::NO_ERROR;
        } break;
        default:
                return BBinder::onTransact(code, data, reply, flags);
        }
        return android::NO_ERROR;
}
