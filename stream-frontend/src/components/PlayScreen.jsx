import { useLocation } from "react-router-dom"
import VideoPlayer from "./VideoPlayer"

function PlayScreen() {
    const location=useLocation();
    const queryParams=new URLSearchParams(location.search);
    const videoId=queryParams.get("videoId");
    const title=queryParams.get("title");

    const videoSrc=`http://localhost:8080/api/v1/videos/${videoId}/master.m3u8`;
  return (
    <div className="container mx-auto p-4 dark:bg-gray-950">
      <h1 className="text-3xl font-semibold mb-4 dark:text-white ">
        Now Playing: <span className="font-medium text-2xl">{title}</span>
      </h1>
      <div className="mb-4">
        <VideoPlayer src={videoSrc}/>
      </div>
    </div>
  )
}

export default PlayScreen
