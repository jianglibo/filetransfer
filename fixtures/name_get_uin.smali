.class public Loicq/wlogin_sdk/oidb/name_get_uin;
.super Loicq/wlogin_sdk/oidb/oidb_base;
.source "name_get_uin.java"


# instance fields
.field public nameinfo:Ljava/util/List;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/List",
            "<",
            "Loicq/wlogin_sdk/oidb/NameUinInfo;",
            ">;"
        }
    .end annotation
.end field


# direct methods
.method public constructor <init>(J)V
    .locals 1
    .parameter "uin"

    .prologue
    .line 15
    invoke-direct {p0}, Loicq/wlogin_sdk/oidb/oidb_base;-><init>()V

    .line 13
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->nameinfo:Ljava/util/List;

    .line 17
    iput-wide p1, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_uin:J

    .line 18
    const/4 v0, 0x1

    iput v0, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_type:I

    .line 19
    const/16 v0, 0x4af

    iput v0, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_cmd:I

    .line 20
    return-void
.end method


# virtual methods
.method public get_request(J[B[[B)[B
    .locals 6
    .parameter "appid"
    .parameter "key"
    .parameter "name_list"

    .prologue
    const/4 v5, -0x1

    .line 33
    if-eqz p3, :cond_0

    if-nez p4, :cond_1

    .line 34
    :cond_0
    const/4 v3, 0x0

    .line 60
    :goto_0
    return-object v3

    .line 37
    :cond_1
    const/4 v3, 0x6

    iput v3, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_body_len:I

    .line 38
    const/4 v1, 0x0

    .local v1, i:I
    :goto_1
    array-length v3, p4

    if-lt v1, v3, :cond_2

    .line 42
    const/4 v2, 0x0

    .line 43
    .local v2, pos:I
    iget v3, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_body_len:I

    new-array v0, v3, [B

    .line 45
    .local v0, body:[B
    const/16 v3, 0x4f

    invoke-static {v0, v2, v3}, Loicq/wlogin_sdk/tools/util;->int8_to_buf([BII)V

    .line 46
    add-int/lit8 v2, v2, 0x1

    .line 47
    invoke-static {v0, v2, v5}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 48
    add-int/lit8 v2, v2, 0x2

    .line 49
    invoke-static {v0, v2, v5}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 50
    add-int/lit8 v2, v2, 0x2

    .line 51
    array-length v3, p4

    invoke-static {v0, v2, v3}, Loicq/wlogin_sdk/tools/util;->int8_to_buf([BII)V

    .line 52
    add-int/lit8 v2, v2, 0x1

    .line 53
    const/4 v1, 0x0

    :goto_2
    array-length v3, p4

    if-lt v1, v3, :cond_3

    .line 60
    invoke-virtual {p0, p1, p2, v0, p3}, Loicq/wlogin_sdk/oidb/name_get_uin;->encode_request(J[B[B)[B

    move-result-object v3

    goto :goto_0

    .line 39
    .end local v0           #body:[B
    .end local v2           #pos:I
    :cond_2
    iget v3, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_body_len:I

    aget-object v4, p4, v1

    array-length v4, v4

    add-int/lit8 v4, v4, 0x1

    add-int/2addr v3, v4

    iput v3, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_body_len:I

    .line 38
    add-int/lit8 v1, v1, 0x1

    goto :goto_1

    .line 54
    .restart local v0       #body:[B
    .restart local v2       #pos:I
    :cond_3
    aget-object v3, p4, v1

    array-length v3, v3

    invoke-static {v0, v2, v3}, Loicq/wlogin_sdk/tools/util;->int8_to_buf([BII)V

    .line 55
    add-int/lit8 v2, v2, 0x1

    .line 56
    aget-object v3, p4, v1

    const/4 v4, 0x0

    aget-object v5, p4, v1

    array-length v5, v5

    invoke-static {v3, v4, v0, v2, v5}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 57
    aget-object v3, p4, v1

    array-length v3, v3

    add-int/2addr v2, v3

    .line 53
    add-int/lit8 v1, v1, 0x1

    goto :goto_2
.end method

.method public get_response([B)I
    .locals 14
    .parameter "rsp_data"

    .prologue
    .line 76
    invoke-virtual {p0, p1}, Loicq/wlogin_sdk/oidb/name_get_uin;->decode_response([B)[B

    move-result-object v6

    .line 77
    .local v6, body:[B
    if-nez v6, :cond_0

    .line 78
    const/16 v0, -0x3f1

    .line 123
    :goto_0
    return v0

    .line 81
    :cond_0
    iget v0, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_result:I

    if-eqz v0, :cond_1

    .line 82
    iget v0, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->_result:I

    goto :goto_0

    .line 85
    :cond_1
    const/4 v12, 0x0

    .line 86
    .local v12, pos:I
    array-length v7, v6

    .line 88
    .local v7, body_len:I
    const/4 v0, 0x3

    if-le v0, v7, :cond_2

    const/16 v0, -0x3f1

    goto :goto_0

    .line 91
    :cond_2
    invoke-static {v6, v12}, Loicq/wlogin_sdk/tools/util;->buf_to_int8([BI)I

    move-result v0

    const/16 v5, 0x4f

    if-eq v0, v5, :cond_3

    const/16 v0, -0x3f1

    goto :goto_0

    .line 92
    :cond_3
    add-int/lit8 v12, v12, 0x1

    .line 95
    invoke-static {v6, v12}, Loicq/wlogin_sdk/tools/util;->buf_to_int8([BI)I

    .line 96
    add-int/lit8 v12, v12, 0x1

    .line 98
    invoke-static {v6, v12}, Loicq/wlogin_sdk/tools/util;->buf_to_int8([BI)I

    move-result v8

    .line 99
    .local v8, cCount:I
    add-int/lit8 v12, v12, 0x1

    .line 101
    iget-object v0, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->nameinfo:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->clear()V

    .line 102
    const/4 v9, 0x0

    .local v9, i:I
    :goto_1
    if-lt v9, v8, :cond_4

    .line 123
    const/4 v0, 0x0

    goto :goto_0

    .line 103
    :cond_4
    add-int/lit8 v0, v12, 0xd

    if-le v0, v7, :cond_5

    const/16 v0, -0x3f1

    goto :goto_0

    .line 105
    :cond_5
    invoke-static {v6, v12}, Loicq/wlogin_sdk/tools/util;->buf_to_int64([BI)J

    move-result-wide v1

    .line 106
    .local v1, uin:J
    add-int/lit8 v12, v12, 0x8

    .line 107
    invoke-static {v6, v12}, Loicq/wlogin_sdk/tools/util;->buf_to_int16([BI)I

    move-result v3

    .line 108
    .local v3, tag1:I
    add-int/lit8 v12, v12, 0x2

    .line 109
    invoke-static {v6, v12}, Loicq/wlogin_sdk/tools/util;->buf_to_int16([BI)I

    move-result v4

    .line 110
    .local v4, tag2:I
    add-int/lit8 v12, v12, 0x2

    .line 111
    invoke-static {v6, v12}, Loicq/wlogin_sdk/tools/util;->buf_to_int8([BI)I

    move-result v11

    .line 112
    .local v11, name_len:I
    add-int/lit8 v12, v12, 0x1

    .line 114
    add-int v0, v12, v11

    if-le v0, v7, :cond_6

    const/16 v0, -0x3f1

    goto :goto_0

    .line 116
    :cond_6
    new-array v10, v11, [B

    .line 117
    .local v10, name:[B
    const/4 v0, 0x0

    invoke-static {v6, v12, v10, v0, v11}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 118
    add-int/2addr v12, v11

    .line 120
    iget-object v13, p0, Loicq/wlogin_sdk/oidb/name_get_uin;->nameinfo:Ljava/util/List;

    new-instance v0, Loicq/wlogin_sdk/oidb/NameUinInfo;

    new-instance v5, Ljava/lang/String;

    invoke-direct {v5, v10}, Ljava/lang/String;-><init>([B)V

    invoke-direct/range {v0 .. v5}, Loicq/wlogin_sdk/oidb/NameUinInfo;-><init>(JIILjava/lang/String;)V

    invoke-interface {v13, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    .line 102
    add-int/lit8 v9, v9, 0x1

    goto :goto_1
.end method
