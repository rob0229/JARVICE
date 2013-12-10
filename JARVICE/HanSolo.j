.class public HanSolo
.super java/lang/Object

.field private static _runTimer LRunTimer;
.field private static _standardIn LPascalTextIn;


.method public <init>()V

	aload_0
	invokenonvirtual	java/lang/Object/<init>()V
	return

.limit locals 1
.limit stack 1
.end method

.method private static printindecendingorder(IC)V

.var 0 is a I
.var 1 is space C


.line 14
L001:
	iload_0
	iconst_0
	if_icmpgt	L003
	iconst_0
	goto	L004
L003:
	iconst_1
L004:
	iconst_1
	ixor
	ifne	L002
.line 15
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%d"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_0
	invokestatic	java/lang/Integer.valueOf(I)Ljava/lang/Integer;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 16
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 17
	iload_0
	iconst_1
	isub
	istore_0
	goto	L001
L002:

	return

.limit locals 2
.limit stack 7
.end method

.method private static nestedloops(ICC)V

.var 0 is a I
.var 3 is b I
.var 2 is blank C
.var 1 is star C


.line 35
L005:
	iload_0
	iconst_0
	if_icmpgt	L007
	iconst_0
	goto	L008
L007:
	iconst_1
L008:
	iconst_1
	ixor
	ifne	L006
.line 36
	iload_0
	istore_3
.line 37
L009:
	iload_3
	iconst_0
	if_icmpgt	L011
	iconst_0
	goto	L012
L011:
	iconst_1
L012:
	iconst_1
	ixor
	ifne	L010
.line 38
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 39
	iload_3
	iconst_1
	isub
	istore_3
	goto	L009
L010:
.line 41
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c\n"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_2
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 42
	iload_0
	iconst_1
	isub
	istore_0
	goto	L005
L006:

	return

.limit locals 4
.limit stack 7
.end method

.method private static christmastree(CC)V

.var 2 is a I
.var 1 is blank C
.var 6 is line C
.var 7 is loops I
.var 5 is num I
.var 4 is numspaces I
.var 3 is numstars I
.var 0 is star C


.line 52
	bipush	124
	istore	6
.line 53
	iconst_0
	istore	5
.line 54
	iconst_5
	istore_2
.line 55
L013:
	iload_2
	iconst_0
	if_icmpge	L015
	iconst_0
	goto	L016
L015:
	iconst_1
L016:
	iconst_1
	ixor
	ifne	L014
.line 56
	iload_2
	istore	4
.line 57
	bipush	6
	istore_3
.line 58
L017:
	iload	4
	iconst_0
	if_icmpge	L019
	iconst_0
	goto	L020
L019:
	iconst_1
L020:
	iconst_1
	ixor
	ifne	L018
.line 59
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 60
	iload	4
	iconst_1
	isub
	istore	4
	goto	L017
L018:
.line 62
	iload_3
	iload_2
	isub
	istore_3
.line 64
L021:
	iload_3
	iconst_0
	if_icmpgt	L023
	iconst_0
	goto	L024
L023:
	iconst_1
L024:
	iconst_1
	ixor
	ifne	L022
.line 65
L025:
	iload	5
	iconst_2
	if_icmplt	L027
	iconst_0
	goto	L028
L027:
	iconst_1
L028:
	iconst_1
	ixor
	ifne	L026
.line 66
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_0
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 68
	iload	5
	iconst_1
	iadd
	istore	5
	goto	L025
L026:
.line 70
	iconst_0
	istore	5
.line 71
	iload_3
	iconst_1
	isub
	istore_3
	goto	L021
L022:
.line 73
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c\n"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 74
	iload_2
	iconst_1
	isub
	istore_2
	goto	L013
L014:
.line 77
	iconst_2
	istore	7
.line 78
L029:
	iload	7
	iconst_0
	if_icmpgt	L031
	iconst_0
	goto	L032
L031:
	iconst_1
L032:
	iconst_1
	ixor
	ifne	L030
.line 79
	iconst_5
	istore_2
.line 80
L033:
	iload_2
	iconst_1
	if_icmpge	L035
	iconst_0
	goto	L036
L035:
	iconst_1
L036:
	iconst_1
	ixor
	ifne	L034
.line 81
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 83
	iload_2
	iconst_1
	isub
	istore_2
	goto	L033
L034:
.line 85
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload	6
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 86
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 87
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 88
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c\n"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload	6
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 89
	iload	7
	iconst_1
	isub
	istore	7
	goto	L029
L030:
.line 92
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c\n"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_1
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V

	return

.limit locals 8
.limit stack 7
.end method

.method private static fact(I)I

.var 1 is answer I
.var 0 is num I
.var 2 is fact I


.line 99
	iload_0
	istore_1
.line 100
L037:
	iload_0
	iconst_1
	if_icmpgt	L039
	iconst_0
	goto	L040
L039:
	iconst_1
L040:
	iconst_1
	ixor
	ifne	L038
.line 101
	iload_0
	iconst_1
	isub
	istore_0
.line 102
	iload_1
	iload_0
	imul
	istore_1
	goto	L037
L038:
.line 105
	iload_1
	istore_2

	iload_2
	ireturn

.limit locals 3
.limit stack 2
.end method

.method private static main()V

.var 2 is blank C
.var 0 is num I
.var 1 is star C


.line 109
	iconst_5
	istore_0
.line 111
	bipush	42
	istore_1
.line 113
	bipush	32
	istore_2
.line 114
	iload_0
	iload_1
	iload_2
	invokestatic	HanSolo/nestedloops(ICC)V
.line 115
	iload_1
	iload_2
	invokestatic	HanSolo/christmastree(CC)V
.line 116
	iload_0
	iload_2
	invokestatic	HanSolo/printindecendingorder(IC)V
.line 117
	iload_0
	invokestatic	HanSolo/fact(I)I
	istore_0
.line 118
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%c\n"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_2
	invokestatic	java/lang/Character.valueOf(C)Ljava/lang/Character;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V
.line 119
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%d"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_0
	invokestatic	java/lang/Integer.valueOf(I)Ljava/lang/Integer;
	aastore
	invokestatic	java/lang/String/format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V

	return

.limit locals 3
.limit stack 7
.end method

.method public static main([Ljava/lang/String;)V

	new	RunTimer
	dup
	invokenonvirtual	RunTimer/<init>()V
	putstatic	HanSolo/_runTimer LRunTimer;
	new	PascalTextIn
	dup
	invokenonvirtual	PascalTextIn/<init>()V
	putstatic	HanSolo/_standardIn LPascalTextIn;


.line 107
	invokestatic	HanSolo/main()V

	getstatic	HanSolo/_runTimer LRunTimer;
	invokevirtual	RunTimer.printElapsedTime()V

	return

.limit locals 1
.limit stack 3
.end method
