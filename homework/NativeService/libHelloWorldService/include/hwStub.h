#ifndef _HWSTUB_H_
#define _HWSTUB_H_

#include <binder/Parcel.h>
#include "IHelloWorld.h"

class BnHelloWorld : public android::BnInterface<IHelloWorld>
{
public:
        android::status_t onTransact(uint32_t code,
                                const android::Parcel &data,
                                android::Parcel *reply,
                                uint32_t flags);
};

class HelloWorldService : public BnHelloWorld
{
public:
        HelloWorldService();
        virtual ~HelloWorldService();
        
        static void instantiate();
        virtual void hellothere(const char *str);
};

#endif
