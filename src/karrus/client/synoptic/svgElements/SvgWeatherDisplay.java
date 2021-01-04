package karrus.client.synoptic.svgElements;

import java.util.List;

import karrus.shared.hibernate.SynWeatherDisplay;

public class SvgWeatherDisplay extends SvgElement {

	private SynWeatherDisplay synWeatherDisplay;
	
	public SvgWeatherDisplay(SynWeatherDisplay synWeatherDisplay, String item, String synoptic) {
		super(item, synoptic, false);
		this.synWeatherDisplay = synWeatherDisplay;
	}
	
	@Override
	public void onMouseDown() {
	};
	
	public void setTextContent(List<String> values, List<String> units) {
		for (int i = 0; i < values.size(); i++) {
			setText(i, values.get(i), units.get(i));
		}
	}
	
	private native void setText(int childrenIndex, String value, String unity) /*-{
		var svgId = this.@karrus.client.synoptic.svgElements.SvgElement::svgId;
		var svgElement = $doc.getElementById(this.@karrus.client.synoptic.svgElements.SvgElement::htmlObjectId).contentDocument.getElementById(svgId);
		var that = this;
		if (svgElement==null) {
			that.@karrus.client.synoptic.svgElements.SvgElement::errorLog(Ljava/lang/String;)(@karrus.shared.language.Language::nullSvgElementError(Ljava/lang/String;)(this.@karrus.client.synoptic.svgElements.SvgElement::svgId))
			return
		}	
		try {
			svgElement.children[childrenIndex].textContent = svgElement.children[childrenIndex].textContent.replace(/=.+/, '= ' + value + ' ' + unity);
		}	
		catch (err) {
			that.@karrus.client.synoptic.svgElements.SvgElement::errorLog(Ljava/lang/String;)('Too many elements have been defined for the display ' + this.@karrus.client.synoptic.svgElements.SvgElement::svgId);
		}	 
	}-*/;
	
	public SynWeatherDisplay getSynWeatherDisplay() {
		return synWeatherDisplay;
	}
}
