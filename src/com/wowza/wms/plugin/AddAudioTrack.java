/*
 * This code and all components (c) Copyright 2006 - 2016, Wowza Media Systems, LLC. All rights reserved.
 * This code is licensed pursuant to the Wowza Public License version 1.0, available at www.wowza.com/legal.
*/
package com.wowza.wms.plugin;

import java.util.HashMap;
import java.util.Iterator;

import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.plugin.AddAudioLogger;
import com.wowza.wms.plugin.AddAudioWorker;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamActionNotify;

public class AddAudioTrack extends ModuleBase
{

	class StreamListener implements IMediaStreamActionNotify
	{
		@Override
		public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend)
		{
			if (!streamName.contains("-audiodestin") && !streamName.contains("-audiosource"))
			{
				synchronized(workers)
				{
					if (workers.containsKey(streamName))
					{
						AddAudioWorker ThisBucket = workers.remove(streamName);
						ThisBucket.quit();
					}

					AddAudioWorker ThisWorker = new AddAudioWorker(appInstance, log);
					ThisWorker.setSourceStream(streamName);
					ThisWorker.setDestinStream(streamName + "-audiodestin");
					ThisWorker.SetAudioSourceFile(audioSourceFile);
					ThisWorker.setDaemon(true);
					ThisWorker.start();
					workers.put(streamName, ThisWorker);
				}
			}
		}

		@Override
		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend)
		{
			synchronized(workers)
			{
				if (workers.containsKey(streamName))
				{
					AddAudioWorker ThisBucket = workers.remove(streamName);
					ThisBucket.quit();
				}
			}
		}

		@Override
		public void onPause(IMediaStream stream, boolean isPause, double location)
		{
		}

		@Override
		public void onPlay(IMediaStream stream, String streamName, double playStart, double playLen, int playReset)
		{
		}

		@Override
		public void onSeek(IMediaStream stream, double location)
		{
		}

		@Override
		public void onStop(IMediaStream stream)
		{
		}
	}

	private AddAudioLogger log = null;
	private AddAudioWorker worker = null;
	private String sourceStream = "";
	private HashMap<String, AddAudioWorker> workers = new HashMap<String, AddAudioWorker>();
	private IApplicationInstance appInstance = null;
	private IMediaStreamActionNotify actionNotify = new StreamListener();
	private String audioSourceFile = "mp4:sample.mp4";

	public void onAppStart(IApplicationInstance appInstance)
	{
		String fullname = appInstance.getApplication().getName() + "/" + appInstance.getName();

		this.log = new AddAudioLogger(WMSLoggerFactory.getLoggerObj(appInstance));
		this.log.LogMessage("Started: " + fullname);

		this.audioSourceFile = appInstance.getProperties().getPropertyStr("addAudioTrackAudioSourceFilename", "mp4:sample.mp4");

		this.appInstance = appInstance;

	}

	public void onAppStop(IApplicationInstance appInstance)
	{
		String fullname = appInstance.getApplication().getName() + "/" + appInstance.getName();

		synchronized(this.workers)
		{
			Iterator<String> currentWorkers = this.workers.keySet().iterator();
			while (currentWorkers.hasNext())
			{
				String workerStream = currentWorkers.next();
				AddAudioWorker thisWorker = this.workers.get(workerStream);
				thisWorker.quit();
			}
		}
		this.log.LogMessage("Stopped: " + fullname);
		this.log = null;

	}

	public AddAudioWorker returnVideoWorker()
	{
		return this.worker;
	}

	public void onStreamCreate(IMediaStream stream)
	{
		stream.addClientListener(actionNotify);
	}

	public void onStreamDestroy(IMediaStream stream)
	{
		stream.removeClientListener(actionNotify);
	}
}