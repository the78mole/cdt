/*******************************************************************************
 * Copyright (c) 2016 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.dsf.gdb.internal.ui.console;

import org.eclipse.cdt.debug.ui.debuggerconsole.IDebuggerConsole;
import org.eclipse.cdt.dsf.gdb.internal.service.IGDBFocusSynchronizer;
import org.eclipse.cdt.dsf.gdb.internal.ui.GdbUIPlugin;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.ui.DebugUITools;

/**
 * GDB specifics to IDebuggerConsole e.g. default implementations
 */
public interface IGDBDebuggerConsole extends IDebuggerConsole {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.debug.ui.debuggerconsole.IDebuggerConsole#consoleSelected()
	 */
	@Override
	public default void consoleSelected() {
		DsfSession session = ((GdbLaunch)getLaunch()).getSession();
		if (!session.isActive()) {return;}

		// only trigger the DV selection if the current selection is in
		// a different session
		IAdaptable context = DebugUITools.getDebugContext();
		if (context != null) {
			ILaunch selectedLaunch =  context.getAdapter(ILaunch.class);
			if (getLaunch().equals(selectedLaunch)) {
				return;
			}
		}

		session.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				DsfServicesTracker tracker = new DsfServicesTracker(GdbUIPlugin.getBundleContext(), session.getId());
				IGDBFocusSynchronizer gdbSync = tracker.getService(IGDBFocusSynchronizer.class);
				tracker.dispose();
				if (gdbSync != null) {
					gdbSync.sessionSelected();
				}
			}
		});
	}

	/**
	 * Stop processing but don't dispose this console yet, 
	 * i.e. It's desirable to keep the last I/O information available to the user
	 */
	default void stop() {
		// Nothing to do by default
	}
}
