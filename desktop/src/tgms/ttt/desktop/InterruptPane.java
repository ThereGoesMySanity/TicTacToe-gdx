package tgms.ttt.desktop;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tgms.ttt.Net.Socket.GameServerSocket;
import tgms.ttt.PlatformInterfaces.Interruptible;

public class InterruptPane extends JOptionPane implements Interruptible {
	private static final long serialVersionUID = 1L;
	private JDialog dialog;
	private ActionListener li;
	private JPanel buttons;
	private String result = null;
	private GameServerSocket gss;

	class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}

	public InterruptPane(GameServerSocket gss) {
		super("Choose a user", QUESTION_MESSAGE);
		this.gss = gss;
		String[] users = gss.getUsers();
		li = (e) -> {
			result = ((JButton)e.getSource()).getText();
			dialog.dispose();
		};
		this.setLayout(new BorderLayout());
		buttons = new JPanel(new GridLayout(users.length - 1, 1));

		this.add(new JScrollPane(buttons, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		JPanel opts = new JPanel(new GridLayout(1, 2));
		JButton re = new JButton("Refresh");
		JButton cl = new JButton("Close");
		re.addActionListener((e) -> refreshUsers(gss.getUsers()));
		cl.addActionListener((e) -> dialog.dispose());
		opts.add(re);
		opts.add(cl);
		dialog = this.createDialog(null, null);
	}

	public void refreshUsers(String[] users) {
		buttons.removeAll();
		for (String u : users) {
			if (!u.equals(gss.getUser().name)) {
				JButton b = new JButton(u);
				b.addActionListener(li);
				buttons.add(b);
			}
		}
	}

	public String showDialog() {
		dialog.setVisible(true);
		return result;
	}

	@Override
	public void interrupt(int type) {
		dialog.dispose();
	}
}
