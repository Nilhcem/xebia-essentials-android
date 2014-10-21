package com.nilhcem.xebia.essentials;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nilhcem.xebia.essentials.core.data.importer.JsonDataImporter;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.core.search.SearchSuggestionProvider;
import com.nilhcem.xebia.essentials.ui.cards.CardsActivity;
import com.nilhcem.xebia.essentials.ui.cards.detail.DetailActivity;
import com.nilhcem.xebia.essentials.ui.cards.detail.card.DetailCardFragment;
import com.nilhcem.xebia.essentials.ui.cards.detail.info.DetailInfoFragment;
import com.nilhcem.xebia.essentials.ui.cards.detail.pager.DetailPagerAdapter;
import com.nilhcem.xebia.essentials.ui.cards.detail.pager.DetailPagerFragment;
import com.nilhcem.xebia.essentials.ui.cards.listing.ListingAdapter;
import com.nilhcem.xebia.essentials.ui.cards.listing.ListingEntryView;
import com.nilhcem.xebia.essentials.ui.cards.listing.ListingFragment;
import com.nilhcem.xebia.essentials.ui.drawer.MenuDrawerAdapter;
import com.nilhcem.xebia.essentials.ui.search.SearchResultActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module(
        injects = {
                EssentialsApplication.class,
                JsonDataImporter.class,
                DataProvider.class,

                SearchResultActivity.class,
                SearchSuggestionProvider.class,

                MenuDrawerAdapter.class,
                CardsActivity.class,
                ListingFragment.class,
                ListingAdapter.class,
                ListingEntryView.class,

                DetailActivity.class,
                DetailPagerFragment.class,
                DetailPagerAdapter.class,
                DetailCardFragment.class,
                DetailInfoFragment.class
        }
)
public class EssentialsModule {

    private final EssentialsApplication mApp;

    public EssentialsModule(EssentialsApplication app) {
        mApp = app;
    }

    @Provides @Singleton Application provideApplication() {
        return mApp;
    }

    @Provides @Singleton EventBus provideBus() {
        return EventBus.getDefault();
    }

    @Provides @Singleton SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("essentials", Context.MODE_PRIVATE);
    }
}
