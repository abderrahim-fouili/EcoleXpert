package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;

/* compiled from: com.google.android.gms:play-services-base@@18.0.1 */
/* loaded from: classes.dex */
interface zai<O> {
    O zaa(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException;
}
