package cn.com.egova.rankview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.com.egova.rankview.R;
import cn.com.egova.rankview.annotation.GradeColor;
import cn.com.egova.rankview.annotation.Label;
import cn.com.egova.rankview.annotation.Order;
import cn.com.egova.rankview.annotation.Value;
import cn.com.egova.rankview.bean.RankBean;
import cn.com.egova.rankview.utils.DensityUtils;


/**
 * Created by yuanchao on 2018/4/26.
 */

public class RankView extends View {
    private static final String TAG = "RankView";

    private List<RankBean> rankList = new ArrayList<>();
    private Context mContext;
    //属性
    private int itemRectHeight;                    //每个Item的矩形标识条的高度
    private int itemVeticalSpace;               //每个Item之间的垂直间距
    private int itemHorizontalSpace;           //Item矩形标识条与左右两边的文本的间距
    private int itemLeftTextSize;              //左边文本的大小
    private int itemRightTextSize;             //右边文本的大小
    private int itemBottomTextSize;             //底部文本的大小
    private int itemLeftTextColor;             //左边文本的颜色
    private int itemRightTextColor;            //右边文本的颜色
    private int itemBottomTextColor;           //底部文本的颜色
    private float maxValue;                    //所有item中的value最大值，以它为基准100
    private float maxLeftTextWidth;            //左边文本最大宽度
    private float maxRightTextWidth;           //右边文本最大宽度
    private float bottomTextHeight;           //底部文本的高度

    private Paint paintRect;
    private Paint paintLeft;
    private Paint paintRight;
    private Paint paintBottom;

    public RankView(Context context) {
        this(context, null);
    }

    public RankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initAttrs(attrs);

