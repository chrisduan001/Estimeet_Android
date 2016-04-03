package estimeet.meetup.ui.adapter.util;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import estimeet.meetup.ui.adapter.FriendListAdapter;

/**
 * Created by AmyDuan on 15/03/16.
 */
public abstract class CursorRecyclerAdapter extends RecyclerView.Adapter<ViewWrapper> {

    protected boolean mDataValid;
    protected Cursor mCursor;
    protected int mRowIdColumn;
    /**
     position and section name value pair, section includes header
     eg: (0,0) == header section (1,1) first section
     */
    protected HashMap<Integer, Integer> sectionHash;
    protected List<Integer> sectionPos;

    public void setCursor(Cursor c) {
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
    public void onBindViewHolder(ViewWrapper holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("This should only be called when cursor is valid");
        }

        int originPos = position;
        position = getCursorPosition(position);
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        onBindViewHolder(holder, mCursor, originPos);
    }

    public abstract void onBindViewHolder(ViewWrapper holder, Cursor cursor, int position);
    public abstract void buildSectionHash(Cursor cursor);

    //ignore the section when moving the cursor
    protected int getCursorPosition(int position) {
        if (sectionPos != null) {
            int i = 0;
            for (int pos : sectionPos) {
                //position is pointing to section or section item
                if (position > pos) i++;
            }

            return position - i;
        }
        return position;
    }

    protected boolean isSection(int position) {
        return sectionHash != null && (sectionHash.get(position) == FriendListAdapter.FRIEND_HEADER
                || sectionHash.get(position) == FriendListAdapter.SESSION_HEADER);
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor!= null) {
            return sectionPos == null ? mCursor.getCount() : mCursor.getCount() + sectionPos.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        if (hasStableIds() && mDataValid && mCursor != null) {
            int cursorPos = getCursorPosition(position);
            if (mCursor.moveToPosition(cursorPos) && !isSection(position)) {
                return mCursor.getLong(mRowIdColumn);
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void changeCursor(Cursor cursor) {
        if (cursor != null) {
            buildSectionHash(cursor);
        }
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
