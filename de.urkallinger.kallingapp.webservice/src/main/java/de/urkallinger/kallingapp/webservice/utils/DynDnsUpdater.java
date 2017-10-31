package de.urkallinger.kallingapp.webservice.utils;

import java.io.IOException;

public interface DynDnsUpdater extends Runnable {
	public void updateDynDns() throws IOException;
	void run();
}
