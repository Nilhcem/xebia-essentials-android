package com.nilhcem.xebia.essentials.test;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.test.core.BaseInstrumentationTestCase;
import com.nilhcem.xebia.essentials.test.core.Compatibility;
import com.nilhcem.xebia.essentials.ui.cards.detail.BaseDetailFragment;
import com.nilhcem.xebia.essentials.ui.cards.detail.pager.DetailPagerFragment;

import org.fest.assertions.api.ANDROID;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class CardsDetailTest extends BaseInstrumentationTestCase {

    public void testCardDetail() {
        shouldDisplayAloneTimeCardView();
        shouldDisplayAloneTimeCardDetail();
        shouldSwipeViewPager();
        backToInitialState();
    }

    private void shouldDisplayAloneTimeCardView() {
        // Select "Alone time" card
        solo.clickOnText("Alone time", 1, true);
        assertThatTextIsFound("Collaboration");

        // Get "Alone time" card fragment
        BaseDetailFragment visibleFragment = getVisibleCardFragment("alone-time");
        View cardLayout = visibleFragment.getView();
        frontCardViewShouldBeVisible(cardLayout);
        screenshot("alone_time_front_card");

        // Flip "Alone time" card
        flipCard(cardLayout);
        assertThatTextIsFound("For some situations");
        backCardViewShouldBeVisible(cardLayout);
        screenshot("alone_time_back_card");
    }

    private BaseDetailFragment getVisibleCardFragment(String expectedCardId) {
        // We can't just use findViewById, as we are in a FragmentStatePagerAdapter (which should have instantiated 3 fragments)
        // So we just loop all the fragments and find the one we are searching for
        FragmentActivity activity = (FragmentActivity) getCurrentActivity();
        FragmentManager fm = activity.getSupportFragmentManager();
        DetailPagerFragment pagerFragment = (DetailPagerFragment) fm.findFragmentById(R.id.main_detail_fragment);
        if (pagerFragment == null) {
            pagerFragment = (DetailPagerFragment) fm.findFragmentByTag(DetailPagerFragment.TAG);
        }

        List<Fragment> fragments = pagerFragment.getChildFragmentManager().getFragments();

        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseDetailFragment) {
                BaseDetailFragment detailFragment = (BaseDetailFragment) fragment;
                if (expectedCardId.equals(detailFragment.getCard().getUrlId())) {
                    return detailFragment;
                }
            }
        }
        return null;
    }

    private void frontCardViewShouldBeVisible(View layout) {
        ANDROID.assertThat(layout.findViewById(R.id.detail_card_front)).isVisible();
        ANDROID.assertThat(layout.findViewById(R.id.detail_card_back)).isNotVisible();
    }

    private void backCardViewShouldBeVisible(View layout) {
        ANDROID.assertThat(layout.findViewById(R.id.detail_card_front)).isNotVisible();
        ANDROID.assertThat(layout.findViewById(R.id.detail_card_back)).isVisible();
    }

    private void flipCard(View layout) {
        solo.clickOnView(layout.findViewById(R.id.detail_card_container));
    }

    private void shouldDisplayAloneTimeCardDetail() {
        solo.clickOnActionBarItem(R.id.action_see_info);

        BaseDetailFragment visibleFragment = getVisibleCardFragment("alone-time");
        View cardLayout = visibleFragment.getView();
        TextView content = (TextView) cardLayout.findViewById(R.id.detail_info_text);
        assertThat(content.getText().toString()).contains("Motivation");

        screenshot("card_detail");
    }

    private void shouldSwipeViewPager() {
        swipeToNextCard();

        BaseDetailFragment visibleFragment = getVisibleCardFragment("assertions");
        View cardLayout = visibleFragment.getView();
        TextView content = (TextView) cardLayout.findViewById(R.id.detail_info_text);
        assertThat(content.getText().toString()).contains("Ideally,");

        screenshot("card_swiped");
    }

    private void swipeToNextCard() {
        Point deviceSize = Compatibility.getScreenSize(solo.getCurrentActivity());
        int screenWidth = deviceSize.x;
        int screenHeight = deviceSize.y;
        int fromX = screenWidth - 10;
        int toX = 10;
        if (getCurrentActivity().getResources().getBoolean(R.bool.has_two_panes)) {
            toX += screenWidth * 35 / 100;
        }
        int fromY = screenHeight / 2;
        int toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 1);
        solo.scrollToTop();
    }

    private void backToInitialState() {
        solo.clickOnActionBarItem(R.id.action_see_card);
        if (getCurrentActivity().getResources().getBoolean(R.bool.has_two_panes)) {
            solo.scrollToTop();
            solo.clickOnText("APIs,", 1, true);
        }
    }
}
