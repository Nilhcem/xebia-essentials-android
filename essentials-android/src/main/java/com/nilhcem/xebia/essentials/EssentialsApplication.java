package com.nilhcem.xebia.essentials;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.nilhcem.xebia.essentials.core.data.importer.JsonDataImporter;

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
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void buildObjectGraphAndInject() {
        mObjectGraph = ObjectGraph.create(new EssentialsModule(this));
        mObjectGraph.inject(this);
    }

    @DebugLog
    private void importData() {
        // We should definitively review this someday.
        // Too much work there is clearly bad, but a splash screen is not a good solution either
        mObjectGraph.get(JsonDataImporter.class).initialize();
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
