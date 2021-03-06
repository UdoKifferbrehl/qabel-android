package de.qabel.qabelbox;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import de.qabel.core.logging.AndroidLoggerWrapper;
import de.qabel.core.logging.QabelLoggerManager;
import de.qabel.qabelbox.chat.services.AndroidChatServiceResponder;
import de.qabel.qabelbox.dagger.components.ApplicationComponent;
import de.qabel.qabelbox.dagger.components.DaggerApplicationComponent;
import de.qabel.qabelbox.dagger.modules.ApplicationModule;
import de.qabel.qabelbox.index.AndroidIndexSyncService;
import de.qabel.qabelbox.index.IndexIdentityListener;

public class QabelBoxApplication extends Application {

    private static final String TAG = "QabelBoxApplication";
    public static final String DEFAULT_DROP_SERVER = "https://test-drop.qabel.de";

    static QabelBoxApplication mInstance = null;

    static {
        // Enforce SpongyCastle as JCE provider
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        //Set android logging adapter
        QabelLoggerManager.INSTANCE.setFactory(AndroidLoggerWrapper.Factory.INSTANCE);
    }

    @Override
    protected void attachBaseContext(Context base) {
        installMultiDex(base);
        super.attachBaseContext(base);
    }

    protected void installMultiDex(Context base) {
        MultiDex.install(base);
    }

    /**
     * @deprecated This is not guaranteed to be initialised
     */
    @Deprecated
    public static QabelBoxApplication getInstance() {
        return mInstance;
    }

    public ApplicationComponent getApplicationComponent() {
        return ApplicationComponent;
    }

    private ApplicationComponent ApplicationComponent;

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((QabelBoxApplication) context.getApplicationContext()).getApplicationComponent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        this.ApplicationComponent = initialiseInjector();
        mInstance = this;
        try{
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false);
        }
        catch(Exception ignored){
        }
    }

    protected ApplicationComponent initialiseInjector() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

}
