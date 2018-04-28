package cn.com.egova.rankview.bean;

/**
 * Created by yuanchao on 2018/4/26.
 */

public class RankBean {
    private String order;
    private String label;
    private String value;
    private float pxValue;
    private int color;

    public RankBean() {
    }

    public RankBean(String order, String label, String value) {
        this.order = order;
        this.label = label;
        this.value = value;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public float getPxValue() {
        return pxValue;
    }

    public void setPxValue(float pxValue) {
        this.pxValue = pxValue;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "RankBean{" +
                "order='" + order + '\'' +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", pxValue=" + pxValue +
                ", color=" + color +
                '}';
    }
}
