.class Loicq/wlogin_sdk/oidb/NameUinInfo$1;
.super Ljava/lang/Object;
.source "NameUinInfo.java"

# interfaces
.implements Landroid/os/Parcelable$Creator;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Loicq/wlogin_sdk/oidb/NameUinInfo;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Landroid/os/Parcelable$Creator",
        "<",
        "Loicq/wlogin_sdk/oidb/NameUinInfo;",
        ">;"
    }
.end annotation


# direct methods
.method constructor <init>()V
    .locals 0

    .prologue
    .line 59
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 1
    return-void
.end method


# virtual methods
.method public bridge synthetic createFromParcel(Landroid/os/Parcel;)Ljava/lang/Object;
    .locals 1
    .parameter

    .prologue
    .line 1
    invoke-virtual {p0, p1}, Loicq/wlogin_sdk/oidb/NameUinInfo$1;->createFromParcel(Landroid/os/Parcel;)Loicq/wlogin_sdk/oidb/NameUinInfo;

    move-result-object v0

    return-object v0
.end method

.method public createFromParcel(Landroid/os/Parcel;)Loicq/wlogin_sdk/oidb/NameUinInfo;
    .locals 2
    .parameter "source"

    .prologue
    .line 64
    new-instance v0, Loicq/wlogin_sdk/oidb/NameUinInfo;

    const/4 v1, 0x0

    invoke-direct {v0, p1, v1}, Loicq/wlogin_sdk/oidb/NameUinInfo;-><init>(Landroid/os/Parcel;Loicq/wlogin_sdk/oidb/NameUinInfo;)V

    return-object v0
.end method

.method public bridge synthetic newArray(I)[Ljava/lang/Object;
    .locals 1
    .parameter

    .prologue
    .line 1
    invoke-virtual {p0, p1}, Loicq/wlogin_sdk/oidb/NameUinInfo$1;->newArray(I)[Loicq/wlogin_sdk/oidb/NameUinInfo;

    move-result-object v0

    return-object v0
.end method

.method public newArray(I)[Loicq/wlogin_sdk/oidb/NameUinInfo;
    .locals 1
    .parameter "size"

    .prologue
    .line 70
    new-array v0, p1, [Loicq/wlogin_sdk/oidb/NameUinInfo;

    return-object v0
.end method
