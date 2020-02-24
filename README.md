# AddAudioTrack
The **AddAudioTrack** module for [Wowza Streaming Engine™ media server software](https://www.wowza.com/products/streaming-engine) enables you to add an audio track to a video-only live stream.

## Prerequisites
Wowza Streaming Engine 4.0.0 or later is required.

## Usage
The module creates two streams for every incoming stream connected to the Wowza Streaming Engine live application. The new streams have **-audiodestin** and **-audiodestin-audiosource** appended to the source stream name.

The **-audiodestin** stream contains the original source video and the audio from the file specified by the **addAudioTrackAudioSourceFilename** property. It is the playback stream. Use it in players and to push to a CDN. 

The **-audiodestin-audiosource** stream is used internally and loops continuously while the source stream is active. The audio track is read from this stream. By default, the module looks for **sample.mp4** in the **StorageDir** of the Wowza Streaming Engine application.

## More resources
[Wowza Streaming Engine Server-Side API Reference](https://www.wowza.com/resources/serverapi/)

[How to extend Wowza Streaming Engine using the Wowza IDE](https://www.wowza.com/forums/content.php?759-How-to-extend-Wowza-Streaming-Engine-using-the-Wowza-IDE)

Wowza Media Systems™ provides developers with a platform to create streaming applications and solutions. See [Wowza Developer Tools](https://www.wowza.com/resources/developers) to learn more about our APIs and SDK.

To use the compiled version of this module, see [How to add an audio track to a video-only stream (AddAudioTrack)](https://www.wowza.com/forums/content.php?590-How-to-add-an-audio-track-to-a-video-only-stream-%28ModuleAddAudioTrack%29).

## Contact
[Wowza Media Systems, LLC](https://www.wowza.com/contact)

## License
This code is distributed under the [Wowza Public License](https://github.com/WowzaMediaSystems/wse-plugin-addaudiotrack/blob/master/LICENSE.txt).
