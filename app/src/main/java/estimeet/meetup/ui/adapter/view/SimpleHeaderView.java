package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;

/**
 * Created by AmyDuan on 22/03/16.
 */
@EViewGroup(R.layout.item_list_simple_header)
public class SimpleHeaderView extends LinearLayout {

    @ViewById(R.id.header_text) TextView headerText;

    public SimpleHeaderView(Context context) {
        super(context);
    }

    public void bindHeader(String text) {
        headerText.setText(text);
    }

}
