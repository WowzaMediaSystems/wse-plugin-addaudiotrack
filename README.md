# AddAudioTrack
The **AddAudioTrack** module for [Wowza Streaming Engine™ media server software](https://www.wowza.com/products/streaming-engine) enables you to add an audio track to a video-only live stream.

This repo includes a [compiled version](/lib/wse-plugin-addaudiotrack.jar).

## Prerequisites
Wowza Streaming Engine 4.0.0 or later is required.

## Usage
The module creates two streams for every incoming stream connected to the Wowza Streaming Engine live application. The new streams have **-audiodestin** and **-audiodestin-audiosource** appended to the source stream name.

The **-audiodestin** stream contains the original source video and the audio from the file specified by the **addAudioTrackAudioSourceFilename** property. It is the playback stream. Use it in players and to push to a CDN. 

The **-audiodestin-audiosource** stream is used internally and loops continuously while the source stream is active. The audio track is read from this stream. By default, the module looks for **sample.mp4** in the **StorageDir** of the Wowza Streaming Engine application.

## More resources
To use the compiled version of this module, see [Add an audio track to a video-only stream with a Wowza Streaming Engine Java module](https://www.wowza.com/docs/how-to-add-an-audio-track-to-a-video-only-stream-moduleaddaudiotrack).

[Wowza Streaming Engine Server-Side API Reference](https://www.wowza.com/resources/serverapi/)

[How to extend Wowza Streaming Engine using the Wowza IDE](https://www.wowza.com/docs/how-to-extend-wowza-streaming-engine-using-the-wowza-ide)

Wowza Media Systems™ provides developers with a platform to create streaming applications and solutions. See [Wowza Developer Tools](https://www.wowza.com/developer) to learn more about our APIs and SDK.

## Contact
[Wowza Media Systems, LLC](https://www.wowza.com/contact)

## License
This code is distributed under the [Wowza Public License](/LICENSE.txt).
