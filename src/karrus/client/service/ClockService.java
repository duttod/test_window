package karrus.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ClockService")
public interface ClockService extends RemoteService
{
    void start();
}
