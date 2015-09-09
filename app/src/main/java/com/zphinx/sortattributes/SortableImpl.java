package com.zphinx.sortattributes;

import java.util.Date;

/**
 * An implementation of the sortable interface used to by this activity.
 * Sortable defines a way for a collection of objects to be sorted using a
 * specified index. Created by rogue on 07/09/15.
 */
public class SortableImpl implements Sortable {

    public Date modificationDate;
    public String userName;
    public Date lastLoginDate;
    public double price;
    public int numberOfViews;
    public int currentSortIndex = 0;

    public static final int SORT_BY_MODIFICATION = 1;

    public static final int SORT_BY_LOGIN = 2;
    public static final int SORT_BY_NAME = 3;
    public static final int SORT_BY_VIEWS = 4;
    public static final int SORT_BY_PRICE = 5;

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another
     *            the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     *         a positive integer if this instance is greater than
     *         {@code another}; 0 if this instance has the same order as
     *         {@code another}.
     * @throws ClassCastException
     *             if {@code another} cannot be converted into something
     *             comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Object another) {

        return compareTo(another, currentSortIndex);
    }

    /**
     * Compares this object to the specified object using the index passed to it
     * in order to determine their relative order.
     *
     * @param another
     *            the object to compare to this instance.
     * @param index
     *            An index used to specify the property which will determine the
     *            comparator
     * @return a negative integer if this instance is less than {@code another};
     *         a positive integer if this instance is greater than
     *         {@code another}; 0 if this instance has the same order as
     *         {@code another}.
     * @throws ClassCastException
     *             if {@code another} cannot be converted into something
     *             comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Object another, int index) {
        int compareValue = 0;
        if (!(another instanceof SortableImpl)) {
            throw new RuntimeException("Unable to process objects which are not sortable instances");
        } else {

            SortableImpl sortable = (SortableImpl) another;
            switch (index) {
                case SORT_BY_MODIFICATION:
                    compareValue = this.modificationDate.compareTo(sortable.modificationDate);
                    break;
                case SORT_BY_LOGIN:
                    compareValue = this.lastLoginDate.compareTo(sortable.lastLoginDate);
                    break;
                case SORT_BY_NAME:
                    compareValue = this.userName.compareTo(sortable.userName);
                    break;
                case SORT_BY_VIEWS:
                    compareValue = (this.numberOfViews == sortable.numberOfViews)
                            ? 0
                            : (this.numberOfViews > sortable.numberOfViews)
                            ? 1
                            : -1;
                    break;
                case SORT_BY_PRICE:
                    compareValue = Double.compare(this.price, sortable.price);
                default:
                    break;

            }
        }
        return compareValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SortableImpl))
            return false;

        SortableImpl sortable = (SortableImpl) o;

        if (Double.compare(sortable.price, price) != 0)
            return false;
        if (numberOfViews != sortable.numberOfViews)
            return false;
        if (currentSortIndex != sortable.currentSortIndex)
            return false;
        if (!modificationDate.equals(sortable.modificationDate))
            return false;
        if (!userName.equals(sortable.userName))
            return false;
        return lastLoginDate.equals(sortable.lastLoginDate);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = modificationDate.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + lastLoginDate.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + numberOfViews;
        return result;
    }
}