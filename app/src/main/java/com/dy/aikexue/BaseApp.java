package com.dy.aikexue;

import android.app.Application;
import android.content.Context;


import com.dy.aikexue.ssolibrary.util.Dysso;
import com.dy.aikexue.ssolibrary.util.ThirdPartyConstants;

import org.cny.awf.er.CrashHandler;
import org.cny.awf.net.http.H;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseApp extends Application {

    public static BaseApp instance;

    public static BaseApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        H.CTX = this;
        Dysso.createInstance(this);
        ThirdPartyConstants.setDebug(true);
        Thread.setDefaultUncaughtExceptionHandler(CrashException.instance());
    }

    public static class CrashException extends CrashHandler {
        private static CrashException CrashExp;
        protected static Logger L = LoggerFactory.getLogger(CrashException.class.getName());
        ;

        public static CrashHandler instance() {
            if (CrashExp == null) {
                CrashExp = new CrashException();
            }
            return CrashExp;
        }

        @Override
        public void uncaughtException(Thread thr, Throwable e) {
            super.uncaughtException(thr, e);
            L.error("have a uncaughtException");
            System.exit(0);
        }
    }

    /**
     * Avoiding the 64K Limit
     * @param base
     */
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
