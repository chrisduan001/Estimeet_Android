package estimeet.meetup.ui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by AmyDuan on 15/03/16.
 */
public abstract class CursorRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected boolean mDataValid;
    protected Cursor mCursor;
    protected int mRowIdColumn;

    public CursorRecyclerAdapter(Cursor c) {
        init(c);
    }

    private void init(Cursor c) {
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mRowIdColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("This should only be called when cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, mCursor);
    }

    public abstract void onBindViewHolder(VH holder, Cursor cursor);

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor!= null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        if (hasStableIds() && mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIdColumn);
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyItemRangeRemoved(0, getItemCount());
        }

        return oldCursor;
    }

    /**
     * <p>Converts the cursor into a CharSequence. Subclasses should override this
     * method to convert their results. The default implementation returns an
     * empty String for null values or the default String representation of
     * the value.</p>
     *
     * @param cursor the cursor to convert to a CharSequence
     * @return a CharSequence representing the value
     */
    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }
}
