package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.JvmInline;

/* compiled from: TimeSource.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u0000 \u00042\u00020\u0001:\u0002\u0004\u0005J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0006"}, d2 = {"Lkotlin/time/TimeSource;", "", "markNow", "Lkotlin/time/TimeMark;", "Companion", "Monotonic", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* loaded from: classes.dex */
public interface TimeSource {
    public static final Companion Companion = Companion.$$INSTANCE;

    TimeMark markNow();

    /* compiled from: TimeSource.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\tB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0015\u0010\u0003\u001a\u00020\u0004H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016\u0082\u0002\b\n\u0002\b!\n\u0002\b\u0019¨\u0006\n"}, d2 = {"Lkotlin/time/TimeSource$Monotonic;", "Lkotlin/time/TimeSource;", "()V", "markNow", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "markNow-z9LOYto", "()J", "toString", "", "ValueTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Monotonic implements TimeSource {
        public static final Monotonic INSTANCE = new Monotonic();

        private Monotonic() {
        }

        @Override // kotlin.time.TimeSource
        public /* bridge */ /* synthetic */ TimeMark markNow() {
            return ValueTimeMark.m1596boximpl(m1595markNowz9LOYto());
        }

        /* renamed from: markNow-z9LOYto  reason: not valid java name */
        public long m1595markNowz9LOYto() {
            return MonotonicTimeSource.INSTANCE.m1590markNowz9LOYto();
        }

        public String toString() {
            return MonotonicTimeSource.INSTANCE.toString();
        }

        /* compiled from: TimeSource.kt */
        @Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0087@\u0018\u00002\u00020\u0001B\u0018\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\u0015\u0010\u0007\u001a\u00020\bH\u0016ø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\t\u0010\u0006J\u001a\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u0011\u0010\u0012J\u000f\u0010\u0013\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u0014\u0010\u0012J\u0010\u0010\u0015\u001a\u00020\u0016HÖ\u0001¢\u0006\u0004\b\u0017\u0010\u0018J\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\bH\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001cJ\u001b\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\bH\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001cJ\u0010\u0010\u001f\u001a\u00020 HÖ\u0001¢\u0006\u0004\b!\u0010\"R\u0012\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0080\u0004¢\u0006\u0002\n\u0000\u0088\u0001\u0002\u0092\u0001\u00060\u0003j\u0002`\u0004ø\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006#"}, d2 = {"Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "Lkotlin/time/TimeMark;", "reading", "", "Lkotlin/time/ValueTimeMarkReading;", "constructor-impl", "(J)J", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "equals", "", "other", "", "equals-impl", "(JLjava/lang/Object;)Z", "hasNotPassedNow", "hasNotPassedNow-impl", "(J)Z", "hasPassedNow", "hasPassedNow-impl", "hashCode", "", "hashCode-impl", "(J)I", "minus", "duration", "minus-LRDsOJo", "(JJ)J", "plus", "plus-LRDsOJo", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
        @JvmInline
        /* loaded from: classes.dex */
        public static final class ValueTimeMark implements TimeMark {
            private final long reading;

            /* renamed from: box-impl  reason: not valid java name */
            public static final /* synthetic */ ValueTimeMark m1596boximpl(long j) {
                return new ValueTimeMark(j);
            }

            /* renamed from: constructor-impl  reason: not valid java name */
            public static long m1597constructorimpl(long j) {
                return j;
            }

            /* renamed from: equals-impl  reason: not valid java name */
            public static boolean m1599equalsimpl(long j, Object obj) {
                return (obj instanceof ValueTimeMark) && j == ((ValueTimeMark) obj).m1609unboximpl();
            }

            /* renamed from: equals-impl0  reason: not valid java name */
            public static final boolean m1600equalsimpl0(long j, long j2) {
                return j == j2;
            }

            /* renamed from: hashCode-impl  reason: not valid java name */
            public static int m1603hashCodeimpl(long j) {
                return (int) (j ^ (j >>> 32));
            }

            /* renamed from: toString-impl  reason: not valid java name */
            public static String m1606toStringimpl(long j) {
                return "ValueTimeMark(reading=" + j + ')';
            }

            public boolean equals(Object obj) {
                return m1599equalsimpl(this.reading, obj);
            }

            public int hashCode() {
                return m1603hashCodeimpl(this.reading);
            }

            public String toString() {
                return m1606toStringimpl(this.reading);
            }

            /* renamed from: unbox-impl  reason: not valid java name */
            public final /* synthetic */ long m1609unboximpl() {
                return this.reading;
            }

            @Override // kotlin.time.TimeMark
            /* renamed from: minus-LRDsOJo */
            public /* bridge */ /* synthetic */ TimeMark mo1451minusLRDsOJo(long j) {
                return m1596boximpl(m1607minusLRDsOJo(j));
            }

            @Override // kotlin.time.TimeMark
            /* renamed from: plus-LRDsOJo */
            public /* bridge */ /* synthetic */ TimeMark mo1452plusLRDsOJo(long j) {
                return m1596boximpl(m1608plusLRDsOJo(j));
            }

            private /* synthetic */ ValueTimeMark(long j) {
                this.reading = j;
            }

            /* renamed from: elapsedNow-UwyO8pc  reason: not valid java name */
            public static long m1598elapsedNowUwyO8pc(long j) {
                return MonotonicTimeSource.INSTANCE.m1589elapsedFrom6eNON_k(j);
            }

            @Override // kotlin.time.TimeMark
            /* renamed from: elapsedNow-UwyO8pc */
            public long mo1450elapsedNowUwyO8pc() {
                return m1598elapsedNowUwyO8pc(this.reading);
            }

            /* renamed from: plus-LRDsOJo  reason: not valid java name */
            public static long m1605plusLRDsOJo(long j, long j2) {
                return MonotonicTimeSource.INSTANCE.m1588adjustReading6QKq23U(j, j2);
            }

            /* renamed from: plus-LRDsOJo  reason: not valid java name */
            public long m1608plusLRDsOJo(long j) {
                return m1605plusLRDsOJo(this.reading, j);
            }

            /* renamed from: minus-LRDsOJo  reason: not valid java name */
            public static long m1604minusLRDsOJo(long j, long j2) {
                return MonotonicTimeSource.INSTANCE.m1588adjustReading6QKq23U(j, Duration.m1510unaryMinusUwyO8pc(j2));
            }

            /* renamed from: minus-LRDsOJo  reason: not valid java name */
            public long m1607minusLRDsOJo(long j) {
                return m1604minusLRDsOJo(this.reading, j);
            }

            /* renamed from: hasPassedNow-impl  reason: not valid java name */
            public static boolean m1602hasPassedNowimpl(long j) {
                return !Duration.m1491isNegativeimpl(m1598elapsedNowUwyO8pc(j));
            }

            @Override // kotlin.time.TimeMark
            public boolean hasPassedNow() {
                return m1602hasPassedNowimpl(this.reading);
            }

            /* renamed from: hasNotPassedNow-impl  reason: not valid java name */
            public static boolean m1601hasNotPassedNowimpl(long j) {
                return Duration.m1491isNegativeimpl(m1598elapsedNowUwyO8pc(j));
            }

            @Override // kotlin.time.TimeMark
            public boolean hasNotPassedNow() {
                return m1601hasNotPassedNowimpl(this.reading);
            }
        }
    }

    /* compiled from: TimeSource.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lkotlin/time/TimeSource$Companion;", "", "()V", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();

        private Companion() {
        }
    }
}
