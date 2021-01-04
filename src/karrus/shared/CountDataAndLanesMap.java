package karrus.shared;

import java.util.List;
import java.util.Map;

import karrus.shared.hibernate.CtCountData;
import net.sf.gilead.pojo.gwt.LightEntity;

public class CountDataAndLanesMap extends LightEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> lanesMap;
	private List<CtCountData> countData;
	
	public CountDataAndLanesMap() {
		
	}
	
	public CountDataAndLanesMap(Map<String, String> lanesMap, List<CtCountData> countData) {
		this.lanesMap = lanesMap;
		this.countData = countData;
	}
	
	public Map<String, String> getLanesMap() {
		return this.lanesMap;
	}

	public void setLanesMap(Map<String, String> lanesMap) {
		this.lanesMap = lanesMap;
	}

	public List<CtCountData> getCountData() {
		return this.countData;
	}

	public void setCountData(List<CtCountData> countData) {
		this.countData = countData;
	}
}
