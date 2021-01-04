package karrus.client.service;

import karrus.shared.IhmParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IhmPropertiesServiceAsync {
	
	void getIhmParameters(AsyncCallback<IhmParameters> callback);
}
