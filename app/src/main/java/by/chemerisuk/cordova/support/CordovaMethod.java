package by.chemerisuk.cordova.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes.dex */
public @interface CordovaMethod {
    String action() default "";

    ExecutionThread value() default ExecutionThread.MAIN;
}
