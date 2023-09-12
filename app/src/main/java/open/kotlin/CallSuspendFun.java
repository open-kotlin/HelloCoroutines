package open.kotlin;

import kotlin.coroutines.*;
import kotlinx.coroutines.DelayKt;
import static open.kotlin.CommonKt.log;

// 演示通过Java调用Kotlin suspend函数
public class CallSuspendFun {
    public static void call() throws Exception {
        callGetPromise("World");
        // callGetPromise(null);
        callMyDelay();
        callKotlinxDelay();
    }

    // 不会挂起的挂起函数
    public static void callGetPromise(String world) {
        Object hi = PromiseKt.getPromise(world, new Continuation<String>() {
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
            @Override
            public void resumeWith(Object result) {
                log.d("result = " + result); // 由于getPromise不会挂起，该回调不会被执行
            }
        });
        log.d("hi = " + hi);
    }

    // 会挂起的挂起函数返回值为：
    // CoroutineSingletons.COROUTINE_SUSPENDED
    public static void callMyDelay() throws Exception {
        Object COROUTINE_SUSPENDED = MyDelayKt.myDelay(5000, new Continuation<kotlin.Unit>() {
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
            @Override
            public void resumeWith(Object result) {
                log.d("result = " + result);
            }
        });
        log.d("COROUTINE_SUSPENDED = " + COROUTINE_SUSPENDED);
        Thread.sleep(6000);
    }

    public static void callKotlinxDelay() throws Exception {
        Object COROUTINE_SUSPENDED = DelayKt.delay(5000, new Continuation<kotlin.Unit>() {
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
            @Override
            public void resumeWith(Object result) {
                log.d("result = " + result);
            }
        });
        log.d("COROUTINE_SUSPENDED = " + COROUTINE_SUSPENDED);
        Thread.sleep(6000);
    }
}
