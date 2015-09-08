package com.zphinx.sortattributes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

import static android.view.LayoutInflater.from;

public class MainActivity extends Activity {

    public static final int LIST_NUM = 12;
    /**
     * Determines if we are sorting ascending or sorting descending
     */
    public static int sortDirection;

    /**
     * Defines what property to use to execute the sort
     */
    public static int sortIndex;

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView mList;
    private SortListAdapter mAdapter;
    private static ArrayList<SortableImpl> mSortables = null;
    private Button mSortButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        mList = (ListView) findViewById(android.R.id.list);
        mSortButton = (Button) findViewById(R.id.btnSortSearch);
        mSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialogManager sm = new SortDialogManager();
                sm.showAlertDialog(MainActivity.this);

            }
        });
        mSortables = createSortables();
        mAdapter = new SortListAdapter(MainActivity.this, R.layout.list_item, R.id.user_name, mSortables.toArray(new SortableImpl[]{}));
        // Log.e(TAG, "the adapter has item size: "+mAdapter.getCount());
        mList.setAdapter(mAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        //mList.refreshDrawableState();


    }

    /**
     * Gets an array ofsortables for use by this activity
     *
     * @return An instance of an array list
     */
    private ArrayList<SortableImpl> createSortables() {
        ArrayList<SortableImpl> allSorts = new ArrayList(LIST_NUM);
        long difference = 3600000;
        for (int i = 0; i < LIST_NUM; i++) {
            allSorts.add(createSingleSort(i, difference));
        }

        return allSorts;
    }

    /**
     * Creates a sortable object using the parameters given
     *
     * @param index      The index to use for generating values
     * @param difference The time difference to specify between values
     */
    private SortableImpl createSingleSort(int index, long difference) {
        SortableImpl sort = new SortableImpl();
        long sysTime = System.currentTimeMillis();
        long time = sysTime + (difference * index);
        long modDate = sysTime + (difference * randInt(1, 10));
        sort.numberOfViews = randInt(0, 100);
        sort.modificationDate = new Date(modDate);
        sort.lastLoginDate = new Date(time - (difference * index));
        sort.price = randDouble();
        sort.userName = "User " + index;
        return sort;
    }


    /**
     * Returns a psuedo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimim value
     * @param max Maximim value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    private int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    /**
     * Generates a arandom double for use by this app
     *
     * @return
     */
    private double randDouble() {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        double randomNum = rand.nextDouble() * 100;

        return randomNum;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Sorts the list of object based on the sortIndex and the SortDirection
     */
    public void fireSorter() {

        Collections.sort(mSortables, new SortComparator(sortIndex));
        if (sortDirection == StateTextView.SORT_ASC) {
            Collections.reverse(mSortables);
        }
        mAdapter.setOptions(mSortables.toArray(new SortableImpl[]{}));
        mAdapter.notifyDataSetChanged();
    }

    /**
     *
     */
    public class SortListAdapter extends ArrayAdapter<SortableImpl> {

        private SortableImpl[] options;


        public SortListAdapter(Context context, int resourceId, int textResourceId, SortableImpl[] options) {
            super(context, resourceId, textResourceId, options);
            this.options = options;
            Log.d(TAG, "The options size is: " + options.length);


        }

        /*
        * (non-Javadoc)
        *
        * @see android.widget.ArrayAdapter#getItem(int)
        */
        @Override
        public SortableImpl getItem(int position) {
            if (!isEmpty() && position > -1) {

                return options[position];
            }
            return null;

        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount() {
            return options.length;
        }

        /*
                     * (non-Javadoc)
                     *
                     * @see android.widget.ArrayAdapter#getView(int, android.view.View,
                     * android.view.ViewGroup)
                     */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = super.getView(position, convertView, parent);
            if (rowView == null) {
                LayoutInflater inflater = from(MainActivity.this);
                rowView = inflater.inflate(R.layout.list_item, parent, false);

                TextView modDate = (TextView) rowView.findViewById(R.id.mod_date);
                modDate.setText(options[position].modificationDate.toString());

                TextView lastLogin = (TextView) rowView.findViewById(R.id.last_login);
                lastLogin.setText(options[position].lastLoginDate.toString());

                TextView userName = (TextView) rowView.findViewById(R.id.user_name);
                userName.setText(options[position].userName);

                TextView numViews = (TextView) rowView.findViewById(R.id.num_views);
                numViews.setText(String.valueOf(options[position].numberOfViews));

                TextView price = (TextView) rowView.findViewById(R.id.price);
                price.setText(Double.toString(options[position].price));
                Log.d(TAG, "setting up list index " + position + "with user name: " + userName.getText());
                convertView = rowView;

            }

            return convertView;
        }


        /**
         * Reset the objects associated with this adapter as the sort algorithm may change the psotion of objects
         *
         * @param options
         */
        public void setOptions(SortableImpl[] options) {
            this.options = options;
        }


    }

    ;


    /**
     * A comparator used during the sorting process
     */
    public class SortComparator implements Comparator<Sortable> {

        private int index;


        /**
         * Constructor which accepts a sort index indicating what the sort criteria will be
         *
         * @param index
         */
        public SortComparator(int index) {
            this.index = index;

        }

        /**
         * Compares the two specified objects to determine their relative ordering. The ordering
         * implied by the return value of this method for all possible pairs of
         * {@code (lhs, rhs)} should form an <i>equivalence relation</i>.
         * This means that
         * <ul>
         * <li>{@code compare(a,a)} returns zero for all {@code a}</li>
         * <li>the sign of {@code compare(a,b)} must be the opposite of the sign of {@code
         * compare(b,a)} for all pairs of (a,b)</li>
         * <li>From {@code compare(a,b) > 0} and {@code compare(b,c) > 0} it must
         * follow {@code compare(a,c) > 0} for all possible combinations of {@code
         * (a,b,c)}</li>
         * </ul>
         *
         * @param lhs an {@code Object}.
         * @param rhs a second {@code Object} to compare with {@code lhs}.
         * @return an integer < 0 if {@code lhs} is less than {@code rhs}, 0 if they are
         * equal, and > 0 if {@code lhs} is greater than {@code rhs}.
         * @throws ClassCastException if objects are not of the correct type.
         */
        @Override
        public int compare(Sortable lhs, Sortable rhs) {
            return lhs.compareTo(rhs, index);
        }
    }


}
