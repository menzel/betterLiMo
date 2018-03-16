package de.thm.spring.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class UploadController {

    private static final String limo_IMAGES = "limo";
    private static final String TOMCAT_HOME_PROPERTY = "catalina.home";
    private static final String TOMCAT_HOME_PATH = System.getProperty(TOMCAT_HOME_PROPERTY);
    private static final String limo_IMAGES_PATH = TOMCAT_HOME_PATH + File.separator + limo_IMAGES;

    private static final File limo_IMAGES_DIR = new File(limo_IMAGES_PATH);
    private static final String limo_IMAGES_DIR_ABSOLUTE_PATH = limo_IMAGES_DIR.getAbsolutePath() + File.separator;

    @RequestMapping(value = "/image/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {

        File serverFile = new File(limo_IMAGES_DIR_ABSOLUTE_PATH+ imageName + ".jpg");

        return Files.readAllBytes(serverFile.toPath());
    }




    private String createImage(String name, MultipartFile file) {
        try {
            createPizzaImagesDirIfNeeded();
            File image = new File(limo_IMAGES_DIR_ABSOLUTE_PATH + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(image));
            stream.write(file.getBytes());
            stream.close();

            return name;
        } catch (Exception e) {
            return String.format("failed", name, e.getMessage());
        }
    }



    private void createPizzaImagesDirIfNeeded() {
        if (!limo_IMAGES_DIR.exists()) {
            limo_IMAGES_DIR.mkdirs();
        }
    }


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadGet(Model model) {

        model.addAttribute("imagepath", "bild.jpg");

        return "analyze";
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadTrack(Model model, @RequestParam("file") MultipartFile file, HttpSession httpSession) {

        String path = createImage(file.getOriginalFilename(), file);


        model.addAttribute("imagepath", path);
        return "analyze";
    }
}
