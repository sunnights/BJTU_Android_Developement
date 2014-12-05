#include <binder/Parcel.h>

#include "IHelloWorld.h"

class BpHelloWorld: public android::BpInterface<IHelloWorld>
{
public:
        BpHelloWorld(const android::sp<android::IBinder>& impl)
                : android::BpInterface<IHelloWorld>(impl) {}
        
        virtual void hellothere(const char *str) {
                // printf("str: %s\n",str);
                android::Parcel data, reply;
                data.writeInterfaceToken(IHelloWorld::getInterfaceDescriptor());
                data.writeCString(str);
                remote()->transact(HW_HELLOTHERE, data, &reply, android::IBinder::FLAG_ONEWAY);
        }
};

IMPLEMENT_META_INTERFACE(HelloWorld, HELLOWORLD_NAME);

