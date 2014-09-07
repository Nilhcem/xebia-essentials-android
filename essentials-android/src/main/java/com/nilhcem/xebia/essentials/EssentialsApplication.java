package com.nilhcem.xebia.essentials;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.nilhcem.xebia.essentials.core.data.importer.XmlDataImporter;
import com.nilhcem.xebia.essentials.core.logging.ReleaseLogTree;

import dagger.ObjectGraph;
import hugo.weaving.DebugLog;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class EssentialsApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        ActiveAndroid.initialize(this);
        buildObjectGraphAndInject();
        importData();
        CalligraphyConfig.initDefault(R.attr.fontPath);
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseLogTree());
        }
    }

    private void buildObjectGraphAndInject() {
        mObjectGraph = ObjectGraph.create(new EssentialsModule(this));
        mObjectGraph.inject(this);
    }

    @DebugLog
    private void importData() {
        // We should definitively review this someday.
        // Too much work there is clearly bad, but is a splash screen better?
        mObjectGraph.get(XmlDataImporter.class).initialize();
    }

    public void inject(Object target) {
        mObjectGraph.inject(target);
    }

    public <T> T get(Class<T> type) {
        return mObjectGraph.get(type);
    }

    public static EssentialsApplication get(Context context) {
        return (EssentialsApplication) context.getApplicationContext();
    }
}
