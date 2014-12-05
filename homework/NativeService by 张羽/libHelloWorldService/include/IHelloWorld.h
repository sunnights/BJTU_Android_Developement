#ifndef _HELLOWORLD_H_
#define _HELLOWORLD_H_

#include <binder/IInterface.h>

#define HELLOWORLD_NAME "org.zy.HelloWorld"

enum {
        HW_HELLOTHERE = 1,
};

class IHelloWorld : public android::IInterface {
public:
        DECLARE_META_INTERFACE(HelloWorld); 
        virtual void hellothere(const char *str) = 0;
};

#endif
