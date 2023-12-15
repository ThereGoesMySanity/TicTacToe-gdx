package tgms.ttt;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.badlogic.gdx.Gdx;

import tgms.ttt.Net.Message;
import tgms.ttt.Net.Socket.GameServerSocket;
import tgms.ttt.PlatformInterfaces.Interruptible;

public class InterruptPane extends JOptionPane implements Interruptible {
	private static final long serialVersionUID = 1L;
	private JDialog dialog;
	private ActionListener li;
	private JPanel buttons;
	private String result = null;
	private GameServerSocket gss;
	public InterruptPane(GameServerSocket gss) {
		super("Choose a user", QUESTION_MESSAGE);
		this.gss = gss;
		li = (e) -> {
			result = ((JButton)e.getSource()).getText();
			dialog.dispose();
		};
		this.setLayout(new BorderLayout());
		buttons = new JPanel(new GridLayout(0, 1));
		this.add(new JScrollPane(buttons, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		JPanel opts = new JPanel(new GridLayout(1, 2));
		JButton re = new JButton("Refresh");
		JButton cl = new JButton("Close");
		re.addActionListener((e) -> gss.updateUsers());
		cl.addActionListener((e) -> dialog.dispose());
		opts.add(re);
		opts.add(cl);
		this.add(opts, BorderLayout.SOUTH);
		dialog = this.createDialog(null, null);
		dialog.setSize(80, 144);
	}

	public void refreshUsers(String[] users) {
		System.out.println(Arrays.toString(users) + "3");
		buttons.removeAll();
		for (String u : users) {
			if (!u.equals(gss.getUser().name)) {
				JButton b = new JButton(u);
				b.addActionListener(li);
				buttons.add(b);
				System.out.println("added button " + b.getText());
			}
		}
		buttons.validate();
	}

	public String showDialog() {
		dialog.setVisible(true);
		return result;
	}

	@Override
	public void interrupt(int type) {
		switch (type) {
		case Message.CONNECT_TO_USER:
			Gdx.app.debug("he", "y");
			dialog.dispose();
			break;
		case Message.GET_USERS:
			refreshUsers(gss.getUsers());
			break;
		}
	}
}
