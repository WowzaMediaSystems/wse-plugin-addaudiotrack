/**
 * Wowza server software and all components Copyright 2006 - 2015, Wowza Media Systems, LLC, licensed pursuant to the Wowza Media Software End User License Agreement.
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
