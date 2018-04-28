package cn.com.egova.examapp;


import cn.com.egova.rankview.annotation.GradeColor;
import cn.com.egova.rankview.annotation.Label;
import cn.com.egova.rankview.annotation.Order;
import cn.com.egova.rankview.annotation.Value;

/**
 * Created by yuanchao on 2018/4/26.
 */

public class RankBO {
    @Order("排名顺序")
    private String order;
    @Label("描述信息")
    private String name;
    @Value("数值项")
    private String value;
    @GradeColor("颜色")
    private int color;

    public RankBO(String order, String name, String value, int color) {
        this.order = order;
        this.name = name;
        this.value = value;
        this.color = color;
    }

    @Override
    public String toString() {
        return "RankBO{" +
                "order='" + order + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", color=" + color +
                '}';
    }
}
