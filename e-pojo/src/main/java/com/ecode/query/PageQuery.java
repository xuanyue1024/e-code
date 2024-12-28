package com.ecode.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "分页查询实体")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = -4369849971849494143L;

    @ApiModelProperty(value = "页码", required = true)
    private Long pageNo;
    @ApiModelProperty(value = "每页记录数", required = true)
    private Long pageSize;
    @ApiModelProperty(value = "排序字段")
    private String sortBy;
    @ApiModelProperty("是否升序")
    private Boolean isAsc;

    public <T>  Page<T> toMpPage(OrderItem... orders){
        // 1.分页条件
        Page<T> p = Page.of(pageNo, pageSize);
        // 2.排序条件
        // 2.1.先看前端有没有传排序字段
        if (sortBy != null) {
            p.addOrder(new OrderItem(sortBy, isAsc));
            return p;
        }
        // 2.2.再看有没有手动指定排序字段
        if(orders != null){
            p.addOrder(orders);
        }
        return p;
    }

    public <T> Page<T> toMpPage(String defaultSortBy, boolean isAsc){
        return this.toMpPage(new OrderItem(defaultSortBy, isAsc));
    }

    public <T> Page<T> toMpPageDefaultSortByCreateTimeDesc() {
        return toMpPage("create_time", false);
    }

    public <T> Page<T> toMpPageDefaultSortByUpdateTimeDesc() {
        return toMpPage("update_time", false);
    }

    /**
     * 根据订单项参数生成带有排序条件的分页对象
     * 如果订单项参数为空且sortBy字段也为null，则生成一个默认按更新时间降序排序的分页对象
     *
     * @param orders 可变参数，包含一个或多个OrderItem对象，用于指定排序条件
     * @param <T> 泛型参数，表示分页对象中包含的元素类型
     * @return 返回一个带有排序条件的分页对象
     */
    public <T>  Page<T> nullToDefault(OrderItem... orders){
        // 检查订单项参数是否为空以及sortBy字段是否为null，以决定是否使用默认排序
        if (orders == null && sortBy == null){
            // 当订单项参数和sortBy字段都为空时，返回一个默认按更新时间降序排序的分页对象
            return toMpPageDefaultSortByUpdateTimeDesc();
        }else {
            // 当订单项参数不为空时，根据传入的订单项参数生成并返回相应的分页对象
            return toMpPage(orders);
        }
    }

}