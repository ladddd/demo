package com.jincaizi.kuaiwin.utils;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Originally from RoboGuice:
 * https://github.com/roboguice/roboguice/blob/master/roboguice/src/main/java
 * /roboguice/util/SafeAsyncTask.java
 * A class similar but unrelated to android's {@link android.os.AsyncTask}.
 * Unlike AsyncTask, this class properly propagates exceptions.
 * If you're familiar with AsyncTask and are looking for
 * {@link android.os.AsyncTask#doInBackground(Object[])}, we've named it {@link #call()} here to
 * conform with java 1.5's {@link java.util.concurrent.Callable} interface.
 * Current limitations: does not yet handle progress, although it shouldn't be hard to add.
 * If using your own executor, you must call future() to get a runnable you can execute.
 * 
 * @param <T>
 */
public abstract class SafeAsyncTask<T> implements Callable<T> {
    private static final String TAG = "SafeAsyncTask";
    public static final int DEFAULT_POOL_SIZE = 25;
    protected static final Executor DEFAULT_EXECUTOR = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);

    protected Handler handler;
    protected Executor executor;
    protected StackTraceElement[] launchLocation;
    protected FutureTask<Void> future;

    /**
     * Sets executor to Executors.newFixedThreadPool(DEFAULT_POOL_SIZE) and Handler to new Handler()
     */
    public SafeAsyncTask() {
        this.executor = DEFAULT_EXECUTOR;
    }

    /**
     * Sets executor to Executors.newFixedThreadPool(DEFAULT_POOL_SIZE)
     */
    public SafeAsyncTask(Handler handler) {
        this.handler = handler;
        this.executor = DEFAULT_EXECUTOR;
    }

    /**
     * Sets Handler to new Handler()
     */
    public SafeAsyncTask(Executor executor) {
        this.executor = executor;
    }

    public SafeAsyncTask(Handler handler, Executor executor) {
        this.handler = handler;
        this.executor = executor;
    }

    public FutureTask<Void> future() {
        future = new FutureTask<Void>(newTask());
        return future;
    }

    public SafeAsyncTask<T> executor(Executor aexecutor) {
        this.executor = aexecutor;
        return this;
    }

    public Executor executor() {
        return executor;
    }

    public SafeAsyncTask<T> handler(Handler ahandler) {
        this.handler = ahandler;
        return this;
    }

    public Handler handler() {
        return handler;
    }

    public void execute() {
        execute(Thread.currentThread().getStackTrace());
    }

    protected void execute(StackTraceElement[] alaunchLocation) {
        this.launchLocation = alaunchLocation;
        executor.execute(future());
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        if (future == null) {
            throw new UnsupportedOperationException("You cannot cancel this task before calling future()");
        }
        return future.cancel(mayInterruptIfRunning);
    }

    /**
     * @throws Exception
     *             , captured on passed to onException() if present.
     */
    protected void onPreExecute() throws Exception {
    }

    /**
     * @param t
     *            the result of {@link #call()}
     * @throws Exception
     *             , captured on passed to onException() if present.
     */
    protected void onSuccess(T t) throws Exception {
    }

    /**
     * Called when the thread has been interrupted, likely because the task was canceled.
     * By default, calls {@link #onException(Exception)}, but this method may be overridden to
     * handle interruptions differently than other exceptions.
     * 
     * @param e
     *            an InterruptedException or InterruptedIOException
     */
    protected void onInterrupted(Exception e) {
        onException(e);
    }

    /**
     * Logs the exception as an Error by default, but this method may be overridden by subclasses.
     * 
     * @param e
     *            the exception thrown from {@link #onPreExecute()}, {@link #call()}, or
     *            {@link #onSuccess(Object)}
     * @throws RuntimeException
     *             , ignored
     */
    protected void onException(Exception e) {
        onThrowable(e);
    }

    protected void onThrowable(Throwable t) {
        Log.e("roboguice", "Throwable caught during background processing", t);
    }

    /**
     * @throws RuntimeException
     *             , ignored
     */
    protected void onFinally() {
    }

    protected Task<T> newTask() {
        return new Task<T>(this);
    }

    /**
     * Task
     * 
     * @author kongnan
     * @param <T>
     */
    public static class Task<T> implements Callable<Void> {
        protected SafeAsyncTask<T> parent;
        protected Handler handler;

        public Task(SafeAsyncTask<T> parent) {
            this.parent = parent;
            this.handler = parent.handler != null ? parent.handler : new Handler(Looper.getMainLooper());
        }

        public Void call() throws Exception {
            String tf = "Task failed.";
            try {
                doPreExecute();
                doSuccess(doCall());

            } catch (final Exception e) {
                e.printStackTrace(); //FIXME add by @Daniex --print some unhandled exception 
                try {
                    doException(e);
                } catch (Exception f) {
                    // logged but ignored
                    // Ln.e(f);
                    Log.e(TAG, tf, f.getCause());
                }
            } catch (final Throwable t) {
                t.printStackTrace(); //FIXME add by @Daniex --print some unhandled exception
                try {
                    doThrowable(t);
                } catch (Exception f) {
                    // logged but ignored
                    // Ln.e(f);
                    Log.e(TAG, tf, f.getCause());
                }
            } finally {
                doFinally();
            }

            return null;
        }

        protected void doPreExecute() throws Exception {
            postToUiThreadAndWait(new Callable<Object>() {
                public Object call() throws Exception {
                    parent.onPreExecute();
                    return null;
                }
            });
        }

        protected T doCall() throws Exception {
            return parent.call();
        }

        protected void doSuccess(final T r) throws Exception {
            postToUiThreadAndWait(new Callable<Object>() {
                public Object call() throws Exception {
                    parent.onSuccess(r);
                    return null;
                }
            });
        }

        protected void doException(final Exception e) throws Exception {
            if (parent.launchLocation != null) {
                final ArrayList<StackTraceElement> stack = new ArrayList<StackTraceElement>(Arrays.asList(e
                        .getStackTrace()));
                stack.addAll(Arrays.asList(parent.launchLocation));
                e.setStackTrace(stack.toArray(new StackTraceElement[stack.size()]));
            }
            postToUiThreadAndWait(new Callable<Object>() {
                public Object call() throws Exception {
                    if (e instanceof InterruptedException || e instanceof InterruptedIOException) {
                        parent.onInterrupted(e);
                    } else {
                        parent.onException(e);
                    }
                    return null;
                }
            });
        }

        protected void doThrowable(final Throwable e) throws Exception {
            if (parent.launchLocation != null) {
                final ArrayList<StackTraceElement> stack = new ArrayList<StackTraceElement>(Arrays.asList(e
                        .getStackTrace()));
                stack.addAll(Arrays.asList(parent.launchLocation));
                e.setStackTrace(stack.toArray(new StackTraceElement[stack.size()]));
            }
            postToUiThreadAndWait(new Callable<Object>() {
                public Object call() throws Exception {
                    parent.onThrowable(e);
                    return null;
                }
            });
        }

        protected void doFinally() throws Exception {
            postToUiThreadAndWait(new Callable<Object>() {
                public Object call() throws Exception {
                    parent.onFinally();
                    return null;
                }
            });
        }

        /**
         * Posts the specified runnable to the UI thread using a handler, and waits for operation to
         * finish. If there's an exception, it captures it and rethrows it.
         * 
         * @param c
         *            the callable to post
         * @throws Exception
         *             on error
         */
        protected void postToUiThreadAndWait(final Callable<?> c) throws Exception {
            final CountDownLatch latch = new CountDownLatch(1);
            final Exception[] exceptions = new Exception[1];

            // Execute onSuccess in the UI thread, but wait
            // for it to complete.
            // If it throws an exception, capture that exception
            // and rethrow it later.
            handler.post(new Runnable() {
                public void run() {
                    try {
                        c.call();
                    } catch (Exception e) {
                        exceptions[0] = e;
                    } finally {
                        latch.countDown();
                    }
                }
            });

            // Wait for onSuccess to finish
            latch.await();

            if (exceptions[0] != null) {
                throw exceptions[0];
            }

        }

    }

}
