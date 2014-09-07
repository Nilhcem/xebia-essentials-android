package com.nilhcem.xebia.essentials.test;

import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.test.core.BaseInstrumentationTestCase;
import com.nilhcem.xebia.essentials.test.core.Compatibility;
import com.nilhcem.xebia.essentials.ui.cards.listing.ListingFragment;
import com.nilhcem.xebia.essentials.ui.drawer.MenuDrawerEntryItemView;

import static org.fest.assertions.api.Assertions.assertThat;

public class CardsListingTest extends BaseInstrumentationTestCase {

    public void testCardsListing() {
        screenshot("cards_listing");
        shouldDisplayAllCards();
        shouldDisplayCollaborationCards();
        shouldDisplayCraftsmanshipCards();
        shouldDisplayRealisationCards();
        shouldDisplayTestingCards();
        shouldDisplayAboutDialog();
    }

    private void shouldDisplayAllCards() {
        assertCardsListEntryNb(74);
        assertThatTextIsFound("Have fun");
        assertThatTextIsFound("Review code");
        screenshot("all_cards_visible");
    }

    private void shouldDisplayCollaborationCards() {
        openNavigationDrawer();
        clickOnNavigationDrawerText("Collaboration");
        assertThatTextIsFound("Brutal");
        assertCardsListEntryNb(22);
        screenshot("collaboration_cards");
    }

    private void shouldDisplayCraftsmanshipCards() {
        openNavigationDrawer();
        clickOnNavigationDrawerText("Craftsmanship");
        assertThatTextIsFound("Be curious");
        assertCardsListEntryNb(23);
        screenshot("craftsmanship_cards");
    }

    private void shouldDisplayRealisationCards() {
        openNavigationDrawer();
        clickOnNavigationDrawerText("Realisation");
        assertThatTextIsFound("Assert");
        assertCardsListEntryNb(18);
        screenshot("realisation_cards");
    }

    private void shouldDisplayTestingCards() {
        openNavigationDrawer();
        clickOnNavigationDrawerText("Testing");
        assertThatTextIsFound("Fail fast");
        assertCardsListEntryNb(11);
        screenshot("testing_cards");
    }

    private void shouldDisplayAboutDialog() {
        openNavigationDrawer();
        clickOnNavigationDrawerText("About");
        assertThatTextIsFound("GOOD SOFTWARE");
        assertThatTextIsFound("ABOUT XEBIA");
        screenshot("about_dialog");
        solo.goBack();
    }

    private void assertCardsListEntryNb(int expectedNb) {
        FragmentActivity activity = (FragmentActivity) getCurrentActivity();
        ListingFragment fragment = (ListingFragment) activity.getSupportFragmentManager().findFragmentById(R.id.main_listing_fragment);
        ListView listView = fragment.getListView();
        assertThat(listView.getCount()).isEqualTo(expectedNb);
    }

    private void openNavigationDrawer() {
        solo.scrollToTop();
        Point deviceSize = Compatibility.getScreenSize(solo.getCurrentActivity());
        int screenWidth = deviceSize.x;
        int screenHeight = deviceSize.y;
        int fromX = 0;
        int toX = screenWidth / 2;
        int fromY = screenHeight / 2;
        int toY = fromY;
        solo.drag(fromX, toX, fromY, toY, 1);
        solo.scrollToTop();

        screenshot("navigation_drawer_opened");
    }

    private void clickOnNavigationDrawerText(String text) {
        ListView drawer = (ListView) getCurrentActivity().findViewById(R.id.left_drawer);
        for (int i = 0; i < drawer.getChildCount(); i++) {
            View child = drawer.getChildAt(i);
            if (child instanceof MenuDrawerEntryItemView) {
                if (text.equals(((TextView) child.findViewById(R.id.drawer_entry_title)).getText().toString())) {
                    drawer.smoothScrollToPosition(i);
                    solo.clickOnView(child);
                    break;
                }
            }
        }
        solo.scrollToTop();
    }
}
