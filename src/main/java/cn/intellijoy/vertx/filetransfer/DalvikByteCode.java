package cn.intellijoy.vertx.filetransfer;

/**
 * dalvik是基于寄存器的工作方式，通过栈模拟寄存器，取值范围是v0-v65535.
 * 对于一个方法来说，参数总是位于寄存器的末端，对于instance方法，this作为第一个参数，
 * 除去参数之后，前面的寄存器作为local变量的存储地。
 * 
 * 
 * 对于我们需要的常见场景是，const设定参数，中间或许有move，然后是invoke方法。
 * 
 * @author libo
 *
 */
public class DalvikByteCode {
  
}

/**
 * 常见的指令：
 * const/4 vA, #+B
 * if-test vA, vB, +CCCC, 比如：if-eq，携带3个参数，前两个用作比较，后面是跳转
 * if-testz vAA, +BBB，2个参数，测试vAA是否为0
 * arrayop vAA, vBB, vCC
 * iinstanceop vA, vB, field@CCCC
 * sstaticop vAA, field@BBBB
 * unop vA, vB
 * 
 */

//nop
//
//move vA, vB
//
//move/from16 vAA, vBBBB
//
//move/16 vAAAA, vBBBB
//
//move-wide vA, vB
//
//move-wide/from16 vAA, vBBBB
//
//move-wide/16 vAAAA, vBBBB
//
//move-object vA, vB
//
//move-object/from16 vAA, vBBBB
//
//move-object/16 vAAAA, vBBBB
//
//move-result vAA
//move-result-wide vAA
//move-result-object vAA
//move-exception vAA
//return-void
//return vAA
//return-wide vAA
//return-object vAA
//const/4 vA, #+B
//
//const/16 vAA, #+BBBB
//
//const vAA, #+BBBBBBBB
//
//const/high16 vAA, #+BBBB0000
//
//const-wide/16 vAA, #+BBBB
//
//const-wide/32 vAA, #+BBBBBBBB
//
//const-wide vAA, #+BBBBBBBBBBBBBBBB
//
//const-wide/high16 vAA, #+BBBB000000000000
//
//const-string vAA, string@BBBB
//
//const-string/jumbo vAA, string@BBBBBBBB
//
//const-class vAA, type@BBBB
//
//monitor-enter vAA
//monitor-exit vAA
//
//check-cast vAA, type@BBBB
//
//instance-of vA, vB, type@CCCC
//
//
//array-length vA, vB
//
//new-instance vAA, type@BBBB
//
//new-array vA, vB, type@CCCC
//
//
//filled-new-array {vC, vD, vE, vF, vG}, type@BBBB
//
//
//filled-new-array/range {vCCCC .. vNNNN}, type@BBBB
//
//
//
//fill-array-data vAA, +BBBBBBBB (with supplemental data as specified below in "fill-array-data-payloadFormat")
//
//throw vAA
//goto +AA
//
//goto/16 +AAAA
//
//goto/32 +AAAAAAAA
//packed-switch vAA, +BBBBBBBB (with supplemental data as specified below in "packed-switch-payloadFormat")
//
//sparse-switch vAA, +BBBBBBBB (with supplemental data as specified below in "sparse-switch-payloadFormat")
//
//cmpkind vAA, vBB, vCC
//2d: cmpl-float (lt bias)
//2e: cmpg-float (gt bias)
//2f: cmpl-double (lt bias)
//30: cmpg-double (gt bias)
//31: cmp-long
//if-test vA, vB, +CCCC
//32: if-eq
//33: if-ne
//34: if-lt
//35: if-ge
//36: if-gt
//37: if-le
//if-testz vAA, +BBBB
//38: if-eqz
//39: if-nez
//3a: if-ltz
//3b: if-gez
//3c: if-gtz
//3d: if-lez
//(unused)
//arrayop vAA, vBB, vCC
//44: aget
//45: aget-wide
//46: aget-object
//47: aget-boolean
//48: aget-byte
//49: aget-char
//4a: aget-short
//4b: aput
//4c: aput-wide
//4d: aput-object
//4e: aput-boolean
//4f: aput-byte
//50: aput-char
//51: aput-short
//iinstanceop vA, vB, field@CCCC
//52: iget
//53: iget-wide
//54: iget-object
//55: iget-boolean
//56: iget-byte
//57: iget-char
//58: iget-short
//59: iput
//5a: iput-wide
//5b: iput-object
//5c: iput-boolean
//5d: iput-byte
//5e: iput-char
//5f: iput-short
//sstaticop vAA, field@BBBB
//60: sget
//61: sget-wide
//62: sget-object
//63: sget-boolean
//64: sget-byte
//65: sget-char
//66: sget-short
//67: sput
//68: sput-wide
//69: sput-object
//6a: sput-boolean
//6b: sput-byte
//6c: sput-char
//6d: sput-short
//invoke-kind {vC, vD, vE, vF, vG}, meth@BBBB
//6e: invoke-virtual
//6f: invoke-super
//70: invoke-direct
//71: invoke-static
//72: invoke-interface
//
//(unused)
//invoke-kind/range {vCCCC .. vNNNN}, meth@BBBB
//74: invoke-virtual/range
//75: invoke-super/range
//76: invoke-direct/range
//77: invoke-static/range
//78: invoke-interface/range
//(unused)
//unop vA, vB
//7b: neg-int
//7c: not-int
//7d: neg-long
//7e: not-long
//7f: neg-float
//80: neg-double
//81: int-to-long
//82: int-to-float
//83: int-to-double
//84: long-to-int
//85: long-to-float
//86: long-to-double
//87: float-to-int
//88: float-to-long
//89: float-to-double
//8a: double-to-int
//8b: double-to-long
//8c: double-to-float
//8d: int-to-byte
//8e: int-to-char
//8f: int-to-short
//binop vAA, vBB, vCC
//90: add-int
//91: sub-int
//92: mul-int
//93: div-int
//94: rem-int
//95: and-int
//96: or-int
//97: xor-int
//98: shl-int
//99: shr-int
//9a: ushr-int
//9b: add-long
//9c: sub-long
//9d: mul-long
//9e: div-long
//9f: rem-long
//a0: and-long
//a1: or-long
//a2: xor-long
//a3: shl-long
//a4: shr-long
//a5: ushr-long
//a6: add-float
//a7: sub-float
//a8: mul-float
//a9: div-float
//aa: rem-float
//ab: add-double
//ac: sub-double
//ad: mul-double
//ae: div-double
//af: rem-double
//binop/2addr vA, vB
//b0: add-int/2addr
//b1: sub-int/2addr
//b2: mul-int/2addr
//b3: div-int/2addr
//b4: rem-int/2addr
//b5: and-int/2addr
//b6: or-int/2addr
//b7: xor-int/2addr
//b8: shl-int/2addr
//b9: shr-int/2addr
//ba: ushr-int/2addr
//bb: add-long/2addr
//bc: sub-long/2addr
//bd: mul-long/2addr
//be: div-long/2addr
//bf: rem-long/2addr
//c0: and-long/2addr
//c1: or-long/2addr
//c2: xor-long/2addr
//c3: shl-long/2addr
//c4: shr-long/2addr
//c5: ushr-long/2addr
//c6: add-float/2addr
//c7: sub-float/2addr
//c8: mul-float/2addr
//c9: div-float/2addr
//ca: rem-float/2addr
//cb: add-double/2addr
//cc: sub-double/2addr
//cd: mul-double/2addr
//ce: div-double/2addr
//cf: rem-double/2addr
//binop/lit16 vA, vB, #+CCCC
//d0: add-int/lit16
//d1: rsub-int (reverse subtract)
//d2: mul-int/lit16
//d3: div-int/lit16
//d4: rem-int/lit16
//d5: and-int/lit16
//d6: or-int/lit16
//d7: xor-int/lit16
//binop/lit8 vAA, vBB, #+CC
//d8: add-int/lit8
//d9: rsub-int/lit8
//da: mul-int/lit8
//db: div-int/lit8
//dc: rem-int/lit8
//dd: and-int/lit8
//de: or-int/lit8
//df: xor-int/lit8
//e0: shl-int/lit8
//e1: shr-int/lit8
//e2: ushr-int/lit8
