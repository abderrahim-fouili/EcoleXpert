package com.google.firebase.concurrent;

import android.os.Build;
import android.os.StrictMode;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.annotations.concurrent.Blocking;
import com.google.firebase.annotations.concurrent.Lightweight;
import com.google.firebase.annotations.concurrent.UiThread;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Lazy;
import com.google.firebase.components.Qualified;
import com.google.firebase.inject.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes.dex */
public class ExecutorsRegistrar implements ComponentRegistrar {
    static final Lazy<ScheduledExecutorService> BG_EXECUTOR = new Lazy<>(new Provider() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda0
        @Override // com.google.firebase.inject.Provider
        public final Object get() {
            return ExecutorsRegistrar.lambda$static$0();
        }
    });
    static final Lazy<ScheduledExecutorService> LITE_EXECUTOR = new Lazy<>(new Provider() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda1
        @Override // com.google.firebase.inject.Provider
        public final Object get() {
            return ExecutorsRegistrar.lambda$static$1();
        }
    });
    static final Lazy<ScheduledExecutorService> BLOCKING_EXECUTOR = new Lazy<>(new Provider() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda2
        @Override // com.google.firebase.inject.Provider
        public final Object get() {
            return ExecutorsRegistrar.lambda$static$2();
        }
    });
    static final Lazy<ScheduledExecutorService> SCHEDULER = new Lazy<>(new Provider() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda3
        @Override // com.google.firebase.inject.Provider
        public final Object get() {
            return ExecutorsRegistrar.lambda$static$3();
        }
    });

    public static /* synthetic */ ScheduledExecutorService lambda$static$0() {
        return scheduled(Executors.newFixedThreadPool(4, factory("Firebase Background", 10, bgPolicy())));
    }

    public static /* synthetic */ ScheduledExecutorService lambda$static$1() {
        return scheduled(Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()), factory("Firebase Lite", 0, litePolicy())));
    }

    public static /* synthetic */ ScheduledExecutorService lambda$static$2() {
        return scheduled(Executors.newCachedThreadPool(factory("Firebase Blocking", 11)));
    }

    public static /* synthetic */ ScheduledExecutorService lambda$static$3() {
        return Executors.newSingleThreadScheduledExecutor(factory("Firebase Scheduler", 0));
    }

    @Override // com.google.firebase.components.ComponentRegistrar
    public List<Component<?>> getComponents() {
        return Arrays.asList(Component.builder(Qualified.qualified(Background.class, ScheduledExecutorService.class), Qualified.qualified(Background.class, ExecutorService.class), Qualified.qualified(Background.class, Executor.class)).factory(new ComponentFactory() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda4
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return ExecutorsRegistrar.lambda$getComponents$4(componentContainer);
            }
        }).build(), Component.builder(Qualified.qualified(Blocking.class, ScheduledExecutorService.class), Qualified.qualified(Blocking.class, ExecutorService.class), Qualified.qualified(Blocking.class, Executor.class)).factory(new ComponentFactory() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda5
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return ExecutorsRegistrar.lambda$getComponents$5(componentContainer);
            }
        }).build(), Component.builder(Qualified.qualified(Lightweight.class, ScheduledExecutorService.class), Qualified.qualified(Lightweight.class, ExecutorService.class), Qualified.qualified(Lightweight.class, Executor.class)).factory(new ComponentFactory() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda6
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return ExecutorsRegistrar.lambda$getComponents$6(componentContainer);
            }
        }).build(), Component.builder(Qualified.qualified(UiThread.class, Executor.class)).factory(new ComponentFactory() { // from class: com.google.firebase.concurrent.ExecutorsRegistrar$$ExternalSyntheticLambda7
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return ExecutorsRegistrar.lambda$getComponents$7(componentContainer);
            }
        }).build());
    }

    public static /* synthetic */ ScheduledExecutorService lambda$getComponents$4(ComponentContainer componentContainer) {
        return BG_EXECUTOR.get();
    }

    public static /* synthetic */ ScheduledExecutorService lambda$getComponents$5(ComponentContainer componentContainer) {
        return BLOCKING_EXECUTOR.get();
    }

    public static /* synthetic */ ScheduledExecutorService lambda$getComponents$6(ComponentContainer componentContainer) {
        return LITE_EXECUTOR.get();
    }

    public static /* synthetic */ Executor lambda$getComponents$7(ComponentContainer componentContainer) {
        return UiExecutor.INSTANCE;
    }

    public static ScheduledExecutorService scheduled(ExecutorService executorService) {
        return new DelegatingScheduledExecutorService(executorService, SCHEDULER.get());
    }

    private static ThreadFactory factory(String str, int i) {
        return new CustomThreadFactory(str, i, null);
    }

    private static ThreadFactory factory(String str, int i, StrictMode.ThreadPolicy threadPolicy) {
        return new CustomThreadFactory(str, i, threadPolicy);
    }

    private static StrictMode.ThreadPolicy bgPolicy() {
        StrictMode.ThreadPolicy.Builder detectNetwork = new StrictMode.ThreadPolicy.Builder().detectNetwork();
        detectNetwork.detectResourceMismatches();
        if (Build.VERSION.SDK_INT >= 26) {
            detectNetwork.detectUnbufferedIo();
        }
        return detectNetwork.penaltyLog().build();
    }

    private static StrictMode.ThreadPolicy litePolicy() {
        return new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
    }
}
