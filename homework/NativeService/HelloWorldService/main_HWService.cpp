#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>

#include "hwStub.h"

int main()
{
        HelloWorldService::instantiate();
        android::ProcessState::self()->startThreadPool();
        printf("--- Hello Service is now ready ---\n");
        android::IPCThreadState::self()->joinThreadPool();
        return 0;
}
