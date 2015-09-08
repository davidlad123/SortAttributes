/**
 * SortDialogManager.java
 * Created: 8 Feb 2013 20:36:26
 *
 * @author David Ladapo
 * Copyright (&copy;) 2011  Zphinx Software Solutions
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * License for more details.
 * <p>
 * THERE IS NO WARRANTY FOR THIS SOFTWARE, TO THE EXTENT PERMITTED BY
 * APPLICABLE LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING BY ZPHINX SOFTWARE SOLUTIONS
 * AND/OR OTHER PARTIES WHO PROVIDE THIS SOFTWARE "AS IS" WITHOUT WARRANTY
 * OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.  THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM
 * IS WITH YOU.  SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF
 * ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 * <p>
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING
 * WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR CONVEYS
 * THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY
 * GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE
 * USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF
 * DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD
 * PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS),
 * EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGES.
 * <p>
 * For further information, please go to http://www.zphinx.co.uk/
 */
package com.zphinx.sortattributes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;


/**
 * Manages the dialog for the Sort dialog used by this app.
 *
 * @author David Ladapo Created: 8 Feb 2013 20:36:26
 * @version 1.0 Copyright (&copy;) 2011 Zphinx Software Solutions
 */
public class SortDialogManager {

    /**
     * The result type to send
     */

    private AlertDialog alertDialog = null;


    private int selectedIndex = -1;


    private static final String TAG = SortDialogManager.class.getSimpleName();


    /**
     * Initializes and displays the alert dialog hosting the list of StateTextView objects
     *
     * @param activity - The activity which uses this dialog
     */
    public void showAlertDialog(final Activity activity) {
        String title = null;
        Log.d(TAG, "The alertDialog is: " + alertDialog);
        if (alertDialog == null) {
            Resources res = activity.getResources();
            String[] sortStrings = res.getStringArray(R.array.searchSortValues);
            title = "Sort Search Results";


            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
            ListAdapter adapter = new SortAdapter(activity, R.layout.spinner_sort_list, sortStrings);

            builder.setSingleChoiceItems(adapter, -1, createItemListener(activity, adapter));

            alertDialog = builder.create();
            // Setting Dialog Title
            alertDialog.setTitle(title);

            // Setting alert dialog icon

            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);

            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", sortListener(adapter, activity));

        }
        alertDialog.setOwnerActivity(activity);

