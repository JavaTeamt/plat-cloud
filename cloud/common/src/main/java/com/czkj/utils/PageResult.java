package com.czkj.utils;

import java.util.List;

/**
 * @author SunMin
 * @description 分页封装
 * @create 2020/4/16
 * @since 1.0.0
 */
public class PageResult<T>{
    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 每页显示条数
     */
    private int size;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 当前页显示数据
     */
    private List<T> items;

    /**
     * 总页面
     */
    private int totalPage;

    public PageResult() {
    }

    public PageResult(int currentPage, int size, int totalCount, List<T> items) {
        this.currentPage = currentPage;
        this.size = size;
        this.totalCount = totalCount;
        this.items = items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalPage() {
        return (int)Math.ceil((double) totalCount/size);
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "currentPage=" + currentPage +
                ", size=" + size +
                ", totalCount=" + totalCount +
                ", items=" + items +
                ", totalPage=" + totalPage +
                '}';
    }
}
