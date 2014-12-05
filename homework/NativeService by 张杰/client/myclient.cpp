#include <binder/IServiceManager.h>
#include <binder/IPCThreadState.h>
#include "myclient.h"

namespace android

{
sp<IBinder> binder;
void myclient::show()
{
getmyService();
Parcel data, reply;
data.writeInt32(getpid());
binder->transact(0, data, &reply);
printf("%s",reply.readCString());
}


void myclient::getmyService()
{
sp<IServiceManager> sm = defaultServiceManager();
binder = sm->getService(String16("android_zj"));
if(binder == 0)
{
return;
}
}
};

using namespace android;
int main(int argc, char** argv)
{
myclient* p = new myclient();
p->show();
return 0;
}
