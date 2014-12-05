#include <binder/IServiceManager.h>
#include <binder/IPCThreadState.h>
#include "myservice.h"
namespace android
{
static pthread_key_t sigbuskey;	
int myservice::Instance()
{
int ret = defaultServiceManager()->addService(String16("android_zj"), new myservice());
return ret;
}
myservice::myservice()
{
pthread_key_create(&sigbuskey, NULL);
}
myservice::~myservice()
{
pthread_key_delete(sigbuskey);
}
status_t myservice::onTransact(uint32_t code, const Parcel& data, Parcel* reply, uint32_t flags)
{
reply->writeCString("Hello,zj has connected---by group-3!.....\n");
return NO_ERROR;
}
}
