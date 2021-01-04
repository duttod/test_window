package karrus.shared.plot;

import java.util.ArrayList;
import java.util.List;

import karrus.shared.hibernate.TtBtItt;
import karrus.shared.hibernate.TtBtStat;
import net.sf.gilead.pojo.gwt.LightEntity;

public class TravelTimePlot extends LightEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private List<TtBtItt> validBtIttList = new ArrayList<TtBtItt>();
	private List<TtBtItt> nonValidBtIttList = new ArrayList<TtBtItt>();
	private List<TtBtStat> btStatList;
	private List<SampleCount> sampleCounts = new ArrayList<SampleCount>();
	

	public TravelTimePlot(){}

	public TravelTimePlot(List<TtBtStat> btStatList, List<TtBtItt> validBtIttList, List<TtBtItt> nonValidBtIttList, List<SampleCount> sampleCounts){
		this.validBtIttList = validBtIttList;
		this.nonValidBtIttList = nonValidBtIttList;
		this.btStatList = btStatList;
		this.sampleCounts = sampleCounts;
	}

	
	public List<TtBtItt> getValidTtBtItts() {
		return validBtIttList;
	}

	public void setValidTtBtItts(List<TtBtItt> validBtIttList) {
		this.validBtIttList = validBtIttList;
	}
	
	public List<TtBtItt> getNonValidTtBtItts() {
		return nonValidBtIttList;
	}

	public void setNonValidTtBtItts(List<TtBtItt> nonValidBtIttList) {
		this.nonValidBtIttList = nonValidBtIttList;
	}
	
	public List<TtBtStat> getTtBtStats() {
		return btStatList;
	}

	public void setTtBtStats(List<TtBtStat> btStatList) {
		this.btStatList = btStatList;
	}

	public List<SampleCount> getSampleCounts() {
		return sampleCounts;
	}

	public void setSampleCounts(List<SampleCount> sampleCounts) {
		this.sampleCounts = sampleCounts;
	}

	public boolean isEmpty(){
		boolean b = (btStatList.size()==0 && validBtIttList.size()==0 && nonValidBtIttList.size()==0);
		if (!b)
			return b;
		return true;

	}

}
