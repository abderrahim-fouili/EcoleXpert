package com.google.firebase.messaging;

import android.content.Context;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TopicsSubscriber {
    static final String ERROR_INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    static final String ERROR_SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
    private static final long MAX_DELAY_SEC = TimeUnit.HOURS.toSeconds(8);
    private static final long MIN_DELAY_SEC = 30;
    private static final long RPC_TIMEOUT_SEC = 30;
    private final Context context;
    private final FirebaseMessaging firebaseMessaging;
    private final Metadata metadata;
    private final GmsRpc rpc;
    private final TopicsStore store;
    private final ScheduledExecutorService syncExecutor;
    private final Map<String, ArrayDeque<TaskCompletionSource<Void>>> pendingOperations = new ArrayMap();
    private boolean syncScheduledOrRunning = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Task<TopicsSubscriber> createInstance(final FirebaseMessaging firebaseMessaging, final Metadata metadata, final GmsRpc gmsRpc, final Context context, final ScheduledExecutorService scheduledExecutorService) {
        return Tasks.call(scheduledExecutorService, new Callable() { // from class: com.google.firebase.messaging.TopicsSubscriber$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return TopicsSubscriber.lambda$createInstance$0(context, scheduledExecutorService, firebaseMessaging, metadata, gmsRpc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ TopicsSubscriber lambda$createInstance$0(Context context, ScheduledExecutorService scheduledExecutorService, FirebaseMessaging firebaseMessaging, Metadata metadata, GmsRpc gmsRpc) throws Exception {
        return new TopicsSubscriber(firebaseMessaging, metadata, TopicsStore.getInstance(context, scheduledExecutorService), gmsRpc, context, scheduledExecutorService);
    }

    private TopicsSubscriber(FirebaseMessaging firebaseMessaging, Metadata metadata, TopicsStore topicsStore, GmsRpc gmsRpc, Context context, ScheduledExecutorService scheduledExecutorService) {
        this.firebaseMessaging = firebaseMessaging;
        this.metadata = metadata;
        this.store = topicsStore;
        this.rpc = gmsRpc;
        this.context = context;
        this.syncExecutor = scheduledExecutorService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task<Void> subscribeToTopic(String str) {
        Task<Void> scheduleTopicOperation = scheduleTopicOperation(TopicOperation.subscribe(str));
        startTopicsSyncIfNecessary();
        return scheduleTopicOperation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task<Void> unsubscribeFromTopic(String str) {
        Task<Void> scheduleTopicOperation = scheduleTopicOperation(TopicOperation.unsubscribe(str));
        startTopicsSyncIfNecessary();
        return scheduleTopicOperation;
    }

    Task<Void> scheduleTopicOperation(TopicOperation topicOperation) {
        this.store.addTopicOperation(topicOperation);
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        addToPendingOperations(topicOperation, taskCompletionSource);
        return taskCompletionSource.getTask();
    }

    private void addToPendingOperations(TopicOperation topicOperation, TaskCompletionSource<Void> taskCompletionSource) {
        ArrayDeque<TaskCompletionSource<Void>> arrayDeque;
        synchronized (this.pendingOperations) {
            String serialize = topicOperation.serialize();
            if (this.pendingOperations.containsKey(serialize)) {
                arrayDeque = this.pendingOperations.get(serialize);
            } else {
                ArrayDeque<TaskCompletionSource<Void>> arrayDeque2 = new ArrayDeque<>();
                this.pendingOperations.put(serialize, arrayDeque2);
                arrayDeque = arrayDeque2;
            }
            arrayDeque.add(taskCompletionSource);
        }
    }

    boolean hasPendingOperation() {
        return this.store.getNextTopicOperation() != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startTopicsSyncIfNecessary() {
        if (hasPendingOperation()) {
            startSync();
        }
    }

    private void startSync() {
        if (isSyncScheduledOrRunning()) {
            return;
        }
        syncWithDelaySecondsInternal(0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void syncWithDelaySecondsInternal(long j) {
        scheduleSyncTaskWithDelaySeconds(new TopicsSyncTask(this, this.context, this.metadata, Math.min(Math.max(30L, 2 * j), MAX_DELAY_SEC)), j);
        setSyncScheduledOrRunning(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleSyncTaskWithDelaySeconds(Runnable runnable, long j) {
        this.syncExecutor.schedule(runnable, j, TimeUnit.SECONDS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000d, code lost:
        if (isDebugLogEnabled() == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x000f, code lost:
        android.util.Log.d(com.google.firebase.messaging.Constants.TAG, "topic sync succeeded");
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0017, code lost:
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean syncTopics() throws java.io.IOException {
        /*
            r2 = this;
        L0:
            monitor-enter(r2)
            com.google.firebase.messaging.TopicsStore r0 = r2.store     // Catch: java.lang.Throwable -> L2b
            com.google.firebase.messaging.TopicOperation r0 = r0.getNextTopicOperation()     // Catch: java.lang.Throwable -> L2b
            if (r0 != 0) goto L19
            boolean r0 = isDebugLogEnabled()     // Catch: java.lang.Throwable -> L2b
            if (r0 == 0) goto L16
            java.lang.String r0 = "FirebaseMessaging"
            java.lang.String r1 = "topic sync succeeded"
            android.util.Log.d(r0, r1)     // Catch: java.lang.Throwable -> L2b
        L16:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L2b
            r0 = 1
            return r0
        L19:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L2b
            boolean r1 = r2.performTopicOperation(r0)
            if (r1 != 0) goto L22
            r0 = 0
            return r0
        L22:
            com.google.firebase.messaging.TopicsStore r1 = r2.store
            r1.removeTopicOperation(r0)
            r2.markCompletePendingOperation(r0)
            goto L0
        L2b:
            r0 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L2b
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.TopicsSubscriber.syncTopics():boolean");
    }

    private void markCompletePendingOperation(TopicOperation topicOperation) {
        synchronized (this.pendingOperations) {
            String serialize = topicOperation.serialize();
            if (this.pendingOperations.containsKey(serialize)) {
                ArrayDeque<TaskCompletionSource<Void>> arrayDeque = this.pendingOperations.get(serialize);
                TaskCompletionSource<Void> poll = arrayDeque.poll();
                if (poll != null) {
                    poll.setResult(null);
                }
                if (arrayDeque.isEmpty()) {
                    this.pendingOperations.remove(serialize);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0079 A[Catch: IOException -> 0x009f, TryCatch #0 {IOException -> 0x009f, blocks: (B:3:0x0009, B:18:0x0036, B:20:0x003c, B:21:0x0053, B:23:0x0060, B:24:0x0079, B:26:0x0086, B:8:0x001b, B:11:0x0025), top: B:41:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean performTopicOperation(com.google.firebase.messaging.TopicOperation r10) throws java.io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = "FirebaseMessaging"
            java.lang.String r1 = "Unsubscribe from topic: "
            java.lang.String r2 = "Subscribe to topic: "
            java.lang.String r3 = "Unknown topic operation"
            r4 = 0
            java.lang.String r5 = r10.getOperation()     // Catch: java.io.IOException -> L9f
            int r6 = r5.hashCode()     // Catch: java.io.IOException -> L9f
            r7 = 83
            r8 = 1
            if (r6 == r7) goto L25
            r7 = 85
            if (r6 == r7) goto L1b
            goto L2f
        L1b:
            java.lang.String r6 = "U"
            boolean r5 = r5.equals(r6)     // Catch: java.io.IOException -> L9f
            if (r5 == 0) goto L2f
            r5 = r8
            goto L30
        L25:
            java.lang.String r6 = "S"
            boolean r5 = r5.equals(r6)     // Catch: java.io.IOException -> L9f
            if (r5 == 0) goto L2f
            r5 = r4
            goto L30
        L2f:
            r5 = -1
        L30:
            java.lang.String r6 = " succeeded."
            if (r5 == 0) goto L79
            if (r5 == r8) goto L53
            boolean r1 = isDebugLogEnabled()     // Catch: java.io.IOException -> L9f
            if (r1 == 0) goto L9e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L9f
            r1.<init>(r3)     // Catch: java.io.IOException -> L9f
            java.lang.StringBuilder r10 = r1.append(r10)     // Catch: java.io.IOException -> L9f
            java.lang.String r1 = "."
            java.lang.StringBuilder r10 = r10.append(r1)     // Catch: java.io.IOException -> L9f
            java.lang.String r10 = r10.toString()     // Catch: java.io.IOException -> L9f
            android.util.Log.d(r0, r10)     // Catch: java.io.IOException -> L9f
            goto L9e
        L53:
            java.lang.String r2 = r10.getTopic()     // Catch: java.io.IOException -> L9f
            r9.blockingUnsubscribeFromTopic(r2)     // Catch: java.io.IOException -> L9f
            boolean r2 = isDebugLogEnabled()     // Catch: java.io.IOException -> L9f
            if (r2 == 0) goto L9e
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L9f
            r2.<init>(r1)     // Catch: java.io.IOException -> L9f
            java.lang.String r10 = r10.getTopic()     // Catch: java.io.IOException -> L9f
            java.lang.StringBuilder r10 = r2.append(r10)     // Catch: java.io.IOException -> L9f
            java.lang.StringBuilder r10 = r10.append(r6)     // Catch: java.io.IOException -> L9f
            java.lang.String r10 = r10.toString()     // Catch: java.io.IOException -> L9f
            android.util.Log.d(r0, r10)     // Catch: java.io.IOException -> L9f
            goto L9e
        L79:
            java.lang.String r1 = r10.getTopic()     // Catch: java.io.IOException -> L9f
            r9.blockingSubscribeToTopic(r1)     // Catch: java.io.IOException -> L9f
            boolean r1 = isDebugLogEnabled()     // Catch: java.io.IOException -> L9f
            if (r1 == 0) goto L9e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L9f
            r1.<init>(r2)     // Catch: java.io.IOException -> L9f
            java.lang.String r10 = r10.getTopic()     // Catch: java.io.IOException -> L9f
            java.lang.StringBuilder r10 = r1.append(r10)     // Catch: java.io.IOException -> L9f
            java.lang.StringBuilder r10 = r10.append(r6)     // Catch: java.io.IOException -> L9f
            java.lang.String r10 = r10.toString()     // Catch: java.io.IOException -> L9f
            android.util.Log.d(r0, r10)     // Catch: java.io.IOException -> L9f
        L9e:
            return r8
        L9f:
            r10 = move-exception
            java.lang.String r1 = "SERVICE_NOT_AVAILABLE"
            java.lang.String r2 = r10.getMessage()
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Lc6
            java.lang.String r1 = "INTERNAL_SERVER_ERROR"
            java.lang.String r2 = r10.getMessage()
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto Lb9
            goto Lc6
        Lb9:
            java.lang.String r1 = r10.getMessage()
            if (r1 != 0) goto Lc5
            java.lang.String r10 = "Topic operation failed without exception message. Will retry Topic operation."
            android.util.Log.e(r0, r10)
            return r4
        Lc5:
            throw r10
        Lc6:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Topic operation failed: "
            r1.<init>(r2)
            java.lang.String r10 = r10.getMessage()
            java.lang.StringBuilder r10 = r1.append(r10)
            java.lang.String r1 = ". Will retry Topic operation."
            java.lang.StringBuilder r10 = r10.append(r1)
            java.lang.String r10 = r10.toString()
            android.util.Log.e(r0, r10)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.TopicsSubscriber.performTopicOperation(com.google.firebase.messaging.TopicOperation):boolean");
    }

    private void blockingSubscribeToTopic(String str) throws IOException {
        awaitTask(this.rpc.subscribeToTopic(this.firebaseMessaging.blockingGetToken(), str));
    }

    private void blockingUnsubscribeFromTopic(String str) throws IOException {
        awaitTask(this.rpc.unsubscribeFromTopic(this.firebaseMessaging.blockingGetToken(), str));
    }

    private static <T> void awaitTask(Task<T> task) throws IOException {
        try {
            Tasks.await(task, 30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e = e;
            throw new IOException(ERROR_SERVICE_NOT_AVAILABLE, e);
        } catch (ExecutionException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new IOException(e2);
        } catch (TimeoutException e3) {
            e = e3;
            throw new IOException(ERROR_SERVICE_NOT_AVAILABLE, e);
        }
    }

    synchronized boolean isSyncScheduledOrRunning() {
        return this.syncScheduledOrRunning;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setSyncScheduledOrRunning(boolean z) {
        this.syncScheduledOrRunning = z;
    }

    static boolean isDebugLogEnabled() {
        return Log.isLoggable(Constants.TAG, 3);
    }

    TopicsStore getStore() {
        return this.store;
    }
}
