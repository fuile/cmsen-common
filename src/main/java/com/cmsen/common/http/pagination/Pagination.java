package com.cmsen.common.http.pagination;

/**
 * 分页
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class Pagination {
    /**
     * 当前页码
     */
    private int pageNo = 1;

    /**
     * 当前页容量
     */
    private int pageSize = 15;

    /**
     * 总页码
     */
    private int pageTotal;

    /**
     * 总记录数
     */
    private int total;
    // private int startIndex;
    // private int endIndex;

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public Pagination setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public Pagination(int total) {
        setTotal(total);
    }

    public Pagination(int total, int pageSize) {
        setPageSize(pageSize);
        setTotal(total);
    }

    public Pagination(int total, int pageSize, int pageNo) {
        setPageNo(pageNo);
        setPageSize(pageSize);
        setTotal(total);
    }

    public Pagination setPageNo(int pageNo) {
        this.pageNo = pageNo <= 0 ? 1 : pageNo;
        // setOraclePaging();
        return this;
    }

    public Pagination setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 1 : pageSize;
        this.pageTotal = (int) Math.ceil(1.0 * total / pageSize);
        // setOraclePaging();
        return this;
    }

    public Pagination setTotal(int total) {
        this.total = total;
        this.pageTotal = (int) Math.ceil(1.0 * total / pageSize);
        return this;
    }

    /**
     * 设置Oracle分页所需参数
     */
    // private void setOraclePaging() {
    //     startIndex = (pageNo - 1) * pageSize;
    //     endIndex = pageNo * pageSize;
    // }
}
