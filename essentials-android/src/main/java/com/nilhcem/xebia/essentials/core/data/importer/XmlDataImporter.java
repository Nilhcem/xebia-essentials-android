package com.nilhcem.xebia.essentials.core.data.importer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.model.Category;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class XmlDataImporter {

    private static final String PREFS_KEY_VERSION = "version_code";

    @Inject Application mApplication;
    @Inject CardsDao mCardsDao;
    @Inject DataProvider mDataProvider;
    @Inject SharedPreferences mSharedPrefs;

    public void initialize() {
        if (shouldImportData()) {
            XmlData data = getDataFromXmlFile(mApplication);
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
            importData = mCardsDao.hasCards();
        }
        return importData;
    }

    private XmlData getDataFromXmlFile(Context context) {
        Timber.d("Importing data from XML");
        InputStream is = context.getResources().openRawResource(R.raw.cards_data);

        try {
            Serializer serializer = new Persister();
            return serializer.read(XmlData.class, is);
        } catch (Exception e) {
            Timber.e(e, "");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Timber.e(e, "");
                }
            }
        }
        return null;
    }

    private void saveToDatabase(XmlData xmlData) {
        ActiveAndroid.beginTransaction();
        try {
            new Delete().from(Card.class).execute();
            new Delete().from(Category.class).execute();

            SparseArray<Category> dbCategories = new SparseArray<>();

            List<XmlCategory> xmlCategories = xmlData.getCategories();
            for (XmlCategory xmlCategory : xmlCategories) {
                Category dbCategory = new Category(xmlCategory);
                dbCategories.put(xmlCategory.getId(), dbCategory);
                dbCategory.save();
            }

            List<XmlCard> xmlCards = xmlData.getCards();
            for (XmlCard xmlCard : xmlCards) {
                Card dbCard = new Card(xmlCard, dbCategories.get(xmlCard.getCategoryId()));
                dbCard.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
