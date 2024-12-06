import { useEffect, useRef } from 'react'
import videojs from 'video.js'
import Hls from 'hls.js'
import "video.js/dist/video-js.css"
import { Toast } from 'flowbite-react';

// eslint-disable-next-line react/prop-types
function VideoPlayer({src}) {
    const videoRef=useRef(null);
    const playerRef=useRef(null);

    useEffect(()=>{
        playerRef.current=videojs(videoRef.current,{
            controls:true,
            autoplay:true,
            muted:true,
            preload:"auto",
        });

        if(Hls.isSupported()){
            const hls=new Hls();
            hls.loadSource(src);
            hls.attachMedia(videoRef.current);
            hls.on(Hls.Events.MANIFEST_PARSED, ()=>{
                videoRef.current.play();
            })
        }else if(videoRef.current.canPlayType("application/vnd.apple.mpegurl")){
            videoRef.current.loadSource(src);
            videoRef.current.addEventListener('canplay',()=>{
                videoRef.current.play();
            })
        }else{
            console.error("Video format not supported");
            Toast.error("Video format not supported")
        }


    },[src]);
  return (
    <div>
      <div data-vjs-player>
        <video ref={videoRef}
        className="video-js vjs-control-bar vjs-default-skin w-full">

        </video>
      </div>
    </div>
  )
}

export default VideoPlayer
