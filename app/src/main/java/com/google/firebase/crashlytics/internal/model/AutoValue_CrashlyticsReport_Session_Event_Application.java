package com.google.firebase.crashlytics.internal.model;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;

/* loaded from: classes.dex */
final class AutoValue_CrashlyticsReport_Session_Event_Application extends CrashlyticsReport.Session.Event.Application {
    private final Boolean background;
    private final ImmutableList<CrashlyticsReport.CustomAttribute> customAttributes;
    private final CrashlyticsReport.Session.Event.Application.Execution execution;
    private final ImmutableList<CrashlyticsReport.CustomAttribute> internalKeys;
    private final int uiOrientation;

    private AutoValue_CrashlyticsReport_Session_Event_Application(CrashlyticsReport.Session.Event.Application.Execution execution, ImmutableList<CrashlyticsReport.CustomAttribute> immutableList, ImmutableList<CrashlyticsReport.CustomAttribute> immutableList2, Boolean bool, int i) {
        this.execution = execution;
        this.customAttributes = immutableList;
        this.internalKeys = immutableList2;
        this.background = bool;
        this.uiOrientation = i;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public CrashlyticsReport.Session.Event.Application.Execution getExecution() {
        return this.execution;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public ImmutableList<CrashlyticsReport.CustomAttribute> getCustomAttributes() {
        return this.customAttributes;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public ImmutableList<CrashlyticsReport.CustomAttribute> getInternalKeys() {
        return this.internalKeys;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public Boolean getBackground() {
        return this.background;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public int getUiOrientation() {
        return this.uiOrientation;
    }

    public String toString() {
        return "Application{execution=" + this.execution + ", customAttributes=" + this.customAttributes + ", internalKeys=" + this.internalKeys + ", background=" + this.background + ", uiOrientation=" + this.uiOrientation + "}";
    }

    public boolean equals(Object obj) {
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList;
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList2;
        Boolean bool;
        if (obj == this) {
            return true;
        }
        if (obj instanceof CrashlyticsReport.Session.Event.Application) {
            CrashlyticsReport.Session.Event.Application application = (CrashlyticsReport.Session.Event.Application) obj;
            return this.execution.equals(application.getExecution()) && ((immutableList = this.customAttributes) != null ? immutableList.equals(application.getCustomAttributes()) : application.getCustomAttributes() == null) && ((immutableList2 = this.internalKeys) != null ? immutableList2.equals(application.getInternalKeys()) : application.getInternalKeys() == null) && ((bool = this.background) != null ? bool.equals(application.getBackground()) : application.getBackground() == null) && this.uiOrientation == application.getUiOrientation();
        }
        return false;
    }

    public int hashCode() {
        int hashCode = (this.execution.hashCode() ^ 1000003) * 1000003;
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList = this.customAttributes;
        int hashCode2 = (hashCode ^ (immutableList == null ? 0 : immutableList.hashCode())) * 1000003;
        ImmutableList<CrashlyticsReport.CustomAttribute> immutableList2 = this.internalKeys;
        int hashCode3 = (hashCode2 ^ (immutableList2 == null ? 0 : immutableList2.hashCode())) * 1000003;
        Boolean bool = this.background;
        return ((hashCode3 ^ (bool != null ? bool.hashCode() : 0)) * 1000003) ^ this.uiOrientation;
    }

    @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application
    public CrashlyticsReport.Session.Event.Application.Builder toBuilder() {
        return new Builder(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Builder extends CrashlyticsReport.Session.Event.Application.Builder {
        private Boolean background;
        private ImmutableList<CrashlyticsReport.CustomAttribute> customAttributes;
        private CrashlyticsReport.Session.Event.Application.Execution execution;
        private ImmutableList<CrashlyticsReport.CustomAttribute> internalKeys;
        private Integer uiOrientation;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder() {
        }

        private Builder(CrashlyticsReport.Session.Event.Application application) {
            this.execution = application.getExecution();
            this.customAttributes = application.getCustomAttributes();
            this.internalKeys = application.getInternalKeys();
            this.background = application.getBackground();
            this.uiOrientation = Integer.valueOf(application.getUiOrientation());
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setExecution(CrashlyticsReport.Session.Event.Application.Execution execution) {
            if (execution == null) {
                throw new NullPointerException("Null execution");
            }
            this.execution = execution;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setCustomAttributes(ImmutableList<CrashlyticsReport.CustomAttribute> immutableList) {
            this.customAttributes = immutableList;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setInternalKeys(ImmutableList<CrashlyticsReport.CustomAttribute> immutableList) {
            this.internalKeys = immutableList;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setBackground(Boolean bool) {
            this.background = bool;
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application.Builder setUiOrientation(int i) {
            this.uiOrientation = Integer.valueOf(i);
            return this;
        }

        @Override // com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Builder
        public CrashlyticsReport.Session.Event.Application build() {
            String str;
            if (this.execution != null) {
                str = "";
            } else {
                str = " execution";
            }
            if (this.uiOrientation == null) {
                str = str + " uiOrientation";
            }
            if (!str.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + str);
            }
            return new AutoValue_CrashlyticsReport_Session_Event_Application(this.execution, this.customAttributes, this.internalKeys, this.background, this.uiOrientation.intValue());
        }
    }
}
