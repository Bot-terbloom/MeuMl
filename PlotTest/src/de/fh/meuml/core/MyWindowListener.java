package de.fh.meuml.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MyWindowListener implements WindowListener
{
	private static int windowsOpened = 0;
	@Override
	public void windowOpened(WindowEvent arg0)
	{
		windowsOpened++;
	}
	
	@Override
	public void windowIconified(WindowEvent arg0) { }
	
	@Override
	public void windowDeiconified(WindowEvent arg0) { }
	
	@Override
	public void windowDeactivated(WindowEvent arg0) { }
	
	@Override
	public void windowClosing(WindowEvent arg0)
	{
		windowsOpened--;
		if (windowsOpened == 0) {
			System.exit(0);
		}
	}
	
	@Override
	public void windowClosed(WindowEvent arg0) { }
	
	@Override
	public void windowActivated(WindowEvent arg0) { }
}
