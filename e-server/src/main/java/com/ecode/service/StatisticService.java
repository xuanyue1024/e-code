package com.ecode.service;


import com.ecode.vo.HistogramVO;

/**
 * <p>
 *  数量服务类
 * </p>
 *
 * @author 竹林听雨
 * @since 2025-1-2
 */
public interface StatisticService {


    /**
     * 得到每道题分数
     *
     * @return 直方图签证官
     */
    HistogramVO getScore(Integer classId);

}
