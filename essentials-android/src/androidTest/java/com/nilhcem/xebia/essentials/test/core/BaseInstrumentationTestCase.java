package com.nilhcem.xebia.essentials.test.core;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.nilhcem.xebia.essentials.ui.cards.CardsActivity;
import com.robotium.solo.Solo;
import com.squareup.spoon.Spoon;

import static org.fest.assertions.api.Assertions.assertThat;

public abstract class BaseInstrumentationTestCase extends ActivityInstrumentationTestCase2<CardsActivity> {

    protected Solo solo;

    public BaseInstrumentationTestCase() {
        super(CardsActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        waitUntilCardsAreVisible();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public Activity getCurrentActivity() {
        return solo.getCurrentActivity();
    }

    public void screenshot(String tag) {
        Spoon.screenshot(getCurrentActivity(), tag);
    }

    public void assertThatTextIsFound(String text) {
        assertThat(solo.searchText(text, 1, true)).overridingErrorMessage("Could not find text: " + text).isTrue();
    }

    private void waitUntilCardsAreVisible() {
        solo.scrollToTop();
        assertThatTextIsFound("APIs,");
    }
}
