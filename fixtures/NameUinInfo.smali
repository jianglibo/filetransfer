.class public Loicq/wlogin_sdk/oidb/NameUinInfo;
.super Ljava/lang/Object;
.source "NameUinInfo.java"

# interfaces
.implements Ljava/io/Serializable;
.implements Landroid/os/Parcelable;


# static fields
.field public static final CREATOR:Landroid/os/Parcelable$Creator; = null
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroid/os/Parcelable$Creator",
            "<",
            "Loicq/wlogin_sdk/oidb/NameUinInfo;",
            ">;"
        }
    .end annotation
.end field

.field private static final serialVersionUID:J = 0x5939dba95ce8f65cL


# instance fields
.field public name:Ljava/lang/String;

.field public tag1:I

.field public tag2:I

.field public uin:J


# direct methods
.method static constructor <clinit>()V
    .locals 1

    .prologue
    .line 59
    new-instance v0, Loicq/wlogin_sdk/oidb/NameUinInfo$1;

    invoke-direct {v0}, Loicq/wlogin_sdk/oidb/NameUinInfo$1;-><init>()V

    sput-object v0, Loicq/wlogin_sdk/oidb/NameUinInfo;->CREATOR:Landroid/os/Parcelable$Creator;

    .line 72
    return-void
.end method

.method public constructor <init>(JIILjava/lang/String;)V
    .locals 0
    .parameter "uin"
    .parameter "tag1"
    .parameter "tag2"
    .parameter "name"

    .prologue
    .line 26
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 27
    iput-wide p1, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->uin:J

    .line 28
    iput p3, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->tag1:I

    .line 29
    iput p4, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->tag2:I

    .line 30
    iput-object p5, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->name:Ljava/lang/String;

    .line 31
    return-void
.end method

.method public constructor <init>(JLjava/lang/String;)V
    .locals 0
    .parameter "uin"
    .parameter "name"

    .prologue
    .line 21
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 22
    iput-wide p1, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->uin:J

    .line 23
    iput-object p3, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->name:Ljava/lang/String;

    .line 24
    return-void
.end method

.method private constructor <init>(Landroid/os/Parcel;)V
    .locals 0
    .parameter "in"

    .prologue
    .line 33
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 34
    invoke-virtual {p0, p1}, Loicq/wlogin_sdk/oidb/NameUinInfo;->readFromParcel(Landroid/os/Parcel;)V

    .line 35
    return-void
.end method

.method synthetic constructor <init>(Landroid/os/Parcel;Loicq/wlogin_sdk/oidb/NameUinInfo;)V
    .locals 0
    .parameter
    .parameter

    .prologue
    .line 33
    invoke-direct {p0, p1}, Loicq/wlogin_sdk/oidb/NameUinInfo;-><init>(Landroid/os/Parcel;)V

    return-void
.end method


# virtual methods
.method public describeContents()I
    .locals 1

    .prologue
    .line 40
    const/4 v0, 0x0

    return v0
.end method

.method public readFromParcel(Landroid/os/Parcel;)V
    .locals 2
    .parameter "in"

    .prologue
    .line 53
    invoke-virtual {p1}, Landroid/os/Parcel;->readLong()J

    move-result-wide v0

    iput-wide v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->uin:J

    .line 54
    invoke-virtual {p1}, Landroid/os/Parcel;->readInt()I

    move-result v0

    iput v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->tag1:I

    .line 55
    invoke-virtual {p1}, Landroid/os/Parcel;->readInt()I

    move-result v0

    iput v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->tag2:I

    .line 56
    invoke-virtual {p1}, Landroid/os/Parcel;->readString()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->name:Ljava/lang/String;

    .line 57
    return-void
.end method

.method public writeToParcel(Landroid/os/Parcel;I)V
    .locals 2
    .parameter "dest"
    .parameter "flags"

    .prologue
    .line 46
    iget-wide v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->uin:J

    invoke-virtual {p1, v0, v1}, Landroid/os/Parcel;->writeLong(J)V

    .line 47
    iget v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->tag1:I

    invoke-virtual {p1, v0}, Landroid/os/Parcel;->writeInt(I)V

    .line 48
    iget v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->tag2:I

    invoke-virtual {p1, v0}, Landroid/os/Parcel;->writeInt(I)V

    .line 49
    iget-object v0, p0, Loicq/wlogin_sdk/oidb/NameUinInfo;->name:Ljava/lang/String;

    invoke-virtual {p1, v0}, Landroid/os/Parcel;->writeString(Ljava/lang/String;)V

    .line 50
    return-void
.end method
