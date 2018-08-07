package tgms.ttt.desktop;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import tgms.ttt.PlatformInterfaces.Interruptible;

public class InterruptPane extends JOptionPane implements Interruptible {
	private static final long serialVersionUID = 1L;
	private JDialog dialog;
	
	public InterruptPane(String message) {
		super(message, QUESTION_MESSAGE);
		setWantsInput(true);
		dialog = this.createDialog(null, null);
	}
	
	public String showDialog() {
		dialog.setVisible(true);
		return (String)super.getInputValue();
	}
	
	@Override
	public void interrupt(int type) {
		dialog.dispose();
	}
}