        // Showing Alert Message
        alertDialog.show();
    }


    /**
     * An adapter used by this dialog manager to parse its list of StateTextview objects
     *
     * @author David Ladapo Created: 17 Aug 2013 15:38:48
     * @version 1.0 Copyright (&copy;) 2013
     */
    private static class SortAdapter extends ArrayAdapter<StateTextView> {

        private int textResourceId = 0;
        private String[] options = null;

        static StateTextView[] stateTextViews = null;

        /**
         * Default Constructor
         *
         * @param context
         * @param textResourceId
         * @param options
         */
        public SortAdapter(Context context, int textResourceId, String[] options) {
            super(context, textResourceId);
            this.textResourceId = textResourceId;
            this.options = options;
            stateTextViews = new StateTextView[options.length];
            notifyDataSetChanged();

        }

        /**
         * @author David Ladapo Created: 22 Aug 2013 23:01:40
         * @version 1.0 Copyright (&copy;) 2013
         */

        private class ViewHolder {
            @SuppressWarnings("unused")
            public StateTextView stateView = null;

        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.ArrayAdapter#getCount()
         */
        @Override
        public int getCount() {

            return options.length;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.ArrayAdapter#getItem(int)
         */
        @Override
        public StateTextView getItem(int position) {
            if (!isEmpty() && position > -1) {

                return stateTextViews[position];
            }
            return null;

        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.ArrayAdapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (stateTextViews[position] == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final StateTextView rowView = (StateTextView) inflater.inflate(this.textResourceId, parent, false);
                rowView.setText(options[position]);
                ViewHolder vhs = new ViewHolder();
                vhs.stateView = rowView;
                rowView.setTag(vhs);
                stateTextViews[position] = rowView;

            }
            return stateTextViews[position];
        }
    }


    /**
     * Creates an ItemListener used by the List adapter
     *
     * @param activity - The activity which uses this dialog
     * @param adapter  - The adapter associated with the list of state textview objects
     * @return A instance of an onClickListener used by the stated list adapter
     */
    private OnClickListener createItemListener(final Activity activity, final ListAdapter adapter) {
        return new OnClickListener() {

            /*
             * (non-Javadoc)
             *
             * @see
             * android.content.DialogInterface.OnClickListener#onClick(android
             * .content.DialogInterface, int)
             */
            @Override
            public void onClick(DialogInterface dialog, int position) {
                clickStateTextView(activity, adapter, position);

            }
        };

    }

    /**
     * Sets the image to be shown by the drawable associated with the StateTextview
     *
     * @param text     The state textview object whose image is to be set
     * @param draws    The array of drawables used by the textview
     * @param activity - The activity which uses this dialog
     * @throws NotFoundException If an error occurs
     */

    private static void setCurrentImage(final StateTextView text, Drawable[] draws, Activity activity) throws NotFoundException {
        Resources res = activity.getResources();
        Integer curState = text.getSortDirection();
        final Integer newState = (curState == StateTextView.SORT_UNINITIALIZED)
                ? StateTextView.SORT_DESC
                : (curState == StateTextView.SORT_DESC)
                ? StateTextView.SORT_ASC
                : StateTextView.SORT_DESC;

        switch (newState) {
            case StateTextView.SORT_DESC:
                draws[2] = ResourcesCompat.getDrawable(res, R.drawable.icon_bottom_arrow, null);
                break;
            case StateTextView.SORT_ASC:
                draws[2] = ResourcesCompat.getDrawable(res, R.drawable.icon_top_arrow, null);
                break;
            default:
                draws[2] = null;
                break;
        }

        Log.d(TAG, "The drawable to draw is: ........................." + draws[2] + " The oldState is: " + curState + " The new state is: " + newState);
        Rect bounds = new Rect(0, 0, draws[2].getIntrinsicWidth(), draws[2].getIntrinsicHeight());
        draws[2].setBounds(bounds);
        text.setCompoundDrawables(null, null, draws[2], null);
        text.setSortDirection(newState);

    }


    /**
     * A listener used to fire the sorting action promoted by this dialog
     *
     * @param adapter The adapter associated with the list of state textview objects
     * @return A click listener used to sort data in the associated activity
     */
    private OnClickListener sortListener(final ListAdapter adapter, final Activity activity) {
        return new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                StateTextView stv = (StateTextView) adapter.getItem(selectedIndex);
                Log.d(TAG, "The stateView is: ........................." + stv);
                if (stv != null) {
                    // currently not checking for changes??
                    int direction = stv.getSortDirection();
                    MainActivity.sortIndex = selectedIndex;
                    MainActivity.sortDirection = direction;
                    ((MainActivity) activity).fireSorter();
                }
                alertDialog.dismiss();
            }
        };
    }

    /**
     * dismiss this alert dialog
     */
    public void onStop() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    /**
     * Specifies the actions that ovccur when a stateTextView object is clicked
     *
     * @param activity The activity hosting this dialogManager
     * @param adapter  The list adapter containing multiple stateview
     * @param position The position of the stateview object been clicked
     * @throws NotFoundException If the specified state textview is not available
     */
    private void clickStateTextView(final Activity activity, final ListAdapter adapter, int position) throws NotFoundException {
        if (position > -1) {
            int count = adapter.getCount();

            for (int i = 0; i < count; i++) {
                if (i != position) {
                    StateTextView anon = (StateTextView) adapter.getItem(i);
                    if ((anon != null)) {
                        anon.setCompoundDrawables(null, null, null, null);
                        anon.setSortDirection(StateTextView.SORT_UNINITIALIZED);
                    }
                }
            }
            StateTextView rowView = (StateTextView) adapter.getItem(position);
            Drawable[] draws = rowView.getCompoundDrawables();
            if (draws == null) {
                draws = new Drawable[4];
            }

            Log.d(TAG, "The stateView is: ........................." + rowView);
            Log.d(TAG, "The drawable is is: ........................." + draws[2]);
            setCurrentImage(rowView, draws, activity);
            selectedIndex = position;
        }
    }
}
