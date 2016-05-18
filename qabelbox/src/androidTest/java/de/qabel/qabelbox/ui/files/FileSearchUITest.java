package de.qabel.qabelbox.ui.files;


import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.widget.SeekBar;

import com.squareup.spoon.Spoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.qabel.qabelbox.R;
import de.qabel.qabelbox.fragments.FilesSearchResultFragment;
import de.qabel.qabelbox.storage.StorageSearch;
import de.qabel.qabelbox.ui.AbstractUITest;
import de.qabel.qabelbox.ui.helper.UITestHelper;
import de.qabel.qabelbox.ui.idling.InjectedIdlingResource;
import de.qabel.qabelbox.ui.idling.WaitResourceCallback;
import de.qabel.qabelbox.ui.matcher.QabelMatcher;
import de.qabel.qabelbox.ui.matcher.ToolbarMatcher;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static de.qabel.qabelbox.ui.action.QabelViewAction.setText;

public class FileSearchUITest extends AbstractUITest {

    private InjectedIdlingResource idlingResource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        uploadTestFiles();
        launchActivity(new Intent(Intent.ACTION_MAIN));
        idlingResource = new InjectedIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }


    @After
    public void unRegisterIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    private void uploadTestFiles() {

        int fileCount = 7;
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "testfile 2", new byte[1011], "");
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "red.png", new byte[1], "");
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "green.png", new byte[100], "");
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "blue.png", new byte[1011], "");
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "black_1.png", new byte[1011], "");
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "black_2.png", new byte[1024 * 10], "");
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "white.png", new byte[1011], "");


        mBoxHelper.waitUntilFileCount(fileCount);
    }

    @Test
    public void search1ByNamesTest() {
        Spoon.screenshot(mActivity, "startup");
        testSearch("black", 2);
        testSearch("", 7);
        testSearch("png", 6);
        Spoon.screenshot(mActivity, "after");
    }

    @Test
    public void search2FilterTest() {
        testSearchWithFilter("", 0, 2048, 6, true);
        testSearchWithFilter("", 0, 10240, 7, false);
        testSearchWithFilter("", 9000, 10240, 1, false);
    }

    @Test
    public void search3CacheTest() throws Exception {

        String text = "";
        int results = 7;

        //start search
        onView(withId(R.id.action_search)).perform(click());
        onView(withHint(R.string.ab_filesearch_hint)).perform(setText(text), pressImeActionButton());
        closeSoftKeyboard();

        onView(withId(R.id.files_list)).check(matches(QabelMatcher.withListSize(results)));
        int fileCount = new StorageSearch(mBoxHelper.mBoxVolume.navigate()).getResults().size();

        //uploadAndDeleteLocalfile file
        mBoxHelper.uploadFile(mBoxHelper.mBoxVolume, "black_3", new byte[1024], "");
        mBoxHelper.waitUntilFileCount(fileCount + 1);

        FilesSearchResultFragment searchResultFragment = (FilesSearchResultFragment)mActivity.getFragmentManager().findFragmentByTag(FilesSearchResultFragment.TAG);
        searchResultFragment.injectIdleCallback(idlingResource);

        WaitResourceCallback waitResourceCallback = new WaitResourceCallback();
        idlingResource.registerIdleTransitionCallback(waitResourceCallback);
        //Refresh and check new item is visible
        onView(withId(R.id.files_list)).perform(swipeDown());

        UITestHelper.waitUntil(() -> waitResourceCallback.isDone(), "refresh files failed");

        onView(withId(R.id.files_list)).check(matches(QabelMatcher.withListSize(fileCount + 1)));

        //Back to files
        pressBack();

        testIfFileBrowserDisplayed(fileCount + 1);

        //start new search
        text = "black";
        onView(withId(R.id.action_search)).perform(click());
        onView(withHint(R.string.ab_filesearch_hint)).perform(setText(text), pressImeActionButton());
        closeSoftKeyboard();

        onView(withId(R.id.files_list)).check(matches(QabelMatcher.withListSize(3)));
    }

    /**
     * test if search result match the given. addition check if file browser displayed after back pressed
     *
     * @param text    search text
     * @param results excepted results
     */
    private void testSearch(String text, int results) {

        onView(withId(R.id.action_search)).perform(click());
        onView(withHint(R.string.ab_filesearch_hint)).perform(setText(text), pressImeActionButton());
        closeSoftKeyboard();
        UITestHelper.sleep(200);
        onView(withId(R.id.files_list)).check(matches(QabelMatcher.withListSize(results)));

        pressBack();
        testIfFileBrowserDisplayed(7);
    }

    private void testIfFileBrowserDisplayed(int count) {
        ToolbarMatcher.matchToolbarTitle(mActivity.getString(R.string.headline_files))
                .check(matches(isDisplayed()));
        onView(withId(R.id.files_list)).check(matches(QabelMatcher.withListSize(count)));
    }

    /**
     * test if search result matches the given. addition check if file browser displayed after back pressed
     *
     * @param text    search text
     * @param results excepted results
     */
    private void testSearchWithFilter(String text, int fileSizeMin, int fileSizeMax, int results, boolean screenShot) {

        onView(withId(R.id.action_search)).perform(click());
        onView(withHint(R.string.ab_filesearch_hint)).perform(setText(text), pressImeActionButton());
        closeSoftKeyboard();
        UITestHelper.sleep(500);
        onView(withId(R.id.action_ok)).check(matches(isDisplayed())).perform(click());
        ((SeekBar) mActivity.findViewById(R.id.sbFileSizeMin)).setProgress(fileSizeMin);
        ((SeekBar) mActivity.findViewById(R.id.sbFileSizeMax)).setProgress(fileSizeMax);

        onView(withId(R.id.action_use_filter)).perform(click());

        onView(withId(R.id.files_list)).
                check(matches(isDisplayed())).
                check(matches(QabelMatcher.withListSize(results)));

        pressBack();
        testIfFileBrowserDisplayed(7);
    }
}
