package shadattonmoy.sustnavigator.utils;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseOfflineCapabilities extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

