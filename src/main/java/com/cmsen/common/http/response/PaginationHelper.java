/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"offset", "rows"}, allowSetters = true)
public class PaginationHelper implements Pagination {
    private int offset;
    private int rows;

    /**
     * 当前页码
     */
    private int index;
    /**
     * 每页大小
     */
    private int size;
    /**
     * 分页总数
     */
    private int total;
    /**
     * 总记录
     */
    private int record;

    public PaginationHelper(int recordTotal) {
        this(recordTotal, 0, 15);
    }

    public PaginationHelper(int recordTotal, int rows) {
        this(recordTotal, 0, rows);
    }

    public PaginationHelper(int recordTotal, int offset, int rows) {
        this.record = recordTotal;
        this.index = pageNumber(offset, 1);
        this.size = pageSize(rows, 0);
        this.total = pageTotal(recordTotal, this.size);
        this.offset = startIndex(this.index, this.size);
        this.rows = this.size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }
}
