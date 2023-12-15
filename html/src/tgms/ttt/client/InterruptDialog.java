package tgms.ttt.client;
import com.badlogic.gdx.Gdx;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import tgms.ttt.PlatformInterfaces.Interruptible;

public class InterruptDialog extends DialogBox implements Interruptible {
	String width;
	AsyncCallback<String> callback;
	String user;
	VerticalPanel names;
	HorizontalPanel opts;

	public InterruptDialog(String[] users, GameConnectionServiceConnection gcsc) {
		this.user = gcsc.getUser().name;
		this.callback = gcsc.new ChooseUserAsync();
		setText("Choose a user");
		opts = new HorizontalPanel();
		opts.add(new Button("Refresh", (ClickEvent ce) -> gcsc.getUsers()));
		opts.add(new Button("Cancel", (ClickEvent ce) -> {
			callback.onSuccess(null);
			InterruptDialog.this.hide();
		}));
		names = new VerticalPanel();
		ScrollPanel sp = new ScrollPanel(names);
		VerticalPanel main = new VerticalPanel();
		main.add(sp);
		main.add(opts);
		setWidget(main);
		refresh(users);
	}

	public void refresh(String[] users) {
		width = opts.getElement().getClientWidth() + "px";
		names.clear();
		ClickHandler ch = (ClickEvent ce) ->  {
			callback.onSuccess(((Button)ce.getSource()).getText());
			InterruptDialog.this.hide();
		};
		Gdx.app.debug("get user", "done");
		for(String u : users) {
			if (!u.equals(user)) {
				Button b = new Button(u, ch);
				if (!width.equals("0px")) b.setWidth(width);
				names.add(b);
			}
		}
	}

	@Override
	public void interrupt(int type) {
		Gdx.app.debug("heye", "html");
		this.hide();
	}
}
