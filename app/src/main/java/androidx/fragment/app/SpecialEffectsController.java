package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewCompat;
import androidx.fragment.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class SpecialEffectsController {
    private final ViewGroup mContainer;
    final ArrayList<Operation> mPendingOperations = new ArrayList<>();
    final ArrayList<Operation> mRunningOperations = new ArrayList<>();
    boolean mOperationDirectionIsPop = false;
    boolean mIsContainerPostponed = false;

    abstract void executeOperations(List<Operation> list, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpecialEffectsController getOrCreateController(ViewGroup viewGroup, FragmentManager fragmentManager) {
        return getOrCreateController(viewGroup, fragmentManager.getSpecialEffectsControllerFactory());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpecialEffectsController getOrCreateController(ViewGroup viewGroup, SpecialEffectsControllerFactory specialEffectsControllerFactory) {
        Object tag = viewGroup.getTag(R.id.special_effects_controller_view_tag);
        if (tag instanceof SpecialEffectsController) {
            return (SpecialEffectsController) tag;
        }
        SpecialEffectsController createController = specialEffectsControllerFactory.createController(viewGroup);
        viewGroup.setTag(R.id.special_effects_controller_view_tag, createController);
        return createController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpecialEffectsController(ViewGroup viewGroup) {
        this.mContainer = viewGroup;
    }

    public ViewGroup getContainer() {
        return this.mContainer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Operation.LifecycleImpact getAwaitingCompletionLifecycleImpact(FragmentStateManager fragmentStateManager) {
        Operation findPendingOperation = findPendingOperation(fragmentStateManager.getFragment());
        Operation.LifecycleImpact lifecycleImpact = findPendingOperation != null ? findPendingOperation.getLifecycleImpact() : null;
        Operation findRunningOperation = findRunningOperation(fragmentStateManager.getFragment());
        return (findRunningOperation == null || !(lifecycleImpact == null || lifecycleImpact == Operation.LifecycleImpact.NONE)) ? lifecycleImpact : findRunningOperation.getLifecycleImpact();
    }

    private Operation findPendingOperation(Fragment fragment) {
        Iterator<Operation> it = this.mPendingOperations.iterator();
        while (it.hasNext()) {
            Operation next = it.next();
            if (next.getFragment().equals(fragment) && !next.isCanceled()) {
                return next;
            }
        }
        return null;
    }

    private Operation findRunningOperation(Fragment fragment) {
        Iterator<Operation> it = this.mRunningOperations.iterator();
        while (it.hasNext()) {
            Operation next = it.next();
            if (next.getFragment().equals(fragment) && !next.isCanceled()) {
                return next;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueAdd(Operation.State state, FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing add operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(state, Operation.LifecycleImpact.ADDING, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueShow(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing show operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(Operation.State.VISIBLE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueHide(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing hide operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(Operation.State.GONE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueRemove(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing remove operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(Operation.State.REMOVED, Operation.LifecycleImpact.REMOVING, fragmentStateManager);
    }

    private void enqueue(Operation.State state, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager) {
        synchronized (this.mPendingOperations) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            Operation findPendingOperation = findPendingOperation(fragmentStateManager.getFragment());
            if (findPendingOperation != null) {
                findPendingOperation.mergeWith(state, lifecycleImpact);
                return;
            }
            final FragmentStateManagerOperation fragmentStateManagerOperation = new FragmentStateManagerOperation(state, lifecycleImpact, fragmentStateManager, cancellationSignal);
            this.mPendingOperations.add(fragmentStateManagerOperation);
            fragmentStateManagerOperation.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.SpecialEffectsController.1
                @Override // java.lang.Runnable
                public void run() {
                    if (SpecialEffectsController.this.mPendingOperations.contains(fragmentStateManagerOperation)) {
                        fragmentStateManagerOperation.getFinalState().applyState(fragmentStateManagerOperation.getFragment().mView);
                    }
                }
            });
            fragmentStateManagerOperation.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.SpecialEffectsController.2
                @Override // java.lang.Runnable
                public void run() {
                    SpecialEffectsController.this.mPendingOperations.remove(fragmentStateManagerOperation);
                    SpecialEffectsController.this.mRunningOperations.remove(fragmentStateManagerOperation);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateOperationDirection(boolean z) {
        this.mOperationDirectionIsPop = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markPostponedState() {
        synchronized (this.mPendingOperations) {
            updateFinalState();
            this.mIsContainerPostponed = false;
            int size = this.mPendingOperations.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                Operation operation = this.mPendingOperations.get(size);
                Operation.State from = Operation.State.from(operation.getFragment().mView);
                if (operation.getFinalState() == Operation.State.VISIBLE && from != Operation.State.VISIBLE) {
                    this.mIsContainerPostponed = operation.getFragment().isPostponed();
                    break;
                }
                size--;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forcePostponedExecutePendingOperations() {
        if (this.mIsContainerPostponed) {
            this.mIsContainerPostponed = false;
            executePendingOperations();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void executePendingOperations() {
        if (this.mIsContainerPostponed) {
            return;
        }
        if (!ViewCompat.isAttachedToWindow(this.mContainer)) {
            forceCompleteAllOperations();
            this.mOperationDirectionIsPop = false;
            return;
        }
        synchronized (this.mPendingOperations) {
            if (!this.mPendingOperations.isEmpty()) {
                ArrayList arrayList = new ArrayList(this.mRunningOperations);
                this.mRunningOperations.clear();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Operation operation = (Operation) it.next();
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Cancelling operation " + operation);
                    }
                    operation.cancel();
                    if (!operation.isComplete()) {
                        this.mRunningOperations.add(operation);
                    }
                }
                updateFinalState();
                ArrayList arrayList2 = new ArrayList(this.mPendingOperations);
                this.mPendingOperations.clear();
                this.mRunningOperations.addAll(arrayList2);
                Iterator it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    ((Operation) it2.next()).onStart();
                }
                executeOperations(arrayList2, this.mOperationDirectionIsPop);
                this.mOperationDirectionIsPop = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceCompleteAllOperations() {
        String str;
        String str2;
        boolean isAttachedToWindow = ViewCompat.isAttachedToWindow(this.mContainer);
        synchronized (this.mPendingOperations) {
            updateFinalState();
            Iterator<Operation> it = this.mPendingOperations.iterator();
            while (it.hasNext()) {
                it.next().onStart();
            }
            Iterator it2 = new ArrayList(this.mRunningOperations).iterator();
            while (it2.hasNext()) {
                Operation operation = (Operation) it2.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    StringBuilder append = new StringBuilder().append("SpecialEffectsController: ");
                    if (isAttachedToWindow) {
                        str2 = "";
                    } else {
                        str2 = "Container " + this.mContainer + " is not attached to window. ";
                    }
                    Log.v("FragmentManager", append.append(str2).append("Cancelling running operation ").append(operation).toString());
                }
                operation.cancel();
            }
            Iterator it3 = new ArrayList(this.mPendingOperations).iterator();
            while (it3.hasNext()) {
                Operation operation2 = (Operation) it3.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    StringBuilder append2 = new StringBuilder().append("SpecialEffectsController: ");
                    if (isAttachedToWindow) {
                        str = "";
                    } else {
                        str = "Container " + this.mContainer + " is not attached to window. ";
                    }
                    Log.v("FragmentManager", append2.append(str).append("Cancelling pending operation ").append(operation2).toString());
                }
                operation2.cancel();
            }
        }
    }

    private void updateFinalState() {
        Iterator<Operation> it = this.mPendingOperations.iterator();
        while (it.hasNext()) {
            Operation next = it.next();
            if (next.getLifecycleImpact() == Operation.LifecycleImpact.ADDING) {
                next.mergeWith(Operation.State.from(next.getFragment().requireView().getVisibility()), Operation.LifecycleImpact.NONE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Operation {
        private State mFinalState;
        private final Fragment mFragment;
        private LifecycleImpact mLifecycleImpact;
        private final List<Runnable> mCompletionListeners = new ArrayList();
        private final HashSet<CancellationSignal> mSpecialEffectsSignals = new HashSet<>();
        private boolean mIsCanceled = false;
        private boolean mIsComplete = false;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public enum LifecycleImpact {
            NONE,
            ADDING,
            REMOVING
        }

        void onStart() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public enum State {
            REMOVED,
            VISIBLE,
            GONE,
            INVISIBLE;

            /* JADX INFO: Access modifiers changed from: package-private */
            public static State from(View view) {
                if (view.getAlpha() == 0.0f && view.getVisibility() == 0) {
                    return INVISIBLE;
                }
                return from(view.getVisibility());
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static State from(int i) {
                if (i != 0) {
                    if (i != 4) {
                        if (i == 8) {
                            return GONE;
                        }
                        throw new IllegalArgumentException("Unknown visibility " + i);
                    }
                    return INVISIBLE;
                }
                return VISIBLE;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public void applyState(View view) {
                int i = AnonymousClass3.$SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[ordinal()];
                if (i == 1) {
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    if (viewGroup != null) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Removing view " + view + " from container " + viewGroup);
                        }
                        viewGroup.removeView(view);
                    }
                } else if (i == 2) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to VISIBLE");
                    }
                    view.setVisibility(0);
                } else if (i == 3) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to GONE");
                    }
                    view.setVisibility(8);
                } else if (i != 4) {
                } else {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to INVISIBLE");
                    }
                    view.setVisibility(4);
                }
            }
        }

        Operation(State state, LifecycleImpact lifecycleImpact, Fragment fragment, CancellationSignal cancellationSignal) {
            this.mFinalState = state;
            this.mLifecycleImpact = lifecycleImpact;
            this.mFragment = fragment;
            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: androidx.fragment.app.SpecialEffectsController.Operation.1
                @Override // androidx.core.os.CancellationSignal.OnCancelListener
                public void onCancel() {
                    Operation.this.cancel();
                }
            });
        }

        public State getFinalState() {
            return this.mFinalState;
        }

        LifecycleImpact getLifecycleImpact() {
            return this.mLifecycleImpact;
        }

        public final Fragment getFragment() {
            return this.mFragment;
        }

        final boolean isCanceled() {
            return this.mIsCanceled;
        }

        public String toString() {
            return "Operation {" + Integer.toHexString(System.identityHashCode(this)) + "} {mFinalState = " + this.mFinalState + "} {mLifecycleImpact = " + this.mLifecycleImpact + "} {mFragment = " + this.mFragment + "}";
        }

        final void cancel() {
            if (isCanceled()) {
                return;
            }
            this.mIsCanceled = true;
            if (this.mSpecialEffectsSignals.isEmpty()) {
                complete();
                return;
            }
            Iterator it = new ArrayList(this.mSpecialEffectsSignals).iterator();
            while (it.hasNext()) {
                ((CancellationSignal) it.next()).cancel();
            }
        }

        final void mergeWith(State state, LifecycleImpact lifecycleImpact) {
            int i = AnonymousClass3.$SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$LifecycleImpact[lifecycleImpact.ordinal()];
            if (i == 1) {
                if (this.mFinalState == State.REMOVED) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = " + this.mLifecycleImpact + " to ADDING.");
                    }
                    this.mFinalState = State.VISIBLE;
                    this.mLifecycleImpact = LifecycleImpact.ADDING;
                }
            } else if (i == 2) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = " + this.mFinalState + " -> REMOVED. mLifecycleImpact  = " + this.mLifecycleImpact + " to REMOVING.");
                }
                this.mFinalState = State.REMOVED;
                this.mLifecycleImpact = LifecycleImpact.REMOVING;
            } else if (i == 3 && this.mFinalState != State.REMOVED) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = " + this.mFinalState + " -> " + state + ". ");
                }
                this.mFinalState = state;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void addCompletionListener(Runnable runnable) {
            this.mCompletionListeners.add(runnable);
        }

        public final void markStartedSpecialEffect(CancellationSignal cancellationSignal) {
            onStart();
            this.mSpecialEffectsSignals.add(cancellationSignal);
        }

        public final void completeSpecialEffect(CancellationSignal cancellationSignal) {
            if (this.mSpecialEffectsSignals.remove(cancellationSignal) && this.mSpecialEffectsSignals.isEmpty()) {
                complete();
            }
        }

        final boolean isComplete() {
            return this.mIsComplete;
        }

        public void complete() {
            if (this.mIsComplete) {
                return;
            }
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "SpecialEffectsController: " + this + " has called complete.");
            }
            this.mIsComplete = true;
            for (Runnable runnable : this.mCompletionListeners) {
                runnable.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.fragment.app.SpecialEffectsController$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$LifecycleImpact;
        static final /* synthetic */ int[] $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State;

        static {
            int[] iArr = new int[Operation.LifecycleImpact.values().length];
            $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$LifecycleImpact = iArr;
            try {
                iArr[Operation.LifecycleImpact.ADDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$LifecycleImpact[Operation.LifecycleImpact.REMOVING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$LifecycleImpact[Operation.LifecycleImpact.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[Operation.State.values().length];
            $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State = iArr2;
            try {
                iArr2[Operation.State.REMOVED.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[Operation.State.VISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[Operation.State.GONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[Operation.State.INVISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FragmentStateManagerOperation extends Operation {
        private final FragmentStateManager mFragmentStateManager;

        FragmentStateManagerOperation(Operation.State state, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager, CancellationSignal cancellationSignal) {
            super(state, lifecycleImpact, fragmentStateManager.getFragment(), cancellationSignal);
            this.mFragmentStateManager = fragmentStateManager;
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Operation
        void onStart() {
            if (getLifecycleImpact() == Operation.LifecycleImpact.ADDING) {
                Fragment fragment = this.mFragmentStateManager.getFragment();
                View findFocus = fragment.mView.findFocus();
                if (findFocus != null) {
                    fragment.setFocusedView(findFocus);
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + fragment);
                    }
                }
                View requireView = getFragment().requireView();
                if (requireView.getParent() == null) {
                    this.mFragmentStateManager.addViewToContainer();
                    requireView.setAlpha(0.0f);
                }
                if (requireView.getAlpha() == 0.0f && requireView.getVisibility() == 0) {
                    requireView.setVisibility(4);
                }
                requireView.setAlpha(fragment.getPostOnViewCreatedAlpha());
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Operation
        public void complete() {
            super.complete();
            this.mFragmentStateManager.moveToExpectedState();
        }
    }
}
