import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeadCell,
  TableRow,
} from "flowbite-react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import apiClient from "../api/apiClient";

function Videos() {
  const [videos, setVideos] = useState([]);
  const [loggedIn, setLoggedIn]=useState(false);
  const navigate = useNavigate();

  const handlePlay = (videoId, title) => {
    navigate(`/videos/play?videoId=${videoId}&title=${title}`);
  };

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        let response = await apiClient.get('/api/v1/videos');
        setVideos(response.data);
        setLoggedIn(true);
      } catch (error) {
        console.error("Error while fetching videos", error);
        setLoggedIn(false);
      }
    };
    fetchVideos();
  }, []);
  return (
    <div className="min-h-screen flex flex-col items-center">
      {!loggedIn && <div className="mt-40 max-w-5xl overflow-x-auto flex-grow dark:bg-gray-950">
        <a className="text-center text-blue-600 text-4xl" href="/login">Please Login</a>
      </div>}
      {loggedIn &&
      <Table className="mt-6 max-w-5xl overflow-x-auto flex-grow dark:bg-gray-950">
        <TableHead>
          <TableHeadCell>Video Title</TableHeadCell>
          <TableHeadCell>Description</TableHeadCell>
          <TableHeadCell>
            <span className="sr-only">Video</span>
          </TableHeadCell>
        </TableHead>
        <TableBody className="divide-y">
          {Array.isArray(videos) && videos.map((video) => (
            <TableRow
              key={video.videoId}
              className="bg-white dark:border-gray-700 dark:bg-gray-800"
            >
              <TableCell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
                {video.title}
              </TableCell>
              <TableCell>{video.description}</TableCell>
              <TableCell>
                <button
                  className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                  onClick={() => handlePlay(video.videoId, video.title)}
                >
                  Play
                </button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>}
    </div>
  );
}

export default Videos;
