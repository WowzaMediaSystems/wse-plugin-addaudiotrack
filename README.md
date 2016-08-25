# wse-plugin-addaudiotrack
The **ModuleAddAudioTrack** module for [Wowza Streaming Engine™ media server software](https://www.wowza.com/products/streaming-engine) enables you to add an audio track to a live stream that does not already have audio.

## Prerequisites
Wowza Streaming Engine 4.0.0 or later is required.

## Usage
The module creates two additional streams for every input stream connected to the application. The new streams created are suffixed with "-audiodestin" and "-audiodestin-audiosource." For example, in the input stream named myStream, additional new streams will be created named myStream-audiodestin and myStream-audiodestin-audiosource. 


## More resources
[Wowza Streaming Engine Server-Side API Reference](https://www.wowza.com/resources/WowzaStreamingEngine_ServerSideAPI.pdf)

[How to extend Wowza Streaming Engine using the Wowza IDE](https://www.wowza.com/forums/content.php?759-How-to-extend-Wowza-Streaming-Engine-using-the-Wowza-IDE)

Wowza Media Systems™ provides developers with a platform to create streaming applications and solutions. See [Wowza Developer Tools](https://www.wowza.com/resources/developers) to learn more about our APIs and SDK.

To use the compiled version of this module, see [How to add an audio track to a video only stream (ModuleAddAudioTrack)](https://www.wowza.com/forums/content.php?590-How-to-add-an-audio-track-to-a-video-only-stream-(ModuleAddAudioTrack)).

## Contact
[Wowza Media Systems, LLC](https://www.wowza.com/contact)

## License
This code is distributed under the [Wowza Public License](https://github.com/WowzaMediaSystems/wse-plugin-addaudiotrack/blob/master/LICENSE.txt).

![alt tag](http://wowzalogs.com/stats/githubimage.php?plugin=wse-plugin-addaudiotrack)