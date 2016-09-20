/*
 * This code and all components (c) Copyright 2006 - 2016, Wowza Media Systems, LLC. All rights reserved.
 * This code is licensed pursuant to the Wowza Public License version 1.0, available at www.wowza.com/legal.
*/
package com.wowza.wms.plugin;

import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerIDs;

public class AddAudioLogger
{

	private WMSLogger logger = null;

	public AddAudioLogger(WMSLogger Log)
	{
		logger = Log;
	}

	public void LogMessage(String message)
	{

		logger.info("AudioAdd: '" + message + "'", WMSLoggerIDs.CAT_application, WMSLoggerIDs.EVT_comment);

	}

}
