package karrus.client.service;

import karrus.shared.IhmParameters;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("IhmPropertiesService")
public interface IhmPropertiesService extends RemoteService {

	IhmParameters getIhmParameters() throws Exception;
}
