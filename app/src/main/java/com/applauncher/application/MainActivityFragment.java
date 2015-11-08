package com.applauncher.application;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appbrain.AppBrainBanner;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    RecyclerView mRecycleView;
    View coordinatorLayoutView;

    private final String TAG = "MainActivityFragment";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        //assign recyclerview UI and sets its display layout type
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();

        return rootView;
    }



    private void setupAdapter(){

        //setup intent to look for aps that contain launcher activites
        Intent startUpIntent = new Intent(Intent.ACTION_MAIN);
        startUpIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();

        //looks up all applications using the specific intent type
        List<ResolveInfo> activities = pm.queryIntentActivities(startUpIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                //sort the activity names in alphabetic order
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        lhs.loadLabel(pm).toString(), rhs.loadLabel(pm).toString());
            }
        });

        //log the number of found applications
        Log.i(TAG, "FOUND " + activities.size() + " activities on device");
        mRecycleView.setAdapter(new ActivityAdapter(activities));

    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ResolveInfo mResolverInfo;
        private TextView mNameTextView;
        private ImageView mIcon;

        public ActivityHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.appNameTextView);
            mIcon = (ImageView) itemView.findViewById(R.id.appIconImageView);

            mNameTextView.setOnClickListener(this);
            mIcon.setOnClickListener(this);
        }

        public void bindActivity(ResolveInfo resolveInfo){

            mResolverInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolverInfo.loadLabel(pm).toString();
            Drawable icon = mResolverInfo.loadIcon(pm).getCurrent();
            mNameTextView.setText(appName); //set application name
            mIcon.setImageDrawable(icon); //set the application icon

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            //called when user presses an app name or icon in the list
            ActivityInfo activityInfo = mResolverInfo.activityInfo;

            //creates a new intent with the clicked resolveInfo listing
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.setClassName(activityInfo.applicationInfo.packageName,activityInfo.name);
            startActivity(launchIntent); //launches the application selected!
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{

        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities = activities;
        }

        /**
         * Called when RecyclerView needs a new {@linkViewHolder} of the given type to represent
         * an item.
         * <p/>
         * This new ViewHolder should be constructed with a new View that can represent the items
         * of the given type. You can either create a new View manually or inflate it from an XML
         * layout file.
         * <p/>
         * The new ViewHolder will be used to display items of the adapter using
         * {@link#onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
         * different items in the data set, it is a good idea to cache references to sub views of
         * the View to avoid unnecessary {@link View#findViewById(int)} calls.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         * @see #getItemViewType(int)
         * @see#onBindViewHolder(ViewHolder, int)
         */
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.data_row,parent,false);

            return new ActivityHolder(view);
        }

        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {@linkViewHolder#itemView} to reflect the item at the given
         * position.
         * <p/>
         * Note that unlike {@link ListView}, RecyclerView will not call this method
         * again if the position of the item changes in the data set unless the item itself is
         * invalidated or the new position cannot be determined. For this reason, you should only
         * use the <code>position</code> parameter while acquiring the related data item inside
         * this method and should not keep a copy of it. If you need the position of an item later
         * on (e.g. in a click listener), use {@linkViewHolder#getAdapterPosition()} which will
         * have the updated adapter position.
         * <p/>
         * Override {@link#onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
         * handle effcient partial bind.
         *
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        /**
         * Returns the total number of items in the data set hold by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return mActivities.size(); //the number of activities gathered from setup adapter()
        }
    }

    public void addBanner(ViewGroup parent) {
        AppBrainBanner banner = new AppBrainBanner(getContext());
        parent.addView(banner);
    }
}
