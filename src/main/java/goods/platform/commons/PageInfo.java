package goods.platform.commons;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 分页
 * @author 孙龙云
 *
 */
import java.util.List;
public class PageInfo implements Serializable{

	private static final long serialVersionUID = 1727384513155705568L;
	private Integer pageNo;
	private Integer pageSize;
	private Long total;
	private Integer pages;
	private List dataList;
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	public List getDataList() {
		return dataList;
	}
	public void setDataList(List list) {
		this.dataList = list;
	}
	private PageInfo(){
	}
	/**
	 * 获取分页对象
	 * @param data
	 * @return
	 */
	public  static <M> PageInfo getPageInfo(List<M> list){
		com.github.pagehelper.PageInfo<M> pageInfo = new com.github.pagehelper.PageInfo<>(list);
		
		PageInfo pInfo = new PageInfo();
		pInfo.setPageNo(pageInfo.getPageNum());
		pInfo.setPageSize(pageInfo.getPageSize());
		pInfo.setPages(pageInfo.getPages());
		pInfo.setTotal(pageInfo.getTotal());
		List<M> ll = new ArrayList<>();
		ll.addAll(pageInfo.getList());
		pInfo.setDataList(ll);
		return pInfo;
	}
	@Override
	public String toString() {
		return "PageInfo [pageNo=" + pageNo + ", pageSize=" + pageSize + ", total=" + total + ", pages=" + pages
				+ ", dataList=" + dataList + "]";
	}
	
}
