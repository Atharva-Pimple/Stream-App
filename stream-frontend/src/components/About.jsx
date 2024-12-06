import { Card } from "flowbite-react";
import AppLogo from "../assets/play-button.png";

function About() {
  return (
    <div className="flex justify-center space-y-3 mt-6">
      <Card className=" bg-gray-200 max-w-4xl mx-auto">
        <h1 className="flex space-x-3 text-3xl text-gray-700 tracking-tight dark:text-white font-semibold sm:text-2xl">
          <span>About</span>
          <img src={AppLogo} style={{ height: "40px", width: "40px" }}></img>
          <span className="text-3xl text-red-600">Video Stream App</span>
        </h1>
        <p className="font-normal text-gray-700 dark:text-gray-400">
          This application is a video streaming platform that enables users to
          upload and stream videos seamlessly. Built with a React frontend and a
          Spring Boot backend, it leverages FFmpeg to process videos into
          adaptive segments for resolutions of 360p, 480p, and 1080p. This
          ensures an optimized viewing experience across different devices and
          internet speeds. The app includes features for video upload, dynamic
          streaming, and playback with resolution switching for user
          convenience.
        </p>
        <p className=" text-gray-500">
          <strong>Developed by:</strong> Atharva Pimple
        </p>
      </Card>
    </div>
  );
}

export default About;
