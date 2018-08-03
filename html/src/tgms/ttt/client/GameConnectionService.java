package tgms.ttt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import tgms.ttt.Net.ConnectionKernel;
import tgms.ttt.Net.Socket.GameServerKernel;

@RemoteServiceRelativePath("gameConnection")
public interface GameConnectionService extends RemoteService, ConnectionKernel, GameServerKernel {
}