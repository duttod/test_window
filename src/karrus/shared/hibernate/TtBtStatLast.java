package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * TtBtStatLast generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class TtBtStatLast extends LightEntity implements java.io.Serializable {

	private TtBtStatLastId id;
	private int sampleSize;
	private int sampleSize5Min;
	private int minTravelTime;
	private int maxTravelTime;
	private int average;
	private float standardDeviation;
	private int decile10;
	private int decile20;
	private int decile30;
	private int decile40;
	private int median;
	private int decile60;
	private int decile70;
	private int decile80;
	private int decile90;

	public TtBtStatLast() {
	}

	public TtBtStatLast(TtBtStatLastId id, int sampleSize, int sampleSize5Min, int minTravelTime, int maxTravelTime,
			int average, float standardDeviation, int decile10, int decile20, int decile30, int decile40, int median,
			int decile60, int decile70, int decile80, int decile90) {
		this.id = id;
		this.sampleSize = sampleSize;
		this.sampleSize5Min = sampleSize5Min;
		this.minTravelTime = minTravelTime;
		this.maxTravelTime = maxTravelTime;
		this.average = average;
		this.standardDeviation = standardDeviation;
		this.decile10 = decile10;
		this.decile20 = decile20;
		this.decile30 = decile30;
		this.decile40 = decile40;
		this.median = median;
		this.decile60 = decile60;
		this.decile70 = decile70;
		this.decile80 = decile80;
		this.decile90 = decile90;
	}

	public TtBtStatLastId getId() {
		return this.id;
	}

	public void setId(TtBtStatLastId id) {
		this.id = id;
	}

	public int getSampleSize() {
		return this.sampleSize;
	}

	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}

	public int getSampleSize5Min() {
		return this.sampleSize5Min;
	}

	public void setSampleSize5Min(int sampleSize5Min) {
		this.sampleSize5Min = sampleSize5Min;
	}

	public int getMinTravelTime() {
		return this.minTravelTime;
	}

	public void setMinTravelTime(int minTravelTime) {
		this.minTravelTime = minTravelTime;
	}

	public int getMaxTravelTime() {
		return this.maxTravelTime;
	}

	public void setMaxTravelTime(int maxTravelTime) {
		this.maxTravelTime = maxTravelTime;
	}

	public int getAverage() {
		return this.average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public float getStandardDeviation() {
		return this.standardDeviation;
	}

	public void setStandardDeviation(float standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public int getDecile10() {
		return this.decile10;
	}

	public void setDecile10(int decile10) {
		this.decile10 = decile10;
	}

	public int getDecile20() {
		return this.decile20;
	}

	public void setDecile20(int decile20) {
		this.decile20 = decile20;
	}

	public int getDecile30() {
		return this.decile30;
	}

	public void setDecile30(int decile30) {
		this.decile30 = decile30;
	}

	public int getDecile40() {
		return this.decile40;
	}

	public void setDecile40(int decile40) {
		this.decile40 = decile40;
	}

	public int getMedian() {
		return this.median;
	}

	public void setMedian(int median) {
		this.median = median;
	}

	public int getDecile60() {
		return this.decile60;
	}

	public void setDecile60(int decile60) {
		this.decile60 = decile60;
	}

	public int getDecile70() {
		return this.decile70;
	}

	public void setDecile70(int decile70) {
		this.decile70 = decile70;
	}

	public int getDecile80() {
		return this.decile80;
	}

	public void setDecile80(int decile80) {
		this.decile80 = decile80;
	}

	public int getDecile90() {
		return this.decile90;
	}

	public void setDecile90(int decile90) {
		this.decile90 = decile90;
	}

	private static final long serialVersionUID = 1L;

}