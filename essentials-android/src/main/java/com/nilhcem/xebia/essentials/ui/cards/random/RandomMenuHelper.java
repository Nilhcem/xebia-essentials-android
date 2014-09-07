package com.nilhcem.xebia.essentials.ui.cards.random;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.ui.cards.detail.DetailActivity;

import javax.inject.Inject;

public class RandomMenuHelper {

    @Inject CardsDao mCardsDao;

    public void onCreateOptionsMenu(Context context, Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.random_card_menu, menu);
        menu.findItem(R.id.action_random_card).setIcon(
                new IconDrawable(context, Iconify.IconValue.fa_random)
                        .colorRes(R.color.actionbar_content)
                        .actionBarSize());
    }

    public boolean onOptionsItemSelected(Context context, MenuItem item) {
        if (item.getItemId() == R.id.action_random_card) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_CARD, mCardsDao.getRandomCard());
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
