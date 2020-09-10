package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @PostMapping("/file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) throws IOException {
        byte[] fileData = fileUpload.getInputStream().readAllBytes();
        String fileName = fileUpload.getOriginalFilename();
        String contentType = fileUpload.getContentType();
        String fileSize = Long.toString(fileUpload.getSize());
        String errorMessage = null;
        File newFile = null;

        if(fileData.length == 0) {
            errorMessage = "Please select a file to upload.";
        } else {
            if(fileService.isFilenameUsed(fileName, authentication.getName())) {
                errorMessage = "Failed to upload file. Duplicated file name is not allowed.";
            } else {
                Integer userId = userService.getUserByUserName(authentication.getName()).getUserId();
                newFile = new File(null, fileName, contentType, fileSize, userId, fileData);
                Integer rowsAdded = fileService.storeNewFile(newFile);

                if(rowsAdded == 0) {
                    errorMessage = "Failed to upload file. Please try again later.";
                }
            }
        }

        if(errorMessage == null) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("activeTab", "files");

        return "result";
    }

    @GetMapping("/file/{fileId}")
    public @ResponseBody void getFile(@PathVariable(value="fileId") Integer fileId, Authentication authentication, HttpServletResponse response) throws IOException {
        File file = fileService.getFile(fileId);
        User currentLoggedInUser = userService.getUserByUserName(authentication.getName());

        response.reset();
        if(file == null ) {
            response.setStatus(404);
        } else if(file.getUserId() != currentLoggedInUser.getUserId()) {
            response.setStatus(403);
        } else {
            response.setContentType(file.getContentType());
            response.setHeader("Content-Disposition", "filename=" + file.getFileName());
            response.getOutputStream().write(file.getFileData());
        }
    }

    @GetMapping("/file-remove/{fileId}")
    public String removeFile(@PathVariable(value="fileId") Integer fileId, Authentication authentication, Model model) {
        User currentLoggedInUser = userService.getUserByUserName(authentication.getName());
        String errorMessage = null;

        Integer rowsRemoved = fileService.removeFile(fileId, currentLoggedInUser.getUserId());

        if(rowsRemoved == 0) {
            errorMessage = "Something wrong happened. Failed to remove file.";
        }

        if(errorMessage == null) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailed", errorMessage);
        }
        model.addAttribute("activeTab", "files");

        return "result";
    }
}
