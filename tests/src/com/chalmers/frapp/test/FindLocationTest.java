package com.chalmers.frapp.test;

import java.util.List;

import com.chalmers.frapp.FindLocationActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import com.chalmers.frapp.R;

/**
 * Test the find location activity to verify that the userinterface
 * behaves as expected and produces the expected results.
 */
public class FindLocationTest extends ActivityInstrumentationTestCase2<FindLocationActivity> {

	private AutoCompleteTextView textbox;
	
	public FindLocationTest() {
		super(FindLocationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		textbox = (AutoCompleteTextView) getActivity().findViewById(R.id.autoCompleteTextView1);
	}

	/**
	 * Attempt to search and use the AutoCompleteTextView to
	 * fin a location.
	 */
	/*
	public final void testSearch() {
		TouchUtils.tapView(this, textbox);
		sendKeys("H C");
		getInstrumentation().waitForIdleSync();
		sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
		sendKeys(KeyEvent.KEYCODE_ENTER);
		getInstrumentation().waitForIdleSync();
		ActivityManager am = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
		assertNotNull(am);
		List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		String act = taskInfo.get(0).topActivity.getClassName();
		assertEquals("DisplayLocationActivity", act);
	}
	*/
}
