package kotlin.collections.unsigned;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* compiled from: _UArraysJvm.kt */
@Metadata(d1 = {"\u0000h\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001c\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u001f\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b!\u0010\"\u001a\u001f\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b#\u0010$\u001a\u001f\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u001f\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b'\u0010(\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b*\u0010+\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b,\u0010-\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b.\u0010/\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b0\u00101\u001a@\u00102\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b7\u00108\u001a@\u00102\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b9\u0010:\u001a@\u00102\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b;\u0010<\u001a@\u00102\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b=\u0010>\u001a4\u0010?\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007ø\u0001\u0000¢\u0006\u0004\bC\u0010D\u001a4\u0010?\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007ø\u0001\u0000¢\u0006\u0004\bE\u0010F\u001a4\u0010?\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007ø\u0001\u0000¢\u0006\u0004\bG\u0010H\u001a4\u0010?\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007ø\u0001\u0000¢\u0006\u0004\bI\u0010J\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\bL\u0010+\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\bM\u0010-\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\bN\u0010/\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\bO\u00101\u001a@\u0010P\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bQ\u00108\u001a@\u0010P\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bR\u0010:\u001a@\u0010P\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bS\u0010<\u001a@\u0010P\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bT\u0010>\u001a4\u0010U\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007ø\u0001\u0000¢\u0006\u0004\bV\u0010D\u001a4\u0010U\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007ø\u0001\u0000¢\u0006\u0004\bW\u0010F\u001a4\u0010U\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007ø\u0001\u0000¢\u0006\u0004\bX\u0010H\u001a4\u0010U\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007ø\u0001\u0000¢\u0006\u0004\bY\u0010J\u001a.\u0010Z\u001a\u00020[*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010]\u001a.\u0010Z\u001a\u00020^*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010`\u001a.\u0010Z\u001a\u00020[*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010a\u001a.\u0010Z\u001a\u00020^*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010b\u001a.\u0010Z\u001a\u00020[*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010c\u001a.\u0010Z\u001a\u00020^*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010d\u001a.\u0010Z\u001a\u00020[*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020[06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010e\u001a.\u0010Z\u001a\u00020^*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020^06H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010f\u0082\u0002\u000b\n\u0002\b\u0019\n\u0005\b\u009920\u0001¨\u0006g"}, d2 = {"asList", "", "Lkotlin/UByte;", "Lkotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Lkotlin/UInt;", "Lkotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Lkotlin/ULong;", "Lkotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Lkotlin/UShort;", "Lkotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", "index", "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "max", "max-GBYM_sE", "([B)Lkotlin/UByte;", "max--ajY-9A", "([I)Lkotlin/UInt;", "max-QwZRm1k", "([J)Lkotlin/ULong;", "max-rL5Bavg", "([S)Lkotlin/UShort;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "maxBy-JOV_ifY", "([BLkotlin/jvm/functions/Function1;)Lkotlin/UByte;", "maxBy-jgv0xPQ", "([ILkotlin/jvm/functions/Function1;)Lkotlin/UInt;", "maxBy-MShoTSo", "([JLkotlin/jvm/functions/Function1;)Lkotlin/ULong;", "maxBy-xTcfx_M", "([SLkotlin/jvm/functions/Function1;)Lkotlin/UShort;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "maxWith-XMRcp5o", "([BLjava/util/Comparator;)Lkotlin/UByte;", "maxWith-YmdZ_VM", "([ILjava/util/Comparator;)Lkotlin/UInt;", "maxWith-zrEWJaI", "([JLjava/util/Comparator;)Lkotlin/ULong;", "maxWith-eOHTfZs", "([SLjava/util/Comparator;)Lkotlin/UShort;", "min", "min-GBYM_sE", "min--ajY-9A", "min-QwZRm1k", "min-rL5Bavg", "minBy", "minBy-JOV_ifY", "minBy-jgv0xPQ", "minBy-MShoTSo", "minBy-xTcfx_M", "minWith", "minWith-XMRcp5o", "minWith-YmdZ_VM", "minWith-zrEWJaI", "minWith-eOHTfZs", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "Ljava/math/BigInteger;", "sumOfBigInteger", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, pn = "kotlin.collections", xi = 49, xs = "kotlin/collections/unsigned/UArraysKt")
/* loaded from: classes.dex */
class UArraysKt___UArraysJvmKt {
    /* renamed from: elementAt-qFRl0hI  reason: not valid java name */
    private static final int m621elementAtqFRl0hI(int[] elementAt, int i) {
        Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return UIntArray.m297getpVg5ArA(elementAt, i);
    }

    /* renamed from: elementAt-r7IrZao  reason: not valid java name */
    private static final long m622elementAtr7IrZao(long[] elementAt, int i) {
        Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return ULongArray.m375getsVKNKU(elementAt, i);
    }

    /* renamed from: elementAt-PpDY95g  reason: not valid java name */
    private static final byte m619elementAtPpDY95g(byte[] elementAt, int i) {
        Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return UByteArray.m219getw2LRezQ(elementAt, i);
    }

    /* renamed from: elementAt-nggk6HY  reason: not valid java name */
    private static final short m620elementAtnggk6HY(short[] elementAt, int i) {
        Intrinsics.checkNotNullParameter(elementAt, "$this$elementAt");
        return UShortArray.m479getMh2AYeg(elementAt, i);
    }

    /* renamed from: asList--ajY-9A  reason: not valid java name */
    public static final List<UInt> m607asListajY9A(int[] asList) {
        Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$1(asList);
    }

    /* renamed from: asList-QwZRm1k  reason: not valid java name */
    public static final List<ULong> m609asListQwZRm1k(long[] asList) {
        Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$2(asList);
    }

    /* renamed from: asList-GBYM_sE  reason: not valid java name */
    public static final List<UByte> m608asListGBYM_sE(byte[] asList) {
        Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$3(asList);
    }

    /* renamed from: asList-rL5Bavg  reason: not valid java name */
    public static final List<UShort> m610asListrL5Bavg(short[] asList) {
        Intrinsics.checkNotNullParameter(asList, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$4(asList);
    }

    /* renamed from: binarySearch-2fe2U9s$default  reason: not valid java name */
    public static /* synthetic */ int m612binarySearch2fe2U9s$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = UIntArray.m298getSizeimpl(iArr);
        }
        return UArraysKt.m611binarySearch2fe2U9s(iArr, i, i2, i3);
    }

    /* renamed from: binarySearch-2fe2U9s  reason: not valid java name */
    public static final int m611binarySearch2fe2U9s(int[] binarySearch, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(i2, i3, UIntArray.m298getSizeimpl(binarySearch));
        int i4 = i3 - 1;
        while (i2 <= i4) {
            int i5 = (i2 + i4) >>> 1;
            int uintCompare = UnsignedKt.uintCompare(binarySearch[i5], i);
            if (uintCompare < 0) {
                i2 = i5 + 1;
            } else if (uintCompare <= 0) {
                return i5;
            } else {
                i4 = i5 - 1;
            }
        }
        return -(i2 + 1);
    }

    /* renamed from: binarySearch-K6DWlUc$default  reason: not valid java name */
    public static /* synthetic */ int m616binarySearchK6DWlUc$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = ULongArray.m376getSizeimpl(jArr);
        }
        return UArraysKt.m615binarySearchK6DWlUc(jArr, j, i, i2);
    }

    /* renamed from: binarySearch-K6DWlUc  reason: not valid java name */
    public static final int m615binarySearchK6DWlUc(long[] binarySearch, long j, int i, int i2) {
        Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(i, i2, ULongArray.m376getSizeimpl(binarySearch));
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            int ulongCompare = UnsignedKt.ulongCompare(binarySearch[i4], j);
            if (ulongCompare < 0) {
                i = i4 + 1;
            } else if (ulongCompare <= 0) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    /* renamed from: binarySearch-WpHrYlw$default  reason: not valid java name */
    public static /* synthetic */ int m618binarySearchWpHrYlw$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UByteArray.m220getSizeimpl(bArr);
        }
        return UArraysKt.m617binarySearchWpHrYlw(bArr, b, i, i2);
    }

    /* renamed from: binarySearch-WpHrYlw  reason: not valid java name */
    public static final int m617binarySearchWpHrYlw(byte[] binarySearch, byte b, int i, int i2) {
        Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(i, i2, UByteArray.m220getSizeimpl(binarySearch));
        int i3 = b & UByte.MAX_VALUE;
        int i4 = i2 - 1;
        while (i <= i4) {
            int i5 = (i + i4) >>> 1;
            int uintCompare = UnsignedKt.uintCompare(binarySearch[i5], i3);
            if (uintCompare < 0) {
                i = i5 + 1;
            } else if (uintCompare <= 0) {
                return i5;
            } else {
                i4 = i5 - 1;
            }
        }
        return -(i + 1);
    }

    /* renamed from: binarySearch-EtDCXyQ$default  reason: not valid java name */
    public static /* synthetic */ int m614binarySearchEtDCXyQ$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UShortArray.m480getSizeimpl(sArr);
        }
        return UArraysKt.m613binarySearchEtDCXyQ(sArr, s, i, i2);
    }

    /* renamed from: binarySearch-EtDCXyQ  reason: not valid java name */
    public static final int m613binarySearchEtDCXyQ(short[] binarySearch, short s, int i, int i2) {
        Intrinsics.checkNotNullParameter(binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(i, i2, UShortArray.m480getSizeimpl(binarySearch));
        int i3 = s & UShort.MAX_VALUE;
        int i4 = i2 - 1;
        while (i <= i4) {
            int i5 = (i + i4) >>> 1;
            int uintCompare = UnsignedKt.uintCompare(binarySearch[i5], i3);
            if (uintCompare < 0) {
                i = i5 + 1;
            } else if (uintCompare <= 0) {
                return i5;
            } else {
                i4 = i5 - 1;
            }
        }
        return -(i + 1);
    }

    @Deprecated(message = "Use maxOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: max--ajY-9A  reason: not valid java name */
    public static final /* synthetic */ UInt m623maxajY9A(int[] max) {
        Intrinsics.checkNotNullParameter(max, "$this$max");
        return UArraysKt.m1003maxOrNullajY9A(max);
    }

    @Deprecated(message = "Use maxOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: max-QwZRm1k  reason: not valid java name */
    public static final /* synthetic */ ULong m625maxQwZRm1k(long[] max) {
        Intrinsics.checkNotNullParameter(max, "$this$max");
        return UArraysKt.m1005maxOrNullQwZRm1k(max);
    }

    @Deprecated(message = "Use maxOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: max-GBYM_sE  reason: not valid java name */
    public static final /* synthetic */ UByte m624maxGBYM_sE(byte[] max) {
        Intrinsics.checkNotNullParameter(max, "$this$max");
        return UArraysKt.m1004maxOrNullGBYM_sE(max);
    }

    @Deprecated(message = "Use maxOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: max-rL5Bavg  reason: not valid java name */
    public static final /* synthetic */ UShort m626maxrL5Bavg(short[] max) {
        Intrinsics.checkNotNullParameter(max, "$this$max");
        return UArraysKt.m1006maxOrNullrL5Bavg(max);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-jgv0xPQ  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UInt m629maxByjgv0xPQ(int[] maxBy, Function1<? super UInt, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UIntArray.m300isEmptyimpl(maxBy)) {
            return null;
        }
        int m297getpVg5ArA = UIntArray.m297getpVg5ArA(maxBy, 0);
        int lastIndex = ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(UInt.m232boximpl(m297getpVg5ArA));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                int m297getpVg5ArA2 = UIntArray.m297getpVg5ArA(maxBy, it.nextInt());
                R invoke2 = selector.invoke(UInt.m232boximpl(m297getpVg5ArA2));
                if (invoke.compareTo(invoke2) < 0) {
                    m297getpVg5ArA = m297getpVg5ArA2;
                    invoke = invoke2;
                }
            }
        }
        return UInt.m232boximpl(m297getpVg5ArA);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-MShoTSo  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> ULong m628maxByMShoTSo(long[] maxBy, Function1<? super ULong, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (ULongArray.m378isEmptyimpl(maxBy)) {
            return null;
        }
        long m375getsVKNKU = ULongArray.m375getsVKNKU(maxBy, 0);
        int lastIndex = ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(ULong.m310boximpl(m375getsVKNKU));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                long m375getsVKNKU2 = ULongArray.m375getsVKNKU(maxBy, it.nextInt());
                R invoke2 = selector.invoke(ULong.m310boximpl(m375getsVKNKU2));
                if (invoke.compareTo(invoke2) < 0) {
                    m375getsVKNKU = m375getsVKNKU2;
                    invoke = invoke2;
                }
            }
        }
        return ULong.m310boximpl(m375getsVKNKU);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-JOV_ifY  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UByte m627maxByJOV_ifY(byte[] maxBy, Function1<? super UByte, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UByteArray.m222isEmptyimpl(maxBy)) {
            return null;
        }
        byte m219getw2LRezQ = UByteArray.m219getw2LRezQ(maxBy, 0);
        int lastIndex = ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(UByte.m156boximpl(m219getw2LRezQ));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                byte m219getw2LRezQ2 = UByteArray.m219getw2LRezQ(maxBy, it.nextInt());
                R invoke2 = selector.invoke(UByte.m156boximpl(m219getw2LRezQ2));
                if (invoke.compareTo(invoke2) < 0) {
                    m219getw2LRezQ = m219getw2LRezQ2;
                    invoke = invoke2;
                }
            }
        }
        return UByte.m156boximpl(m219getw2LRezQ);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-xTcfx_M  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UShort m630maxByxTcfx_M(short[] maxBy, Function1<? super UShort, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(maxBy, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UShortArray.m482isEmptyimpl(maxBy)) {
            return null;
        }
        short m479getMh2AYeg = UShortArray.m479getMh2AYeg(maxBy, 0);
        int lastIndex = ArraysKt.getLastIndex(maxBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(UShort.m416boximpl(m479getMh2AYeg));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                short m479getMh2AYeg2 = UShortArray.m479getMh2AYeg(maxBy, it.nextInt());
                R invoke2 = selector.invoke(UShort.m416boximpl(m479getMh2AYeg2));
                if (invoke.compareTo(invoke2) < 0) {
                    m479getMh2AYeg = m479getMh2AYeg2;
                    invoke = invoke2;
                }
            }
        }
        return UShort.m416boximpl(m479getMh2AYeg);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-YmdZ_VM  reason: not valid java name */
    public static final /* synthetic */ UInt m632maxWithYmdZ_VM(int[] maxWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1012maxWithOrNullYmdZ_VM(maxWith, comparator);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-zrEWJaI  reason: not valid java name */
    public static final /* synthetic */ ULong m634maxWithzrEWJaI(long[] maxWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1014maxWithOrNullzrEWJaI(maxWith, comparator);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-XMRcp5o  reason: not valid java name */
    public static final /* synthetic */ UByte m631maxWithXMRcp5o(byte[] maxWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1011maxWithOrNullXMRcp5o(maxWith, comparator);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-eOHTfZs  reason: not valid java name */
    public static final /* synthetic */ UShort m633maxWitheOHTfZs(short[] maxWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(maxWith, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1013maxWithOrNulleOHTfZs(maxWith, comparator);
    }

    @Deprecated(message = "Use minOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: min--ajY-9A  reason: not valid java name */
    public static final /* synthetic */ UInt m635minajY9A(int[] min) {
        Intrinsics.checkNotNullParameter(min, "$this$min");
        return UArraysKt.m1059minOrNullajY9A(min);
    }

    @Deprecated(message = "Use minOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: min-QwZRm1k  reason: not valid java name */
    public static final /* synthetic */ ULong m637minQwZRm1k(long[] min) {
        Intrinsics.checkNotNullParameter(min, "$this$min");
        return UArraysKt.m1061minOrNullQwZRm1k(min);
    }

    @Deprecated(message = "Use minOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: min-GBYM_sE  reason: not valid java name */
    public static final /* synthetic */ UByte m636minGBYM_sE(byte[] min) {
        Intrinsics.checkNotNullParameter(min, "$this$min");
        return UArraysKt.m1060minOrNullGBYM_sE(min);
    }

    @Deprecated(message = "Use minOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: min-rL5Bavg  reason: not valid java name */
    public static final /* synthetic */ UShort m638minrL5Bavg(short[] min) {
        Intrinsics.checkNotNullParameter(min, "$this$min");
        return UArraysKt.m1062minOrNullrL5Bavg(min);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-jgv0xPQ  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UInt m641minByjgv0xPQ(int[] minBy, Function1<? super UInt, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UIntArray.m300isEmptyimpl(minBy)) {
            return null;
        }
        int m297getpVg5ArA = UIntArray.m297getpVg5ArA(minBy, 0);
        int lastIndex = ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(UInt.m232boximpl(m297getpVg5ArA));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                int m297getpVg5ArA2 = UIntArray.m297getpVg5ArA(minBy, it.nextInt());
                R invoke2 = selector.invoke(UInt.m232boximpl(m297getpVg5ArA2));
                if (invoke.compareTo(invoke2) > 0) {
                    m297getpVg5ArA = m297getpVg5ArA2;
                    invoke = invoke2;
                }
            }
        }
        return UInt.m232boximpl(m297getpVg5ArA);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-MShoTSo  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> ULong m640minByMShoTSo(long[] minBy, Function1<? super ULong, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (ULongArray.m378isEmptyimpl(minBy)) {
            return null;
        }
        long m375getsVKNKU = ULongArray.m375getsVKNKU(minBy, 0);
        int lastIndex = ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(ULong.m310boximpl(m375getsVKNKU));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                long m375getsVKNKU2 = ULongArray.m375getsVKNKU(minBy, it.nextInt());
                R invoke2 = selector.invoke(ULong.m310boximpl(m375getsVKNKU2));
                if (invoke.compareTo(invoke2) > 0) {
                    m375getsVKNKU = m375getsVKNKU2;
                    invoke = invoke2;
                }
            }
        }
        return ULong.m310boximpl(m375getsVKNKU);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-JOV_ifY  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UByte m639minByJOV_ifY(byte[] minBy, Function1<? super UByte, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UByteArray.m222isEmptyimpl(minBy)) {
            return null;
        }
        byte m219getw2LRezQ = UByteArray.m219getw2LRezQ(minBy, 0);
        int lastIndex = ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(UByte.m156boximpl(m219getw2LRezQ));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                byte m219getw2LRezQ2 = UByteArray.m219getw2LRezQ(minBy, it.nextInt());
                R invoke2 = selector.invoke(UByte.m156boximpl(m219getw2LRezQ2));
                if (invoke.compareTo(invoke2) > 0) {
                    m219getw2LRezQ = m219getw2LRezQ2;
                    invoke = invoke2;
                }
            }
        }
        return UByte.m156boximpl(m219getw2LRezQ);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-xTcfx_M  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UShort m642minByxTcfx_M(short[] minBy, Function1<? super UShort, ? extends R> selector) {
        Intrinsics.checkNotNullParameter(minBy, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UShortArray.m482isEmptyimpl(minBy)) {
            return null;
        }
        short m479getMh2AYeg = UShortArray.m479getMh2AYeg(minBy, 0);
        int lastIndex = ArraysKt.getLastIndex(minBy);
        if (lastIndex != 0) {
            R invoke = selector.invoke(UShort.m416boximpl(m479getMh2AYeg));
            ?? it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                short m479getMh2AYeg2 = UShortArray.m479getMh2AYeg(minBy, it.nextInt());
                R invoke2 = selector.invoke(UShort.m416boximpl(m479getMh2AYeg2));
                if (invoke.compareTo(invoke2) > 0) {
                    m479getMh2AYeg = m479getMh2AYeg2;
                    invoke = invoke2;
                }
            }
        }
        return UShort.m416boximpl(m479getMh2AYeg);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-YmdZ_VM  reason: not valid java name */
    public static final /* synthetic */ UInt m644minWithYmdZ_VM(int[] minWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1068minWithOrNullYmdZ_VM(minWith, comparator);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-zrEWJaI  reason: not valid java name */
    public static final /* synthetic */ ULong m646minWithzrEWJaI(long[] minWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1070minWithOrNullzrEWJaI(minWith, comparator);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-XMRcp5o  reason: not valid java name */
    public static final /* synthetic */ UByte m643minWithXMRcp5o(byte[] minWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1067minWithOrNullXMRcp5o(minWith, comparator);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-eOHTfZs  reason: not valid java name */
    public static final /* synthetic */ UShort m645minWitheOHTfZs(short[] minWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter(minWith, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m1069minWithOrNulleOHTfZs(minWith, comparator);
    }

    private static final BigDecimal sumOfBigDecimal(int[] sumOf, Function1<? super UInt, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m298getSizeimpl = UIntArray.m298getSizeimpl(sumOf);
        for (int i = 0; i < m298getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(UInt.m232boximpl(UIntArray.m297getpVg5ArA(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }

    private static final BigDecimal sumOfBigDecimal(long[] sumOf, Function1<? super ULong, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m376getSizeimpl = ULongArray.m376getSizeimpl(sumOf);
        for (int i = 0; i < m376getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(ULong.m310boximpl(ULongArray.m375getsVKNKU(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }

    private static final BigDecimal sumOfBigDecimal(byte[] sumOf, Function1<? super UByte, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m220getSizeimpl = UByteArray.m220getSizeimpl(sumOf);
        for (int i = 0; i < m220getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(UByte.m156boximpl(UByteArray.m219getw2LRezQ(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }

    private static final BigDecimal sumOfBigDecimal(short[] sumOf, Function1<? super UShort, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m480getSizeimpl = UShortArray.m480getSizeimpl(sumOf);
        for (int i = 0; i < m480getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(UShort.m416boximpl(UShortArray.m479getMh2AYeg(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(int[] sumOf, Function1<? super UInt, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m298getSizeimpl = UIntArray.m298getSizeimpl(sumOf);
        for (int i = 0; i < m298getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(UInt.m232boximpl(UIntArray.m297getpVg5ArA(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(long[] sumOf, Function1<? super ULong, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m376getSizeimpl = ULongArray.m376getSizeimpl(sumOf);
        for (int i = 0; i < m376getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(ULong.m310boximpl(ULongArray.m375getsVKNKU(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(byte[] sumOf, Function1<? super UByte, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m220getSizeimpl = UByteArray.m220getSizeimpl(sumOf);
        for (int i = 0; i < m220getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(UByte.m156boximpl(UByteArray.m219getw2LRezQ(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(short[] sumOf, Function1<? super UShort, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter(sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int m480getSizeimpl = UShortArray.m480getSizeimpl(sumOf);
        for (int i = 0; i < m480getSizeimpl; i++) {
            valueOf = valueOf.add(selector.invoke(UShort.m416boximpl(UShortArray.m479getMh2AYeg(sumOf, i))));
            Intrinsics.checkNotNullExpressionValue(valueOf, "this.add(other)");
        }
        return valueOf;
    }
}
