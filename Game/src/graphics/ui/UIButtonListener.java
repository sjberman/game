package graphics.ui;

public class UIButtonListener {

	public void entered(UIButton button) {
		button.setColor(0xCDCDCD);
	}

	public void exited(UIButton button) {
		button.setColor(0xAAAAAA);
	}

	public void pressed(UIButton button) {
		button.setColor(0xCC2222);
		button.ignoreNextPress();
	}

	public void released(UIButton button) {
		button.setColor(0xCDCDCD);
	}
}
