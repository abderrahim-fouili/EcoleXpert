package com.google.zxing.oned.rss.expanded;

import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import java.util.Objects;

/* loaded from: classes.dex */
final class ExpandedPair {
    private final FinderPattern finderPattern;
    private final DataCharacter leftChar;
    private final DataCharacter rightChar;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ExpandedPair(DataCharacter dataCharacter, DataCharacter dataCharacter2, FinderPattern finderPattern) {
        this.leftChar = dataCharacter;
        this.rightChar = dataCharacter2;
        this.finderPattern = finderPattern;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataCharacter getLeftChar() {
        return this.leftChar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataCharacter getRightChar() {
        return this.rightChar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FinderPattern getFinderPattern() {
        return this.finderPattern;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean mustBeLast() {
        return this.rightChar == null;
    }

    public String toString() {
        StringBuilder append = new StringBuilder("[ ").append(this.leftChar).append(" , ").append(this.rightChar).append(" : ");
        FinderPattern finderPattern = this.finderPattern;
        return append.append(finderPattern == null ? "null" : Integer.valueOf(finderPattern.getValue())).append(" ]").toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ExpandedPair) {
            ExpandedPair expandedPair = (ExpandedPair) obj;
            return Objects.equals(this.leftChar, expandedPair.leftChar) && Objects.equals(this.rightChar, expandedPair.rightChar) && Objects.equals(this.finderPattern, expandedPair.finderPattern);
        }
        return false;
    }

    public int hashCode() {
        return (Objects.hashCode(this.leftChar) ^ Objects.hashCode(this.rightChar)) ^ Objects.hashCode(this.finderPattern);
    }
}
