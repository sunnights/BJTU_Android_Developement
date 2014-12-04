#include <sys/types.h>
#include <unistd.h>
#include <grp.h>
#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>

#include "IHelloWorld.h"
namespace android{
int main()
{
        printf("Hello client is now starting");
        android::sp<android::IServiceManager> sm = android::defaultServiceManager();
        android::sp<android::IBinder> binder;
        android::sp<android::IHelloWorld> shw;
        
        do {
                binder = sm->getService(String16(HELLOWORLD_NAME));
                if (binder != 0)
                        break;
                printf("HelloWorld not published. wating...");
                usleep(500000);
        } while(true);

        printf("Hello client is now trying");
        shw = interface_cast<IHelloWorld>(binder);
        shw.hellothere("fun");
        printf("Hello client is now writing");
        
        return 0;
}
}
