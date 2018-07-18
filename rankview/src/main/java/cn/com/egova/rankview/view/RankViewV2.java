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

public class RankViewV2 extends View {
    private static final String TAG = "RankViewV2";

    private List<RankBean> rankList = new ArrayList<>();
    private Context mContext;
    //属性
    private int itemRectHeight;                    //每个Item的矩形标识条的高度
    private int itemRectColor;                     //每个Item的矩形标识条的颜色
    private int itemRectUnderColor;                 //每个item的衬底色
    private int itemVeticalSpace;                  //每个Item之间的垂直间距
    private int itemTopSpace;                      //Item顶部文本与矩形条之间的垂直间距
    private int itemTextSize;                      //左右两边文本的大小
    private int itemLeftTextColor;                 //左边文本的颜色
    private int itemRightTextColor;                //右边文本的颜色
    private float maxValue;                        //所有item中的value最大值，以它为基准100
    private float topTextHeight;                   //顶部文本的高度

    private Paint paintRect;
    private Paint paintLeft;
    private Paint paintRight;
    private Paint paintRectUnder; //底色

    public RankViewV2(Context context) {
        this(context, null);
    }

    public RankViewV2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initAttrs(attrs);

        initPaints();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.rankView);
        this.itemRectHeight = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemRectHeight, DensityUtils.dp2px(mContext, 30));
        this.itemRectColor = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemRectColor, Color.GREEN);
        this.itemRectUnderColor = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemRectUnderColor, Color.parseColor("#E3E4E5"));
        this.itemVeticalSpace = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemVeticalSpace, DensityUtils.dp2px(mContext, 20));
        this.itemTopSpace = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemTopSpace, DensityUtils.dp2px(mContext, 10));
        this.itemTextSize = typedArray.getDimensionPixelOffset(R.styleable.rankView_itemTextSize, DensityUtils.sp2px(mContext, 16));
        this.itemLeftTextColor = typedArray.getColor(R.styleable.rankView_itemLeftTextColor, Color.BLACK);
        this.itemRightTextColor = typedArray.getColor(R.styleable.rankView_itemRightTextColor, Color.BLACK);
    }

    private void initPaints() {
        paintLeft = new Paint();
        paintLeft.setAntiAlias(true);
        paintLeft.setColor(itemLeftTextColor);
        paintLeft.setTextSize(itemTextSize);

        paintRight = new Paint();
        paintRight.setAntiAlias(true);
        paintRight.setColor(itemRightTextColor);
        paintRight.setTextSize(itemTextSize);

        paintRect = new Paint();
        paintRect.setAntiAlias(true);
        paintRect.setColor(itemRectColor);
        paintRect.setStyle(Paint.Style.FILL);

        paintRectUnder = new Paint();
        paintRectUnder.setAntiAlias(true);
        paintRectUnder.setColor(itemRectUnderColor);
        paintRectUnder.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        //因为item数量较多的情况下RankView的高度会超过一屏幕高度，故需要重新计算一下。注意，最后一个item不需要底部的垂直间距.
        int calHSize = (int) (rankList.size() * (topTextHeight + itemTopSpace + itemRectHeight + itemVeticalSpace)) - itemVeticalSpace;
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
            top = i * (topTextHeight + this.itemTopSpace + itemRectHeight + itemVeticalSpace) + topTextHeight;   //顶部文本的高度+item的高度+item之间的间距.

            //1.draw left text
            float leftTextBaselineX = left;
            float leftTextBaselineY = top;
            canvas.drawText(bean.getLabel(), leftTextBaselineX, leftTextBaselineY, paintLeft);

            //2.draw right text
            float rightTextWidth = paintRight.measureText(bean.getValue());
            float rightTextBaselineX = left + availableWidth - rightTextWidth;   //保证右边文本右对齐
            float rightTextBaselineY = top;
            canvas.drawText(bean.getValue(), rightTextBaselineX, rightTextBaselineY, paintRight);

            //3.draw rect under with specified color
            right = left + availableWidth;
            top = top + this.itemTopSpace;
            bottom = top + itemRectHeight;
            canvas.drawRect(left, top, right, bottom, paintRectUnder);

            //4.draw rect with specified color
            right = left + bean.getPxValue();
            paintRect.setColor(bean.getColor() == 0 ? itemRectColor : bean.getColor());
            canvas.drawRect(left, top, right, bottom, paintRect);
        }
    }


    /**
     * 注意矩形标识条会根据集合中每个元素的value值按比例显示，而不是直接显示value像素，
     * 因为value可能会很小，也可能会非常大，这两种情况都会导致显示混乱。
     * <p>
     * 思路：
     * 1.以最大的value为100，其他的按比例进行缩放；
     * 2.100对应的像素应该为：RankView自身的宽度。
     *
     * @param width RankView的宽度.
     * @return 最大剩余可用宽度
     */
    private float calculateRectWidth(int width) {
        for (RankBean bean : rankList) {
            //如果正常传参数，是不会发生异常的，以防万一parseFloat.
            try {
                float value = Float.parseFloat(bean.getValue());
                float virtualValue = value / maxValue;    //value对于maxValue的占比.
                bean.setPxValue(virtualValue * width);
            } catch (Exception e) {
            }
        }
        return width;
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
        this.rankList.clear();   //每次setData时均clear一下.
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
                    && !TextUtils.isEmpty(rankBean.getValue())) {
                rankList.add(rankBean);
            }
        }


        if (rankList.size() > 0) {
            this.maxValue = getMaxValue();
            this.topTextHeight = getTopTextHeight();

            //刷新界面,因为我们需要重新走onMeasure(),故调用一下requestLayout(),但它不保证会调用onDraw(),故还需要invalidate()
            requestLayout();
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
     * 获取顶部文本的高度
     * 顶部文本，左边和右边默认大小一样，故高度也一样。
     *
     * @return
     */
    private float getTopTextHeight() {
        Paint.FontMetrics fontMetrics = paintLeft.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }
}
