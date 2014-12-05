#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>

#include "IHelloWorld.h"

int main()
{
        printf("--- Hello client is now starting ---\n");
        android::sp<android::IServiceManager> sm = android::defaultServiceManager();
        android::sp<android::IBinder> binder;
        android::sp<IHelloWorld> shw;
        
        do {
                binder = sm->getService(android::String16(HELLOWORLD_NAME));
                if (binder != 0)
                        break;
                printf("HelloWorld not published. wating...\n");
                usleep(500000);
        } while(true);

        printf("--- Hello client is now trying ---\n");
        shw = android::interface_cast<IHelloWorld>(binder);
        shw->hellothere("# Group 3 - ZY #");
        printf("--- Hello client is now exiting ---\n");
        
        return 0;
}
