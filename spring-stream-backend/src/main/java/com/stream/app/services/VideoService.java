package com.stream.app.services;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.stream.app.entities.Video;

public interface VideoService {

    Video saveVideo(Video video, MultipartFile file);
    Video getVideo(String videoId);
    Video getByTitle(String title);
    List<Video> getAllVideos();

    // processing video
    String processVideo(String videoId);    
}
