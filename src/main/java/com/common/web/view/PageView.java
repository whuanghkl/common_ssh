package com.common.web.view;

import java.util.List;
import java.util.Map;

import com.string.widget.util.ValueWidget;

/**
 * 分页值对象
 * User: huangwei
 */
public class PageView {

    /**
     * 当前页数
     */
    protected int currentPage=1;
    /**
     * 每页记录数
     */
    protected int recordsPerPage=10;
    /**
     * 总页数
     */
    protected int totalPages;
    /***
     * 当前页面记录的实际条数
     */
    protected int recordNumOfCurrent;
    /**
     * 总记录数
     */
    protected long totalRecords;
    /**
     * 当前页记录列表,不是泛型,不一定是T,所以不要写死了
     */
    protected List recordList;
    /**
     * 排序的字段
     */
    protected String sortKey;
    /**
     * 排序方式
     */
    protected String ascDesc;

    protected Map<String, String> statusMap;
    /***
     * 分页的标识,是查询,还是上一页或者下一页
     */
    protected String pageFlag;
   

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

   
    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List getRecordList() {
        return recordList;
    }

    public void setRecordList(List recordList) {
        this.recordList = recordList;
    }

    public String getAscDesc() {
        return ascDesc;
    }

    public void setAscDesc(String ascDesc) {
    	if(!ValueWidget.isNullOrEmpty(ascDesc)){
    		if(!(ascDesc.equals("asc")||ascDesc.equals("desc"))){
    			throw new RuntimeException("ascDesc must be either asc or desc");
    		}
    	}
        this.ascDesc = ascDesc;
    }

    public Map<String, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public int getRecordNumOfCurrent() {
		return recordNumOfCurrent;
	}

	public void setRecordNumOfCurrent(int recordNumOfCurrent) {
		this.recordNumOfCurrent = recordNumOfCurrent;
	}

	@Override
	public String toString() {
		return "PageView [currentPage=" + currentPage + ", recordsPerPage="
				+ recordsPerPage + ", totalPages=" + totalPages
				+ ", recordNumOfCurrent=" + recordNumOfCurrent
				+ ", totalRecords=" + totalRecords + ", recordList="
				+ recordList + ", statusMap=" + statusMap + ", pageFlag="
				+ pageFlag + "]";
	}
	
    
}
