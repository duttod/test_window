package karrus.client.synoptic.svgElements;

import com.allen_sauer.gwt.log.client.Log;

public abstract class SvgElement {

	protected String svgId;
	protected String htmlObjectId;
	private boolean isHoverable;

	public SvgElement(String svgId, String htmlObjectId, boolean isHoverable) {
		this.svgId = svgId;
		this.htmlObjectId = htmlObjectId;
		this.isHoverable = isHoverable;
	}
	
	public native void jsInit() /*-{
		var svgDoc = $doc.getElementById(this.@karrus.client.synoptic.svgElements.SvgElement::htmlObjectId).contentDocument;
    	var svgElement = svgDoc.getElementById(this.@karrus.client.synoptic.svgElements.SvgElement::svgId);
    	var that = this;
    	if (svgElement==null) {
    		that.@karrus.client.synoptic.svgElements.SvgElement::errorLog(Ljava/lang/String;)(@karrus.shared.language.Language::nullSvgElementError(Ljava/lang/String;)(this.@karrus.client.synoptic.svgElements.SvgElement::svgId))
    		return
    	}	
    	function1 = function() {
				that.@karrus.client.synoptic.svgElements.SvgElement::onMouseDown()();
		}
		function2 = function() {
				svgElement.style.cursor='pointer'; svgElement.style.opacity=0.5;
		}
		function3 = function() {
				svgElement.style.cursor='default'; svgElement.style.opacity=1;
		}		
    	svgElement.addEventListener("mousedown", function1, false);
    	if (this.@karrus.client.synoptic.svgElements.SvgElement::isHoverable) {
			svgElement.addEventListener("mouseover", function2, false);
			svgElement.addEventListener("mouseout", function3, false);
    	}	
	}-*/;
	
	abstract void onMouseDown();

	public native void setColor(String color) /*-{
		var svgId = this.@karrus.client.synoptic.svgElements.SvgElement::svgId;
		var svgElement = $doc.getElementById(this.@karrus.client.synoptic.svgElements.SvgElement::htmlObjectId).contentDocument.getElementById(svgId);
		var that = this;
		if (svgElement==null) {
    		that.@karrus.client.synoptic.svgElements.SvgElement::errorLog(Ljava/lang/String;)(@karrus.shared.language.Language::nullSvgElementError(Ljava/lang/String;)(this.@karrus.client.synoptic.svgElements.SvgElement::svgId))
    		return
    	}	
		svgElement.style.fill=color
	}-*/;
	
	public native void setTitle(String title) /*-{
		var svgId = this.@karrus.client.synoptic.svgElements.SvgElement::svgId;
		var svgElement = $doc.getElementById(this.@karrus.client.synoptic.svgElements.SvgElement::htmlObjectId).contentDocument.getElementById(svgId);
		var that = this;
		if (svgElement==null) {
			that.@karrus.client.synoptic.svgElements.SvgElement::errorLog(Ljava/lang/String;)(@karrus.shared.language.Language::nullSvgElementError(Ljava/lang/String;)(this.@karrus.client.synoptic.svgElements.SvgElement::svgId))
			return
		}	
		var titleTag = $doc.createElementNS('http://www.w3.org/2000/svg','title');
		titleTag.textContent=title;
		titleTag.innerHTML=title;
		svgElement.appendChild(titleTag);
	}-*/;
	
	private void errorLog(String message) {
		Log.error(message);
	}
	
	public String getSvgId() {
		return svgId;
	}

}
