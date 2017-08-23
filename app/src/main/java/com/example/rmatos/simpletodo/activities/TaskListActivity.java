package com.example.rmatos.simpletodo.activities;

import android.support.v4.app.Fragment;

import com.example.rmatos.simpletodo.activities.SingleFragmentActivity;
import com.example.rmatos.simpletodo.fragments.TaskListFragment;

/**
 * Created by RMatos on 05/08/2017.
 */

public class TaskListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}
