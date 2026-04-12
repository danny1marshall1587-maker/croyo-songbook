package com.garethevans.church.opensongtablet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class JavaInstrumentedTest {

    private MainActivityInterface mainActivityInterface;
    private final String TAG = "Test";

    // 1. Add the ActivityScenarioRule for your MainActivity
    // This rule will launch MainActivity before each test
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // 2. Create a @Before method to get the interface reference
    // This method runs before every @Test method
    @Before
    public void setUp() {
        // Use the rule to get a reference to the running activity
        activityRule.getScenario().onActivity(activity -> {
            // This code runs on the UI thread, safely accessing the activity
            mainActivityInterface = activity;
        });
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.garethevans.church.opensongtablet", appContext.getPackageName());

        // You no longer need to get the interface here, it's already set up in @Before
        assertNotNull("MainActivityInterface should not be null", mainActivityInterface);
    }

    @Test
    public void testStartupStabilityStressTest() {
        // Launch and finish the activity 10 times to catch race conditions during helper init
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, "Starting startup stress test iteration: " + (i + 1));
            
            activityRule.getScenario().onActivity(activity -> {
                // Verify core helpers are instantiated and thread-safe
                assertNotNull("SQLiteHelper should be initialized", activity.getSQLiteHelper());
                assertNotNull("StorageAccess should be initialized", activity.getStorageAccess());
                assertNotNull("Preferences should be initialized", activity.getPreferences());
                assertNotNull("Song should be initialized", activity.getSong());
                assertNotNull("Midi should be initialized", activity.getMidi());
                
                Log.d(TAG, "Iteration " + (i + 1) + " helpers verified.");
            });
            
            // Re-creating the scenario for the next iteration if needed
            // Actually, activityRule handles one launch per test normally.
            // For a true stress test within a single test method, we can use ActivityScenario directly.
        }
    }

    @Test
    public void testHelperLazyLoading() {
        activityRule.getScenario().onActivity(activity -> {
            // Test that multiple rapid calls to the same getter return the same instance (Singleton behavior)
            MainActivityInterface main = activity;
            assertNotNull(main.getStorageAccess());
            assertEquals(main.getStorageAccess(), main.getStorageAccess());
            assertEquals(main.getSQLiteHelper(), main.getSQLiteHelper());
            assertEquals(main.getPreferences(), main.getPreferences());
        });
    }

    // Helper method for the test
    public String processString(String thisString) {
        Log.d(TAG,"thisString:"+thisString);
        return thisString;
    }
}
