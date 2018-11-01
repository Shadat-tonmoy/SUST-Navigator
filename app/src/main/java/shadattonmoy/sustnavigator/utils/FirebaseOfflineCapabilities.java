package shadattonmoy.sustnavigator.utils;

import com.google.firebase.database.FirebaseDatabase;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class FirebaseOfflineCapabilities extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

