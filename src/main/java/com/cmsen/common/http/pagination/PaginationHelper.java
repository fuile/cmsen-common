package com.cmsen.common.http.pagination;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class PaginationHelper {
    private Pagination page;

    public Pagination getPage() {
        return page;
    }

    public PaginationHelper setPage(Pagination page) {
        this.page = page;
        return this;
    }
}
