package com.dy.aikexue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by mingshanmo on 2017/4/22.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class , sdk = 25)
public class MainActivityTest {

    @Test
    public void testlearn_isCorrect() throws Exception {

        MainActivity mainActivity = new MainActivity();
        mainActivity.initView();
    }

    @Before
    public void setUp() {
        Activity activity = Robolectric.setupActivity(MainActivity.class);
    }

}