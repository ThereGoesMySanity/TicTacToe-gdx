package tgms.ttt.client;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import tgms.ttt.PlatformInterfaces.Interruptible;

public class InterruptDialog extends DialogBox implements Interruptible {
	AsyncCallback<String> callback;
	String user;
	VerticalPanel names;

	public InterruptDialog(String[] users, String user, GameConnectionServiceConnection gcsc) {
		this.user = user;
		this.callback = gcsc.new ChooseUserAsync();
		setText("Choose a user");
		HorizontalPanel opts = new HorizontalPanel();
		opts.add(new Button("Cancel", (ClickEvent ce) -> {
			callback.onSuccess(null);
			InterruptDialog.this.hide();
		}));
		opts.add(new Button("Refresh", (ClickEvent ce) -> gcsc.getUsers()));
		names = new VerticalPanel();
		refresh(users);
		ScrollPanel sp = new ScrollPanel(names);
		VerticalPanel main = new VerticalPanel();
		main.add(sp);
		main.add(opts);
		setWidget(main);
	}

	public void refresh(String[] users) {
		names.clear();
		for(String u : users) {
			if (!u.equals(user)) {
				names.add(new Button(user, (ClickEvent ce) ->  {
					callback.onSuccess(u);
					InterruptDialog.this.hide();
				}));
			}
		}
	}

	@Override
	public void interrupt(int type) {
		this.hide();
	}
}
