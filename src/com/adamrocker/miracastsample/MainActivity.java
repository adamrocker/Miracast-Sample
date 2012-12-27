package com.adamrocker.miracastsample;

import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.util.SparseArray;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private DisplayManager mDisplayManager;
    private DisplayListAdapter mDisplayListAdapter;
    private ListView mListView;
    private final SparseArray<RemotePresentation> mActivePresentations = new SparseArray<RemotePresentation>();

    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {
            mDisplayListAdapter.updateContents();
        }

        @Override
        public void onDisplayChanged(int displayId) {
            mDisplayListAdapter.updateContents();
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            mDisplayListAdapter.updateContents();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_display);
        mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);

        mDisplayListAdapter = new DisplayListAdapter(this);
        mListView = (ListView)findViewById(R.id.display_list);
        mListView.setAdapter(mDisplayListAdapter);
    }
    
    protected void onResume() {
        super.onResume();
        mDisplayListAdapter.updateContents();
        mDisplayManager.registerDisplayListener(mDisplayListener, null);
    }
    
    private void showPresentation(Display display) {
        RemotePresentation presentation = new RemotePresentation(this, display);
        mActivePresentations.put(display.getDisplayId(), presentation);
        presentation.show();
    }
    
    private void hidePresentation(Display display) {
        final int displayId = display.getDisplayId();
        RemotePresentation presentation = mActivePresentations.get(displayId);
        if (presentation == null) {
            return;
        }

        presentation.dismiss();
        mActivePresentations.delete(displayId);
    }

    private final class DisplayListAdapter extends ArrayAdapter<Display> {
        final Context mContext;
        private OnCheckedChangeListener mCheckedRemoteDisplay = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                synchronized (mCheckedRemoteDisplay) {
                    final Display display = (Display)view.getTag();
                    if (isChecked) {
                        showPresentation(display);
                    } else {
                        hidePresentation(display);
                    }
                }
            }
        };

        public DisplayListAdapter(Context context) {
            super(context, R.layout.list_item);
            mContext = context;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View v;
            if (convertView == null) {
                v = ((Activity) mContext).getLayoutInflater().inflate(
                        R.layout.list_item, null);
            } else {
                v = convertView;
            }

            final Display display = getItem(position);

            //TITLE
            TextView tv = (TextView)v.findViewById(R.id.display_id);
            tv.setText(display.getName() + "( ID: " + display.getDisplayId() + " )");

            //DESCRIPTION
            tv = (TextView)v.findViewById(R.id.display_desc);
            tv.setText(display.toString());

            //SHOW or HIDE the presentation 
            CheckBox cb = (CheckBox)v.findViewById(R.id.display_cb);
            cb.setTag(display);
            cb.setOnCheckedChangeListener(mCheckedRemoteDisplay);
            return v;
        }

        public void updateContents() {
            clear();

            Display[] displays = mDisplayManager.getDisplays();
            addAll(displays);

            for (Display display : displays) {
            }
        }
    }
    
    private final class RemotePresentation extends Presentation {
        public RemotePresentation(Context context, Display display) {
            super(context, display);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.remote_display);
        }
    }
}
