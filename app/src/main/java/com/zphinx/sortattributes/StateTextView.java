/**
 * StateTextView.java
 * Created: 17 Aug 2013 12:20:56
 *
 * @author David Ladapo
 * Copyright (&copy;) 2013
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
 */
package com.zphinx.sortattributes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * A textview with attributes for the associated image state
 *
 * @author David Ladapo Created: 17 Aug 2013 12:20:56
 * @version 1.0 Copyright (&copy;) 2013
 */
public class StateTextView extends android.widget.TextView {

    /**
     * uninitialised state for a textview
     */
    public static final int SORT_UNINITIALIZED = 0;

    /**
     * sort descending state for a textview
     */
    public static final int SORT_DESC = 1;

    /**
     * sort ascending state for a textview
     */
    public static final int SORT_ASC = 2;

    /**
     * An int representing the current sort direction
     */
    private int currentSortDirection = 0;

    /**
     * The array of sort direction values
     */
    public static final int[] STATE_SORT_DIRECTION = {R.attr.state_sort_direction};

    /**
     * Default Constructor
     *
     * @param context The context calling this object
     * @param attrs   The set of attributes avaialable to the textview
     */
    public StateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.sortDirection, 0, 0);

        try {

            currentSortDirection = a.getInteger(R.styleable.sortDirection_state_sort_direction, 0);
        } finally {
            a.recycle();
        }

    }


    /**
     * Android calls this method to know the current drawable state of the view.
     * It starts with an "extraSpace" of 0 in View.java, and each inherited view
     * adds its new state.
     * We add just one more state, hence, we create a new array of size
     * "extraSpace + 1".
     *
     * @param extraSpace
     * @return An array of ints representing the drawable states for the chosen list
     */
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        // Ask the parent to add its default states.
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        // If we are private, add the state to array of states.
        // If not added, the value will be treated as false.
        // mergeDrawableStates() takes care of resolving the duplicates.

        mergeDrawableStates(drawableState, STATE_SORT_DIRECTION);

        // Return the new drawable state.
        return drawableState;
    }

    /**
     * We need a way for the Activity (or some other part of the code) to enable
     * mode changes for the view.
     * <p>
     * If we flip the current state of sortDirection, record the value and
     * inform Android to refresh the drawable state. This will in turn
     * invalidate() the view.
     *
     * @param sortDirection
     */
    public void setSortDirection(Integer sortDirection) {

        this.currentSortDirection = sortDirection;
        refreshDrawableState();

    }

    /**
     * Gets the current sort direction for this view
     *
     * @return An Integer representing the current sort direction
     */
    public Integer getSortDirection() {
        return this.currentSortDirection;
    }

}
