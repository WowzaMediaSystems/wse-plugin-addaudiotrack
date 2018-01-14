/*
 * This code and all components (c) Copyright 2006 - 2018, Wowza Media Systems, LLC. All rights reserved.
 * This code is licensed pursuant to the Wowza Public License version 1.0, available at www.wowza.com/legal.
 */
package com.wowza.wms.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.wowza.wms.amf.AMFPacket;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.publish.Playlist;
import com.wowza.wms.stream.publish.Publisher;
import com.wowza.wms.stream.publish.Stream;

public class AddAudioWorker extends Thread
{
	private boolean quit = false;
	private int sleepTime = 1000;
	private IApplicationInstance appInstance = null;
	private String video = null;
	private String destinStream = null;
	private boolean firstAudio = true;
	private boolean firstVideo = true;
	private AddAudioLogger log = null;
	private long fillASequence = 0L;
	private long fillVSequence = 0L;
	private Publisher publisher = null;
	private Stream audioSource = null;
	private String audioFile = "mp4:sample.mp4";

	public AddAudioWorker(IApplicationInstance appins, AddAudioLogger log)
	{
		this.appInstance = appins;
		this.log = log;
	}

	public void setSourceStream(String sourceName)
	{
		this.video = sourceName;
	}

	public void setDestinStream(String destinName)
	{
		this.destinStream = destinName;
	}

	public void SetAudioSourceFile(String audioFileName)
	{
		this.audioFile = audioFileName;
	}

	public void SetVideoResources()
	{
		this.publisher = Publisher.createInstance(this.appInstance);
		this.publisher.setStreamType(appInstance.getStreamType());
		this.publisher.publish(this.destinStream);

		this.audioSource = Stream.createInstance(this.appInstance, this.destinStream + "-audiosource");
		Playlist playlist = new Playlist("audiosource");
		playlist.setRepeat(true);
		playlist.addItem(this.audioFile, 0, -1);
		playlist.open(this.audioSource);

	}

	public synchronized void quit()
	{
		this.quit = true;
		interrupt();
	}

	public synchronized void setSleepTime(int sleepTime)
	{
		this.sleepTime = sleepTime;
	}

	public void run()
	{
		boolean firstRun = true;
		long offsetTimeCode = 0L;

		try
		{
			Thread.currentThread().sleep(6000);
		}
		catch (Exception e)
		{
			this.log.LogMessage("Source stream shut down early");
		}
			
		synchronized(this)
		{
			if (this.quit)
				return;
		}
		
		SetVideoResources();

		while (true)
		{
			HashMap<Long, AMFPacket> audioPackets = new HashMap<Long, AMFPacket>();
			HashMap<Long, AMFPacket> videoPackets = new HashMap<Long, AMFPacket>();
			HashMap<Long, Boolean> packetList = new HashMap<Long, Boolean>();

			try
			{
				Thread.currentThread().sleep(this.sleepTime);
			}
			catch (Exception e)
			{
			}

			synchronized(this)
			{
				if (this.quit)
				{
					this.log.LogMessage("Source Stream shut down");
					break;
				}
			}

			IMediaStream audioSource = null;
			IMediaStream videoSource = null;

			try
			{
				audioSource = this.appInstance.getStreams().getStream(this.destinStream + "-audiosource");
			}
			catch (Exception e)
			{
			}

			try
			{
				videoSource = this.appInstance.getStreams().getStream(this.video);
			}
			catch (Exception e)
			{
			}

			if (audioSource != null)
			{
				List<AMFPacket> packetsFiller = audioSource.getPlayPackets();
				Iterator<AMFPacket> packetsFillerIter = packetsFiller.iterator();
				AMFPacket thisPacket = null;

				if (packetsFiller.size() > 0)
				{
					while (packetsFillerIter.hasNext())
					{
						thisPacket = packetsFillerIter.next();
						if (thisPacket == null)
							continue;

						long newSequence = thisPacket.getSeq();

						if (newSequence > this.fillASequence)
						{

							if (thisPacket.isAudio())
							{
								audioPackets.put(thisPacket.getAbsTimecode(), thisPacket);
								packetList.put(thisPacket.getAbsTimecode(), true);
							}
							this.fillASequence = thisPacket.getSeq();
						}
					}
				}

			}

			if (videoSource != null)
			{
				List<AMFPacket> packetsFillerV = videoSource.getPlayPackets();
				Iterator<AMFPacket> packetsFillerIterV = packetsFillerV.iterator();
				AMFPacket thisPacket = null;
				if (packetsFillerV.size() > 0)
				{
					while (packetsFillerIterV.hasNext())
					{
						thisPacket = packetsFillerIterV.next();
						if (thisPacket == null)
							continue;

						long newSequence = thisPacket.getSeq();

						if (newSequence > this.fillVSequence)
						{

							if (thisPacket.isVideo())
							{
								long timecode = 0L;
								timecode = thisPacket.getAbsTimecode();
								if (firstRun == true)
								{
									offsetTimeCode = timecode;
									firstRun = false;
								}
								videoPackets.put(timecode, thisPacket);
								packetList.put(timecode, true);
							}
							this.fillVSequence = thisPacket.getSeq();
						}
					}
				}

			}

			if (packetList.size() > 0)
			{

				List<Long> mySet = new ArrayList<Long>();
				Iterator<Long> packetsFillerIterA = packetList.keySet().iterator();
				while (packetsFillerIterA.hasNext())
				{
					Long thisNum = packetsFillerIterA.next();
					mySet.add(thisNum);
				}

				Collections.sort(mySet);

				for (int a = 0; a < mySet.size(); a++)
				{
					Long thisCode = mySet.get(a);
					if (audioPackets.containsKey(thisCode))
					{
						AMFPacket aPacket = audioPackets.get(thisCode);
						if (this.firstAudio == true)
						{
							AMFPacket configPacket = audioSource.getAudioCodecConfigPacket(aPacket.getAbsTimecode());
							if (configPacket != null)
								this.publisher.addAudioData(configPacket.getData(), configPacket.getSize(), (configPacket.getAbsTimecode() + offsetTimeCode));
							this.firstAudio = false;
						}
						this.publisher.addAudioData(aPacket.getData(), aPacket.getData().length, (aPacket.getAbsTimecode() + offsetTimeCode));
					}

					if (videoPackets.containsKey(thisCode))
					{
						AMFPacket vPacket = videoPackets.get(thisCode);
						if (this.firstVideo == true)
						{
							AMFPacket configPacket = videoSource.getVideoCodecConfigPacket(vPacket.getAbsTimecode());
							this.publisher.addVideoData(configPacket.getData(), configPacket.getSize(), configPacket.getAbsTimecode());
							this.firstVideo = false;
						}
						this.publisher.addVideoData(vPacket.getData(), vPacket.getData().length, vPacket.getAbsTimecode());
					}
				}
			}
		}
		if (this.audioSource != null)
		{
			this.audioSource.close();
		}
		this.audioSource = null;
		if (this.publisher != null)
		{
			this.publisher.unpublish();
			this.publisher.close();
		}
		this.publisher = null;
	}
}
