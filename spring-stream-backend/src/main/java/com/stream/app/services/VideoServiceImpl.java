package com.stream.app.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.stream.app.entities.Video;
import com.stream.app.repositories.VideoRepository;

import jakarta.annotation.PostConstruct;

import java.io.*;
import java.nio.file.*;

@Service
public class VideoServiceImpl implements VideoService{

    private Logger logger=LoggerFactory.getLogger(VideoServiceImpl.class);

    @Value("${files.video}")
    private String dir;

    @Value("${files.video.hls}")
    private String HLS_DIR;

    private VideoRepository repository;
    
    public VideoServiceImpl(VideoRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init(){
        File file=new File(dir);

        try{
            Files.createDirectories(Paths.get(HLS_DIR));
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
        if(!file.exists()){
            file.mkdir();
            logger.info("File Created");
        }else{
            logger.info("File already exist");
        }
    }

    @Override
    public Video saveVideo(Video video, MultipartFile file) {
        try{
            String filename=file.getOriginalFilename();
            String contentType=file.getContentType();
            InputStream inputStream=file.getInputStream();

            String cleanFilename=StringUtils.cleanPath(filename);
            String cleanFolder=StringUtils.cleanPath(dir);

            Path path=Paths.get(cleanFolder,cleanFilename);


            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            video.setContentType(contentType);
            video.setFilePath(StringUtils.cleanPath(path.toString()));

            Video savedVideo=repository.save(video);
            
            processVideo(video.getVideoId());

            return savedVideo;

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        
        
    }

    @Override
    public Video getVideo(String videoId) {
        return repository.findById(videoId).orElseThrow(()->new RuntimeException("Video not found"));
    }

    @Override
    public Video getByTitle(String title) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByTitle'");
    }

    @Override
    public List<Video> getAllVideos() {
        return repository.findAll();
    }

    @Override
    public String processVideo(String videoId) {
        Video video=this.getVideo(videoId);
        String filePath=video.getFilePath();

        Path videoPath=Paths.get(filePath);

        // String output360p=HLS_DIR + videoId + "/360p/";
        // String outpu720p=HLS_DIR + videoId + "/720p/";
        // String output1080p=HLS_DIR + videoId + "/1080p/";

        try{
            // Files.createDirectories(Paths.get(output360p));
            // Files.createDirectories(Paths.get(outpu720p));
            // Files.createDirectories(Paths.get(output1080p));

            Path outputPath=Paths.get(HLS_DIR,videoId);
            Files.createDirectories(outputPath);

            String ffmpegCmd=String.format(
                "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\" \"%s/master.m3u8\"",
                videoPath,outputPath,outputPath
            );

            // StringBuilder ffmpegCmd=new StringBuilder("");
            // ffmpegCmd.append("ffmpeg -i")
            //         .append(videoPath.toString()).append(" ")
            //         .append("-map 0:v -map 0:a -s:v:0 640x360 -b:v:0 800k ")
            //         .append("-map 0:v -map 0:a -s:v:1 1280x720 -b:v:1 2800k ")
            //         .append("-map 0:v -map 0:a -s:v:2 1920x1080 -b:v:2 5000k ")
            //         .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
            //         .append("-master_pl_name ").append(HLS_DIR).append(videoId).append("/master.m3u8 ")
            //         .append("-f hls -hls_time 10 -hls_list_size 0")
            //         .append("-hls_segment_filename \"").append(HLS_DIR).append(videoId);
            
            System.out.println(ffmpegCmd);
            // ProcessBuilder processBuilder=new ProcessBuilder("/bin/bash","-c", ffmpegCmd);
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process=processBuilder.start();
            int exit=process.waitFor();
            if(exit!=0){
                throw new RuntimeException("Video processing failed");
            }

            return videoId;


        }catch(IOException e){
            throw new RuntimeException("Video Processing failed");
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

    }

}
