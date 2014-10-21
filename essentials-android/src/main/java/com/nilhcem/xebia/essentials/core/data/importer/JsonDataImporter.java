package com.nilhcem.xebia.essentials.core.data.importer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.google.gson.Gson;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.model.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class JsonDataImporter {

    private static final String PREFS_KEY_VERSION = "version_code";

    @Inject Application mApplication;
    @Inject CardsDao mCardsDao;
    @Inject DataProvider mDataProvider;
    @Inject SharedPreferences mSharedPrefs;

    public void initialize() {
        if (shouldImportData()) {
            JsonData data = getDataFromJsonFile(mApplication);
            saveToDatabase(data);
        }
        mDataProvider.initData();
    }

    private boolean shouldImportData() {
        boolean importData = true;

        // Force re-importing data each time the application is updated
        try {
            PackageInfo info = mApplication.getPackageManager().getPackageInfo(mApplication.getPackageName(), 0);
            int thisVersion = info.versionCode;
            int lastVersion = mSharedPrefs.getInt(PREFS_KEY_VERSION, 0);

            if (thisVersion == lastVersion) {
                importData = false;
            } else {
                Timber.d("Version code has changed");
                mSharedPrefs.edit().putInt(PREFS_KEY_VERSION, thisVersion).apply();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Failed getting version code");
        }

        // Force importing data if database does not contain any items
        if (!importData) {
            importData = !mCardsDao.hasCards();
        }
        return importData;
    }

    private JsonData getDataFromJsonFile(Context context) {
        Timber.d("Importing data from JSON");
        InputStream is = context.getResources().openRawResource(R.raw.cards_data);
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            return new Gson().fromJson(reader, JsonData.class);
        } catch (IOException e) {
            Timber.e(e, "Error getting data from Json");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Timber.e(e, "Error closing inputstream");
            }
        }
        return null;
    }

    private void saveToDatabase(JsonData jsonData) {
        ActiveAndroid.beginTransaction();
        try {
            new Delete().from(Card.class).execute();
            new Delete().from(Category.class).execute();

            SparseArray<Category> dbCategories = new SparseArray<>();

            List<JsonCategory> jsonCategories = jsonData.getCategories();
            for (JsonCategory jsonCategory : jsonCategories) {
                Category dbCategory = new Category(jsonCategory);
                dbCategories.put(jsonCategory.getId(), dbCategory);
                dbCategory.save();
            }

            List<JsonCard> jsonCards = jsonData.getCards();
            for (JsonCard jsonCard : jsonCards) {
                Card dbCard = new Card(jsonCard, dbCategories.get(jsonCard.getCategory()));
                dbCard.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