        initPaints();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.rankView);
        this.itemRectHeight = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemRectHeight, DensityUtils.dp2px(mContext, 30));
        this.itemVeticalSpace = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemVeticalSpace, DensityUtils.dp2px(mContext, 20));
        this.itemHorizontalSpace = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemHorizontalSpace, DensityUtils.dp2px(mContext, 10));
        this.itemLeftTextSize = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemLeftTextSize, DensityUtils.sp2px(mContext, 16));
        this.itemRightTextSize = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemRightTextSize, DensityUtils.sp2px(mContext, 16));
        this.itemBottomTextSize = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemBottomTextSize, DensityUtils.sp2px(mContext, 16));
        this.itemLeftTextColor = typedArray.getColor(R.styleable.rankView_itemLeftTextColor, Color.BLACK);
        this.itemRightTextColor = typedArray.getColor(R.styleable.rankView_itemRightTextColor, Color.BLACK);
        this.itemBottomTextColor = typedArray.getColor(R.styleable.rankView_itemBottomTextColor, Color.BLACK);

    }

    private void initPaints() {
        paintLeft = new Paint();
        paintLeft.setAntiAlias(true);
        paintLeft.setColor(itemLeftTextColor);
        paintLeft.setTextSize(itemLeftTextSize);

        paintRect = new Paint();
        paintRect.setAntiAlias(true);
        paintRect.setColor(Color.RED);
        paintRect.setStyle(Paint.Style.FILL);

        paintRight = new Paint();
        paintRight.setAntiAlias(true);
        paintRight.setColor(itemRightTextColor);
        paintRight.setTextSize(itemRightTextSize);

        paintBottom = new Paint();
        paintBottom.setAntiAlias(true);
        paintBottom.setColor(itemBottomTextColor);
        paintBottom.setTextSize(itemBottomTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        //因为item数量较多的情况下RankView的高度会超过一屏幕高度，故需要重新计算一下。
        int calHSize = (int) (rankList.size() * (itemRectHeight + bottomTextHeight + itemVeticalSpace));
        hSize = hSize > calHSize ? hSize : calHSize;
        setMeasuredDimension(wSize, hSize);
        Log.i(TAG, "onMeasure()--wMode=" + wMode + ",wSize=" + wSize + ",hMode=" + hMode + ",hSize=" + hSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left, top, right, bottom;

        int width = getMeasuredWidth();
        //1.计算各item的value对应的矩形标识条的像素值; 2.返回矩形标识条的最大可用宽度.
        float availableWidth = calculateRectWidth(width);

        for (int i = 0; i < rankList.size(); i++) {
            RankBean bean = rankList.get(i);
            left = 0;
            top = i * (itemRectHeight + bottomTextHeight + itemVeticalSpace);   //item的高度+底部文本的高度+item之间的间距.
            bottom = top + itemRectHeight;

            //1.draw left text
            Paint.FontMetrics leftFontMetrics = paintLeft.getFontMetrics();
            float leftTextBaselineX = left;
            float leftTextBaselineY = top + itemRectHeight / 2 + ((-leftFontMetrics.ascent) - (leftFontMetrics.descent - leftFontMetrics.ascent) / 2); //ascent为负，dscent为正。
            canvas.drawText(bean.getOrder(), leftTextBaselineX, leftTextBaselineY, paintLeft);

            //2.draw rect with specified color
            left = left + maxLeftTextWidth + itemHorizontalSpace;   //这里使用最大文本宽度,是为了保证矩形标识条左对齐.
            right = left + bean.getPxValue();
            paintRect.setColor(bean.getColor());
            canvas.drawRect(left, top, right, bottom, paintRect);

            //3.draw right text
            Paint.FontMetrics rightFontMetrics = paintLeft.getFontMetrics();
            float rightTextBaselineX = left + availableWidth + itemHorizontalSpace;   //使用availableWidth可以保证右边文本左对齐
            float rightTextBaselineY = top + itemRectHeight / 2 + ((-rightFontMetrics.ascent) - (rightFontMetrics.descent - rightFontMetrics.ascent) / 2); //ascent为负，dscent为正。
            canvas.drawText(bean.getValue(), rightTextBaselineX, rightTextBaselineY, paintRight);

            //4.draw bottom text
            float bottomTextBaselineX = left;
            float bottomTextBaselineY = bottom + bottomTextHeight;
            canvas.drawText(bean.getLabel(), bottomTextBaselineX, bottomTextBaselineY, paintBottom);
        }
    }


    /**
     * 注意矩形标识条会根据集合中每个元素的value值按比例显示，而不是直接显示value像素，
     * 因为value可能会很小，也可能会非常大，这两种情况都会导致显示混乱。
     * <p>
     * 思路：
     * 1.以最大的value为100，其他的按比例进行缩放；
     * 2.100对应的像素应该为：RankView自身的宽度-左右两边最大文本宽度-2个水平间距 的剩余可用宽度值。
     *
     * @param width RankView的宽度.
     * @return 最大剩余可用宽度
     */
    private float calculateRectWidth(int width) {
        float availableWidth = width - maxLeftTextWidth - maxRightTextWidth - 2 * itemHorizontalSpace;
        for (RankBean bean : rankList) {
            //如果正常传参数，是不会发生异常的，以防万一parseFloat.
            try {
                float value = Float.parseFloat(bean.getValue());
                float virtualValue = value / maxValue;    //value对于maxValue的占比.
                bean.setPxValue(virtualValue * availableWidth);
            } catch (Exception e) {
            }
        }
        return availableWidth;
    }

    /**
     * 设置数据并刷新界面.
     *
     * @param data
     * @throws IllegalAccessException
     */
    public void setData(List<Object> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        for (Object obj : data) {
            RankBean rankBean = new RankBean();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Order orderAnnatation = field.getAnnotation(Order.class);
                Label labelAnnatation = field.getAnnotation(Label.class);
                Value valueAnnatation = field.getAnnotation(Value.class);
                GradeColor colorAnnatation = field.getAnnotation(GradeColor.class);
                //字段上一个注解也没有，不是我们需要的字段.
                if (orderAnnatation == null && labelAnnatation == null && valueAnnatation == null && colorAnnatation == null) {
                    continue;
                }
                try {
                    //创建实例
                    String fieldValue = field.get(obj).toString();
                    if (orderAnnatation != null) {
                        rankBean.setOrder(fieldValue);
                    }
                    if (labelAnnatation != null) {
                        rankBean.setLabel(fieldValue);
                    }
                    if (valueAnnatation != null) {
                        rankBean.setValue(fieldValue);
                    }
                    if (colorAnnatation != null) {
                        rankBean.setColor(Integer.parseInt(fieldValue));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(rankBean.getOrder()) && !TextUtils.isEmpty(rankBean.getLabel())
                    && !TextUtils.isEmpty(rankBean.getValue()) && rankBean.getColor() != 0) {
                rankList.add(rankBean);
            }
        }


        if (rankList.size() > 0) {
            this.maxValue = getMaxValue();
            float[] widths = getMaxTextWidth();
            this.maxLeftTextWidth = widths[0];
            this.maxRightTextWidth = widths[1];
            this.bottomTextHeight = getBottomTextHeight();

            //刷新界面.
            invalidate();
        }
    }

    /**
     * 获取集合中最大的value值.
     *
     * @return
     */
    public float getMaxValue() {
        float max = -100000000;
        for (RankBean bean : rankList) {
            //如果正常传参数，是不会发生异常的，以防万一
            try {
                float temp = Float.parseFloat(bean.getValue());
                if (temp > max) {
                    max = temp;
                }
            } catch (Exception e) {
            }
        }
        return max;
    }

    /**
     * 获取集合中左右两边文本的最大宽度.
     *
     * @return
     */
    private float[] getMaxTextWidth() {
        float maxLeft = -10000000;
        float maxRight = -10000000;
        for (RankBean bean : rankList) {
            float leftWidth = paintLeft.measureText(bean.getOrder());
            float rightWidth = paintRight.measureText(bean.getValue());
            if (leftWidth > maxLeft) {
                maxLeft = leftWidth;
            }
            if (rightWidth > maxRight) {
                maxRight = rightWidth;
            }
        }
        return new float[]{maxLeft, maxRight};
    }

    /**
     * 获取底部文本的高度
     * 注意：底部文本只能用简短的文本，因为本控件不支持换行.
     *
     * @return
     */
    private float getBottomTextHeight() {
        Paint.FontMetrics fontMetrics = paintBottom.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }
}
