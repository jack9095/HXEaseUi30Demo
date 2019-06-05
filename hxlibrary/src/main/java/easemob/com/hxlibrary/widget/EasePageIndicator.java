package easemob.com.hxlibrary.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import easemob.com.hxlibrary.R;
import easemob.com.hxlibrary.utils.DisplayUtils;

import static com.baidu.location.d.a.i;

/**
 * Created by fei.wang on 2019/5/4.
 */

public class EasePageIndicator extends LinearLayout {

    private static final String TAG = EasePageIndicator.class.getSimpleName();
    private ArrayList<View> indicators = new ArrayList<>();
    private int checkedPosition = 0;

    public EasePageIndicator(Context context) {
        super(context);
    }

    public EasePageIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EasePageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EasePageIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setup(int num){
        int count = num;
        if (num < 0) {
            count = 0;
        }

        int delta = count - indicators.size();
        if (delta > 0) { // add indicator
            for (int i = 0; i < delta; i++) {
                indicators.add(createIndicator());
            }
        } else { // remove indicator from end

//            for (int j = 0; j < indicators.size() - 1; j++) {
//                indicators.remove(j);
//                removeViewAt(j);
//            }
        }
    }

   public void setItemChecked(int position) {
        if (position >= indicators.size() || position < 0) {
            return;
        }

        if (checkedPosition > -1 && checkedPosition < indicators.size()) {
            indicators.get(checkedPosition).setSelected(false);
        }
        checkedPosition = position;
        indicators.get(checkedPosition).setSelected(true);
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    private View createIndicator() {
        View indicator = new View(getContext());
        LinearLayout.LayoutParams lp = (LayoutParams) new ViewGroup.LayoutParams(DisplayUtils.dp2px(getContext(), 16f), DisplayUtils.dp2px(getContext(), 4f));
        indicator.setLayoutParams(lp);
        indicator.setBackgroundResource(R.drawable.em_indicator_selector);
        addView(indicator);
        int margin = DisplayUtils.dp2px(getContext(), 5f);
        lp.setMargins(margin, 0, margin, 0);
        indicator.requestLayout();
        return indicator;
    }
}
