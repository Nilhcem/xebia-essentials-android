package com.nilhcem.xebia.essentials.ui.cards.detail.pager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CategoriesDao;
import com.nilhcem.xebia.essentials.core.utils.ColorUtils;
import com.nilhcem.xebia.essentials.events.CardChangedEvent;
import com.nilhcem.xebia.essentials.events.CategoryChangedEvent;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.model.Category;
import com.nilhcem.xebia.essentials.ui.base.BaseFragment;
import com.nilhcem.xebia.essentials.ui.cards.random.RandomMenuHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.InjectView;
import timber.log.Timber;

import static com.nilhcem.xebia.essentials.core.data.provider.DataProvider.CARD_POSITION_UNSET;

public class DetailPagerFragment extends BaseFragment implements ViewPager.OnPageChangeListener, DetailPagerTransformer.OnPageTransformedListener {

    public static final String TAG = DetailPagerFragment.class.getSimpleName();
    private static final String ARG_CARD = "mCard";

    // Optional single card to display (if received from intent-filer)
    private Card mCard;

    @Inject RandomMenuHelper mRandomMenuHelper;
    @Inject DetailPagerTransformer mDetailPagerTransformer;
    @Inject CategoriesDao mCategoriesDao;

    @InjectView(R.id.detail_pager) ViewPager mViewPager;
    private DetailPagerAdapter mAdapter;

    private ShareActionProvider mShareActionProvider;

    public DetailPagerFragment() {
        super(R.layout.detail_pager_fragment, true);
    }

    public static DetailPagerFragment newInstance(Card card) {
        DetailPagerFragment fragment = new DetailPagerFragment();
        if (card != null) {
            Bundle args = new Bundle();
            args.putParcelable(ARG_CARD, card);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter = new DetailPagerAdapter(getActivity(), getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, mDetailPagerTransformer);
        mDetailPagerTransformer.setPageTransformedListener(this);

        Bundle arguments = getArguments();
        if (arguments == null) {
            List<Card> cards = mDataProvider.getCards();
            mAdapter.updateItems(cards);
            mViewPager.setCurrentItem(mDataProvider.getCurrentCardPosition(false), false);
            setViewPagerBackground(cards.get(0).getCategory().getColor());
        } else {
            mCard = arguments.getParcelable(ARG_CARD);
            mAdapter.updateItems(Arrays.asList(mCard));
            mViewPager.setCurrentItem(0, false);
            setViewPagerBackground(mCard.getCategory().getColor());
        }

        mViewPager.setOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Context context = getActivity();
        mRandomMenuHelper.onCreateOptionsMenu(context, menu, inflater);

        inflater.inflate(R.menu.card_detail_menu, menu);

        menu.findItem(R.id.action_see_card).setIcon(
                new IconDrawable(context, Iconify.IconValue.fa_list_alt)
                        .colorRes(R.color.actionbar_content)
                        .actionBarSize());

        menu.findItem(R.id.action_see_info).setIcon(
                new IconDrawable(context, Iconify.IconValue.fa_align_left)
                        .colorRes(R.color.actionbar_content)
                        .actionBarSize());

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem card = menu.findItem(R.id.action_see_card);
        MenuItem info = menu.findItem(R.id.action_see_info);

        boolean showInfoIcon = mDataProvider.shouldDisplayCardView();
        info.setVisible(showInfoIcon);
        card.setVisible(!showInfoIcon);
        setShareIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!mRandomMenuHelper.onOptionsItemSelected(getActivity(), item)) {
            switch (item.getItemId()) {
                case R.id.action_see_card:
                case R.id.action_see_info:
                    mDataProvider.switchDisplayMode();
                    updateDisplayMode();
                    return true;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDisplayMode();
    }

    private void updateDisplayMode() {
        mAdapter.notifyDataSetChanged();
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Do nothing
    }

    @Override
    public void onPageSelected(int position) {
        Timber.d("onPageSelected(position=%d)", position);
        mDataProvider.setCurrentCardPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Do nothing
    }

    public void onEventMainThread(CardChangedEvent event) {
        Timber.d("onEventMainThread(CardChangedEvent=%s)", event);
        int position = event.cardPosition;
        if (position != CARD_POSITION_UNSET) {
            if (mViewPager.getCurrentItem() != position) {
                mDetailPagerTransformer.setEnableTransformations(false);
                mViewPager.setCurrentItem(position, false);
                mDetailPagerTransformer.setEnableTransformations(true);
            }
            setShareIntent();
            setViewPagerBackground(mDataProvider.getCardAt(position).getCategory().getColor());
        }
    }

    public void onEventMainThread(CategoryChangedEvent event) {
        Timber.d("onEventMainThread(CategoryChangedEvent=%s)", event);
        setViewPagerBackground(mCategoriesDao.getCategoryColor(event.categoryId, getResources()));
        mViewPager.setCurrentItem(0, false);
        mAdapter.updateItems(mDataProvider.getCards());
        setShareIntent();
    }

    public void setViewPagerBackground(int color) {
        mViewPager.setBackgroundColor(ColorUtils.darker(color, 0.30f));
    }

    private void setShareIntent() {
        if (ActivityManager.isUserAMonkey()) {
            Timber.d("Monkeys do not share - Monkeys are selfish");
            return;
        }

        Card currentCard = mCard;
        List<Card> cards = mDataProvider.getCards();
        if (currentCard == null && !cards.isEmpty()) {
            currentCard = cards.get(mDataProvider.getCurrentCardPosition(false));
        }

        if (currentCard != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentCard.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(Locale.US, "%s%s",
                    CardsDao.BASE_URL_ESSENTIALS, currentCard.getUrlId()));
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    /**
     * The following will change (and fade) the viewpager background color depending on the
     * current and next cards colors.
     */
    @Override
    public void onPageTransformed(View view, float position) {
        int category = mDataProvider.getCurrentCategoryId();
        if (category == Category.CATEGORY_ID_ALL || category == Category.CATEGORY_ID_SEARCH) {
            int currentItem = mViewPager.getCurrentItem();
            int nextItem = currentItem + (position == 0 ? 0 : (position < 0 ? 1 : -1));

            if (mDataProvider.getCardAt(currentItem).getId().equals(view.getTag())) {
                int curColor = mDataProvider.getCardAt(currentItem).getCategory().getColor();
                int nextColor = mDataProvider.getCardAt(nextItem).getCategory().getColor();
                int newColor = ColorUtils.mixColors(nextColor, curColor, Math.abs(position));
                setViewPagerBackground(newColor);
            }
        }
    }
}
