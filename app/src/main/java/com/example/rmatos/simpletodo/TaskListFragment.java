package com.example.rmatos.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by RMatos on 05/08/2017.
 */

public class TaskListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";                                //For screen rotation

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Needed for vector graphics
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //Enables menu inflation
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        mTaskRecyclerView = (RecyclerView) view.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));                 //TODO: Check different layouts

        //Retrieves saved values
        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    //Inflates menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);

        //Changes text depending on subtitle state
        MenuItem subtitleItem = menu.findItem(R.id.list_menu_show_count);
            if (mSubtitleVisible)
                subtitleItem.setTitle(R.string.list_menu_enable_counter);
            else
                subtitleItem.setTitle(R.string.list_menu_disable_counter);

    }

    //Handles menu events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_menu_new_task:
                Task task = new Task();
                TaskStore.get(getActivity()).addTask(task);
                Intent intent = TaskPagerActivity.newIntent(getActivity(), task.getID());
                startActivity(intent);
                return true;
            case R.id.list_menu_show_count:
                mSubtitleVisible = (!mSubtitleVisible);
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



    }

    //Updates actionbar subtitle (task counter)
    private void updateSubtitle() {
        TaskStore taskStore = TaskStore.get(getActivity());
        int taskCount = taskStore.getTasks().size();
        String subtitle = getString(R.string.list_subtitle_task_count, taskCount);

        if (!mSubtitleVisible)
            subtitle = null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        TaskStore taskStore = TaskStore.get(getActivity());
        List<Task> tasks = taskStore.getTasks();

        //Update or Create RecyclerView
        if (mAdapter == null) {
            mAdapter = new TaskAdapter(tasks);
            mTaskRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }











    /**
     * Viewholder
     */
    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final int NOTE_LIMIT = 30;

        private Task mTask;

        private TextView mTitleTextView;
        private TextView mNoteTextView;
        private TextView mDueDateTextView;
        private TextView mReminderDateTextView;
        private TextView mReminderTimeTextView;
        private ImageView mReminderTypeImageView;


        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.listItem_title);
            mNoteTextView = (TextView) itemView.findViewById(R.id.listItem_note);
            mDueDateTextView = (TextView) itemView.findViewById(R.id.listItem_dueDate);
            mReminderDateTextView = (TextView) itemView.findViewById(R.id.listItem_reminder_date);
            mReminderTimeTextView = (TextView) itemView.findViewById(R.id.listItem_reminder_time);
            mReminderTypeImageView = (ImageView) itemView.findViewById(R.id.listItem_reminder_type);
        }

        public void bind(Task task) {
            mTask = task;
            DateFormat dateFormat = new DateFormat();
            mTitleTextView.setText(task.getTitle());
            if (mTask.getNote() != null) {
                mNoteTextView.setVisibility(View.VISIBLE);
                mNoteTextView.setText((task.getNote().length() > NOTE_LIMIT) ?
                        task.getNote().substring(0,NOTE_LIMIT) : task.getNote());
            } else {
                mNoteTextView.setVisibility(View.INVISIBLE);
            }
            if (mTask.getDueDate() != null) {
                mDueDateTextView.setVisibility(View.VISIBLE);
                mDueDateTextView.setText(dateFormat.format("EEE, dd MMM yyyy, HH:mm", mTask.getDueDate()));
            } else {
                mDueDateTextView.setVisibility(View.INVISIBLE);
            }
            if (mTask.getReminder() != null) {
                mReminderDateTextView.setVisibility(View.VISIBLE);
                mReminderTimeTextView.setVisibility(View.VISIBLE);
                mReminderDateTextView.setText(dateFormat.format("EEE, dd MMM yyyy", mTask.getReminder()));
                mReminderTimeTextView.setText(dateFormat.format("HH:mm", mTask.getReminder()));
            } else {
                mReminderDateTextView.setVisibility(View.INVISIBLE);
                mReminderTimeTextView.setVisibility(View.INVISIBLE);
            }
            if (mTask.getReminderType() == Task.ReminderType.NONE) {
                mReminderTypeImageView.setVisibility(View.INVISIBLE);
            } else if (mTask.getReminderType() == Task.ReminderType.ALARM) {
                mReminderTypeImageView.setVisibility(View.VISIBLE);
                mReminderTypeImageView.setImageResource(R.drawable.ic_alarm);
            } else if (mTask.getReminderType() == Task.ReminderType.NOTIFICATION) {
                mReminderTypeImageView.setVisibility(View.VISIBLE);
                mReminderTypeImageView.setImageResource(R.drawable.ic_notification);
            }
        }

        //Click on list item
        @Override
        public void onClick(View view) {
            //Starts TaskFragment
            Intent intent = TaskPagerActivity.newIntent(getActivity(), mTask.getID());
            startActivity(intent);
        }
    }


    /**
     * Adapter
     * Model - View
     */
    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> mTasks;

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }
    }



}
