package com.web.blog.controller.file;


import com.web.blog.model.BasicResponse;
import com.web.blog.model.file.Image;
import com.web.blog.model.file.repository.ImageRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    ImageRepository imageRepository;

    @RequestMapping("/upload")
    public Object upload(@RequestPart List<MultipartFile> files){
        BasicResponse result = new BasicResponse();
        String currentPath = System.getProperty("user.dir");
        String path = currentPath + "\\src\\main\\resources\\static\\img\\";
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        String time = format.format(System.currentTimeMillis());
        for(MultipartFile file : files) {
            Image image = new Image();
            String fileName = file.getOriginalFilename();
            String iid = time + fileName;
            path = path + iid;
            System.out.println(path);
            File dest = new File(path);

            try {
                file.transferTo(dest);
                image.setIid(iid);
                image.setIname(fileName);
                image.setPath("static/img/"+iid);
                imageRepository.save(image);

                result.data = "success";
                result.status = true;
                result.object = image;

            } catch (Exception e) {
                result.data = "fail";
                result.status = false;
                e.printStackTrace();
                return new ResponseEntity<>(result, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
