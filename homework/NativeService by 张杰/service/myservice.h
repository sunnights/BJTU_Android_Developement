#ifndef ANDROID_HELLOWORLDSERVICE_H
#define ANDROID_HELLOWORLDSERVICE_H
#include <utils/RefBase.h>
#include <binder/IInterface.h>
#include <binder/Parcel.h>
namespace android
{
class myservice : public BBinder
{
public:
static int Instance();
myservice();
virtual ~myservice();
virtual status_t onTransact(uint32_t, const Parcel&, Parcel*, uint32_t);
};
};
#endif
