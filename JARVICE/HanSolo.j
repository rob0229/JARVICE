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

.method private static fact(II)I

.var 3 is answer I
.var 1 is e I
.var 2 is f I
.var 4 is fact I


.line 5
	iconst_4
	istore_2
.line 7
	iconst_5
	istore_3
.line 8
L001:
	iload_3
	sipush	300
	if_icmplt	L003
	iconst_0
	goto	L004
L003:
	iconst_1
L004:
	iconst_1
	ixor
	ifne	L002
.line 9
	iload_3
	iload_1
	iload_2
	imul
	bipush	25
	imul
	iadd
	istore_3
	goto	L001
L002:
.line 11
	iload_3
	istore	4
.line 11

	iload	4
	ireturn

.limit locals 5
.limit stack 3
.end method

.method private static main()V

.var 1 is a I
.var 2 is b I


.line 19
	iconst_2
	istore_1
.line 20
	iconst_4
	istore_2
.line 21
	iload_1
	iload_2
	invokestatic	HanSolo/fact(II)I
	istore_2
.line 22
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"%d"
	iconst_1
	anewarray	java/lang/Object
	dup
	iconst_0
	iload_2
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


.line 25
	invokestatic	HanSolo/main()V

	getstatic	HanSolo/_runTimer LRunTimer;
	invokevirtual	RunTimer.printElapsedTime()V

	return

.limit locals 1
.limit stack 3
.end method
