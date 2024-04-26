package sp.openconnect.remote.data;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

// I need some object ;-)
public class Global extends GlobalHelper {
    public Global(Context context) {
        setMainApplication(context);
    }

    public Activity getMainApplication() {
        return (Activity) this.mainApplication;
    }

    public void setMainApplication(@NonNull Context mainApplication) {
        this.mainApplication = mainApplication;
    }

}