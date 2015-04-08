.class public Loicq/wlogin_sdk/oidb/oidb_base;
.super Ljava/lang/Object;
.source "oidb_base.java"


# instance fields
.field public _body_len:I

.field public _cmd:I

.field public _pkg_len:I

.field public _result:I

.field public _role:I

.field public _trans_pkg_head_ext_len:I

.field public _trans_pkg_head_len:I

.field public _type:I

.field public _uin:J


# direct methods
.method public constructor <init>()V
    .locals 3

    .prologue
    const/4 v2, 0x0

    .line 65
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 55
    iput v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_cmd:I

    .line 56
    const-wide/16 v0, 0x0

    iput-wide v0, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_uin:J

    .line 57
    iput v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_type:I

    .line 58
    iput v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_result:I

    .line 59
    const/16 v0, 0x7f

    iput v0, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    .line 60
    iput v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    .line 61
    iput v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_body_len:I

    .line 62
    iput v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_pkg_len:I

    .line 63
    const/16 v0, 0x7e

    iput v0, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_role:I

    .line 67
    return-void
.end method


# virtual methods
.method public decode_response([B)[B
    .locals 8
    .parameter "rsp_data"

    .prologue
    const/4 v7, 0x0

    const/4 v0, 0x0

    .line 154
    if-eqz p1, :cond_0

    array-length v3, p1

    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    add-int/lit8 v4, v4, 0x4

    if-ge v3, v4, :cond_1

    .line 200
    :cond_0
    :goto_0
    return-object v0

    .line 158
    :cond_1
    new-instance v3, Ljava/lang/StringBuilder;

    const-string v4, "rsp len:"

    invoke-direct {v3, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    array-length v4, p1

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, " data:"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-static {p1}, Loicq/wlogin_sdk/tools/util;->buf_to_string([B)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3}, Loicq/wlogin_sdk/tools/util;->LOGD(Ljava/lang/String;)V

    .line 160
    iget v3, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    add-int/lit8 v3, v3, 0x1

    invoke-static {p1, v3}, Loicq/wlogin_sdk/tools/util;->buf_to_int16([BI)I

    move-result v1

    .line 161
    .local v1, ext_len:I
    array-length v3, p1

    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    add-int/2addr v4, v1

    add-int/lit8 v4, v4, 0x2

    if-lt v3, v4, :cond_0

    .line 165
    array-length v3, p1

    iput v3, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_pkg_len:I

    .line 166
    iput v1, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    .line 168
    invoke-static {p1, v7}, Loicq/wlogin_sdk/tools/util;->buf_to_int8([BI)I

    move-result v3

    const/16 v4, 0xa

    if-ne v3, v4, :cond_0

    .line 169
    array-length v3, p1

    add-int/lit8 v3, v3, -0x1

    invoke-static {p1, v3}, Loicq/wlogin_sdk/tools/util;->buf_to_int8([BI)I

    move-result v3

    const/4 v4, 0x3

    if-ne v3, v4, :cond_0

    .line 173
    const/4 v2, 0x1

    .line 175
    .local v2, pos:I
    invoke-static {p1, v2}, Loicq/wlogin_sdk/tools/util;->buf_to_int16([BI)I

    move-result v3

    array-length v4, p1

    if-ne v3, v4, :cond_0

    .line 179
    add-int/lit8 v2, v2, 0x4

    .line 181
    invoke-static {p1, v2}, Loicq/wlogin_sdk/tools/util;->buf_to_int16([BI)I

    move-result v3

    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_cmd:I

    if-ne v3, v4, :cond_0

    .line 185
    add-int/lit8 v2, v2, 0x2

    .line 187
    invoke-static {p1, v2}, Loicq/wlogin_sdk/tools/util;->buf_to_int32([BI)I

    move-result v3

    int-to-long v3, v3

    iget-wide v5, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_uin:J

    cmp-long v3, v3, v5

    if-nez v3, :cond_0

    .line 191
    add-int/lit8 v2, v2, 0x4

    .line 193
    invoke-static {p1, v2}, Loicq/wlogin_sdk/tools/util;->buf_to_int8([BI)I

    move-result v3

    iput v3, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_result:I

    .line 194
    new-instance v3, Ljava/lang/StringBuilder;

    const-string v4, "result:"

    invoke-direct {v3, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_result:I

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3}, Loicq/wlogin_sdk/tools/util;->LOGD(Ljava/lang/String;)V

    .line 196
    iget v3, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    add-int/lit8 v3, v3, 0x1

    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    add-int v2, v3, v4

    .line 197
    iget v3, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_pkg_len:I

    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    sub-int/2addr v3, v4

    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    sub-int/2addr v3, v4

    add-int/lit8 v3, v3, -0x2

    new-array v0, v3, [B

    .line 198
    .local v0, body:[B
    array-length v3, v0

    invoke-static {p1, v2, v0, v7, v3}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    goto/16 :goto_0
.end method

.method public encode_request(J[B[B)[B
    .locals 7
    .parameter "appid"
    .parameter "body"
    .parameter "key"

    .prologue
    const/4 v6, 0x0

    .line 118
    if-eqz p3, :cond_0

    if-nez p4, :cond_1

    .line 119
    :cond_0
    const/4 v3, 0x0

    .line 150
    :goto_0
    return-object v3

    .line 122
    :cond_1
    array-length v4, p4

    add-int/lit8 v4, v4, 0xf

    iput v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    .line 123
    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    add-int/lit8 v4, v4, 0x2

    iget v5, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    add-int/2addr v4, v5

    array-length v5, p3

    add-int/2addr v4, v5

    iput v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_pkg_len:I

    .line 125
    const/4 v2, 0x0

    .line 126
    .local v2, pos:I
    iget v4, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_pkg_len:I

    new-array v3, v4, [B

    .line 129
    .local v3, req_data:[B
    const/16 v4, 0xa

    invoke-static {v3, v2, v4}, Loicq/wlogin_sdk/tools/util;->int8_to_buf([BII)V

    .line 130
    add-int/lit8 v2, v2, 0x1

    .line 133
    invoke-virtual {p0}, Loicq/wlogin_sdk/oidb/oidb_base;->get_trans_pkg_head()[B

    move-result-object v0

    .line 134
    .local v0, head:[B
    array-length v4, v0

    invoke-static {v0, v6, v3, v2, v4}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 135
    array-length v4, v0

    add-int/lit8 v2, v4, 0x1

    .line 138
    invoke-virtual {p0, p1, p2, p4}, Loicq/wlogin_sdk/oidb/oidb_base;->get_trans_pkg_head_ext(J[B)[B

    move-result-object v1

    .line 139
    .local v1, head_ext:[B
    array-length v4, v1

    invoke-static {v1, v6, v3, v2, v4}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 140
    array-length v4, v1

    add-int/2addr v2, v4

    .line 143
    array-length v4, p3

    invoke-static {p3, v6, v3, v2, v4}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 144
    array-length v4, p3

    add-int/2addr v2, v4

    .line 147
    const/4 v4, 0x3

    invoke-static {v3, v2, v4}, Loicq/wlogin_sdk/tools/util;->int8_to_buf([BII)V

    .line 148
    add-int/lit8 v2, v2, 0x1

    .line 150
    goto :goto_0
.end method

.method public get_cmd()I
    .locals 1

    .prologue
    .line 70
    iget v0, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_cmd:I

    return v0
.end method

.method public get_trans_pkg_head()[B
    .locals 4

    .prologue
    .line 74
    const/4 v1, 0x0

    .line 75
    .local v1, pos:I
    iget v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_len:I

    new-array v0, v2, [B

    .line 77
    .local v0, head:[B
    iget v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_pkg_len:I

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 78
    add-int/lit8 v1, v1, 0x2

    .line 79
    const/4 v2, 0x5

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 80
    add-int/lit8 v1, v1, 0x2

    .line 81
    iget v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_cmd:I

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 82
    add-int/lit8 v1, v1, 0x2

    .line 83
    iget-wide v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_uin:J

    invoke-static {v0, v1, v2, v3}, Loicq/wlogin_sdk/tools/util;->int64_to_buf32([BIJ)V

    .line 84
    add-int/lit8 v1, v1, 0x4

    .line 85
    add-int/lit8 v1, v1, 0x1

    .line 87
    add-int/lit8 v1, v1, 0x2

    .line 88
    const-string v2, "mobile qq"

    invoke-static {v2}, Loicq/wlogin_sdk/tools/util;->string_to_buf(Ljava/lang/String;)[B

    .line 89
    add-int/lit8 v1, v1, 0x32

    .line 90
    iget v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_type:I

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int8_to_buf([BII)V

    .line 91
    add-int/lit8 v1, v1, 0x40

    .line 93
    return-object v0
.end method

.method public get_trans_pkg_head_ext(J[B)[B
    .locals 4
    .parameter "appid"
    .parameter "key"

    .prologue
    .line 97
    const/4 v1, 0x0

    .line 98
    .local v1, pos:I
    iget v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    new-array v0, v2, [B

    .line 100
    .local v0, head_ext:[B
    iget v2, p0, Loicq/wlogin_sdk/oidb/oidb_base;->_trans_pkg_head_ext_len:I

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 101
    add-int/lit8 v1, v1, 0x2

    .line 102
    const/16 v2, 0x258

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 103
    add-int/lit8 v1, v1, 0x2

    .line 104
    invoke-static {v0, v1, p1, p2}, Loicq/wlogin_sdk/tools/util;->int64_to_buf32([BIJ)V

    .line 105
    add-int/lit8 v1, v1, 0x4

    .line 106
    const/16 v2, 0x8

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int8_to_buf([BII)V

    .line 107
    add-int/lit8 v1, v1, 0x1

    .line 108
    array-length v2, p3

    invoke-static {v0, v1, v2}, Loicq/wlogin_sdk/tools/util;->int16_to_buf([BII)V

    .line 109
    add-int/lit8 v1, v1, 0x2

    .line 110
    const/4 v2, 0x0

    array-length v3, p3

    invoke-static {p3, v2, v0, v1, v3}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 111
    array-length v2, p3

    add-int/lit8 v1, v2, 0xb

    .line 112
    add-int/lit8 v1, v1, 0x4

    .line 114
    return-object v0
.end method
