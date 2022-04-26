package com.omarserrar.textme.controllers;

import com.omarserrar.textme.models.responses.TempKeyResponse;
import com.omarserrar.textme.models.user.Image;
import com.omarserrar.textme.models.user.exceptions.BadFileTypeException;
import com.omarserrar.textme.services.FileManagerService;
import com.omarserrar.textme.services.UserService;
import com.omarserrar.textme.models.responses.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("api/files")
public class FilesController {
    @Autowired
    public UserService userService;
    @Autowired
    public FileManagerService fileManagerService;
    /*
    TODO getTempKey
     */
    @GetMapping("key")
    private ResponseEntity getTempKey(){
        return ResponseEntity.ok(new TempKeyResponse(fileManagerService.getTempKey()));
    }
    @PostMapping("picture")
    private ResponseEntity deleteContact(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            userService.updatePicture(file);
            return ResponseEntity.noContent().build();
        } catch (BadFileTypeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e));
        }
        catch (Throwable e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e));
        }
    }
    @GetMapping("download/file/{id}")
    private ResponseEntity getFile(@PathVariable(value = "id", required = false) Long id) throws IOException {
        try {
            Image img = fileManagerService.getFile(id);
            if(img == null || img.getData() == null || img.getData().length == 0){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(img.getType())).body(img.getData());
        }
        catch (FileNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        catch (Throwable e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e));
        }
    }
    @GetMapping({"download/picture/{id}", "download/picture"})
    private ResponseEntity getUserPicture(@PathVariable(value = "id", required = false) Long id) throws IOException {
        try {
            Image img;
            if(id == null){
                img = userService.getUserPicture();
            }
            else{
                img = userService.getPicture(id);
            }
            if(img == null || img.getData() == null || img.getData().length == 0){
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                return ResponseEntity.status(HttpStatus.SEE_OTHER).location(new URI("/default.jpg")).build();
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(img.getType())).body(img.getData());
        }
        catch (Throwable e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e));
        }
    }
}
