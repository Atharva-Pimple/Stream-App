package com.stream.app.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.stream.app.entities.Video;
import com.stream.app.helper.AppConstants;
import com.stream.app.playload.CustomMessage;
import com.stream.app.services.VideoService;

@RestController
@RequestMapping("/api/v1/videos")
@ComponentScan
@CrossOrigin("*")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // uploading video
    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        Video video = new Video();
        video.setVideoId(UUID.randomUUID().toString());
        video.setTitle(title);
        video.setDescription(description);

        Video savedVideo = videoService.saveVideo(video, file);
        if (savedVideo != null) {
            return ResponseEntity.status(HttpStatus.OK).body(savedVideo);
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomMessage.builder()
                            .Message("Error! Video not uploaded")
                            .success(false).build());
        }

    }

    // Streaming video
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(@PathVariable("videoId") String videoId) {

        Video video = videoService.getVideo(videoId);

        String filepath = video.getFilePath();
        String contentType = video.getContentType();

        Resource resource = new FileSystemResource(filepath);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    // get all videos
    @GetMapping
    public List<Video> getAll() {
        return videoService.getAllVideos();
    }

    // get video in small chunks range
    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoRange(
            @PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String range) {
        Video video = videoService.getVideo(videoId);
        Path path = Paths.get(video.getFilePath());
        String contentType = video.getContentType();

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        long fileLength = path.toFile().length();
        long rangeStart = 0;
        long rangeEnd = fileLength - 1;

        if (range != null) {
            String[] ranges = range.replace("bytes=", "").split("-");
            rangeStart = Long.parseLong(ranges[0]);
            // if (ranges.length > 1) {
            // rangeEnd = Long.parseLong(ranges[1]);
            // }
            rangeEnd = Math.min(rangeEnd, rangeStart + AppConstants.CHUNK_SIZE - 1);

        }
        long contentLength = rangeEnd - rangeStart + 1;

        System.out.println("Range start:" + rangeStart);
        System.out.println("Range end:" + rangeEnd);

        InputStream inputStream;

        try {
            inputStream = Files.newInputStream(path);
            inputStream.skip(rangeStart);

            byte[] data=new byte[(int)contentLength];
            inputStream.read(data, 0, data.length);


            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
            headers.add("Accept-Ranges", "bytes");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("X-Content-Type-Options", "nosniff");
            headers.setContentLength(contentLength);

            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.parseMediaType(contentType))
                    .headers(headers)
                    .body(new ByteArrayResource(data));

        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Value("${files.video.hls}")
    private String HLS_DIR;

    @GetMapping("/{videoId}/master.m3u8")
    public ResponseEntity<Resource> serveMasterFile(@PathVariable String videoId){

        Path path=Paths.get(HLS_DIR,videoId,"master.m3u8");
        System.out.println(path);

        if(!Files.exists(path)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resource resource=new FileSystemResource(path);
        return ResponseEntity
            .ok()
            .header(
                HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl"
            )
            .body(resource);
    }

    @GetMapping("/{videoId}/{segment}.ts")
    public ResponseEntity<Resource> serveSegment(
        @PathVariable String videoId,
        @PathVariable String segment
    ){
        Path path=Paths.get(HLS_DIR,videoId,segment+".ts");

        if(!Files.exists(path)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resource resource= new FileSystemResource(path);

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_TYPE, "video/mp2t")
            .body(resource);
    }
}
