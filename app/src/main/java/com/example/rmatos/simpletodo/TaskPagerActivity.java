package com.example.rmatos.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Allows you to swipe on TaskFragment to go to next instance of RecyclerView
 */

public class TaskPagerActivity extends AppCompatActivity {
    private static final String EXTRA_TASK_ID = "com.example.rmatos.simpletodo.task_id";

    private ViewPager mViewPager;
    private List<Task> mTasks;


    //Called by a fragment to get intent needed to run activity (by extension TaskFragment)
    public static Intent newIntent(Context packageContext, UUID taskID) {
        Intent intent = new Intent(packageContext, TaskPagerActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskID);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);

        UUID taskID = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);

        mViewPager = (ViewPager) findViewById(R.id.task_view_pager);
        mTasks = TaskStore.get(this).getTasks();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Task task = mTasks.get(position);
                return TaskFragment.newInstance(task.getID());
            }

            @Override
            public int getCount() {
                return mTasks.size();
            }
        });

        //Sets index of current item
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).getID().equals(taskID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
