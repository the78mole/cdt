/**
 * Copyright (c) 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - Initial Implementation
 *
 */
package org.eclipse.cdt.make.xlc.core.scannerconfig;

import org.eclipse.cdt.make.internal.core.scannerconfig2.GCCSpecsRunSIProvider;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.macros.BuildMacroProvider;
import org.eclipse.cdt.managedbuilder.macros.BuildMacroException;
import org.eclipse.cdt.managedbuilder.macros.IBuildMacroProvider;
import org.eclipse.core.runtime.Path;

/**
 * @author laggarcia
 * 
 */
public class XlCSpecsRunSIProvider extends GCCSpecsRunSIProvider {

	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.make.internal.core.scannerconfig2.GCCSpecsRunSIProvider#initialize()
	 */
	protected boolean initialize() {

		boolean rc = super.initialize();

		if (rc) {
			try {
				this.fCompileCommand = new Path(BuildMacroProvider.getDefault()
						.resolveValue(
								this.fCompileCommand.toString(),
								EMPTY_STRING,
								null,
								IBuildMacroProvider.CONTEXT_CONFIGURATION,
								ManagedBuildManager.getBuildInfo(
										this.resource.getProject())
										.getDefaultConfiguration()));
			} catch (BuildMacroException e) {
				e.printStackTrace();
				return false;
			}
		}

		return rc;

	}

}
