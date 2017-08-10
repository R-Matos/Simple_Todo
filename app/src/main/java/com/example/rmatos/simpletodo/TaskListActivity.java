package com.example.rmatos.simpletodo;

import android.support.v4.app.Fragment;

/**
 * Created by RMatos on 05/08/2017.
 */

public class TaskListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}
