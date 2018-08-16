package de.thm.spring.controller;

import de.thm.spring.model.Lichen;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
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

    @RequestMapping(value = "/image/{imageName:.+}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {

        File serverFile = new File(limo_IMAGES_DIR_ABSOLUTE_PATH + imageName);

        return Files.readAllBytes(serverFile.toPath());
    }

    private String createImage(String name, MultipartFile file) {
        try {
            createLiMoImagesDirIfNeeded();
            String path = limo_IMAGES_DIR_ABSOLUTE_PATH + name;
            File image = new File(path);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(image));
            stream.write(file.getBytes());
            stream.close();

            return path;
        } catch (Exception e) {
            return String.format("failed", name, e.getMessage());
        }
    }



    private void createLiMoImagesDirIfNeeded() {
        if (!limo_IMAGES_DIR.exists()) {
            limo_IMAGES_DIR.mkdirs();
        }
    }


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadGet(Model model) {


        model.addAttribute("width", 1132);
        model.addAttribute("imagepath", "/img/bild.jpg");
        model.addAttribute("filename", "Beispiel.jpg");

        model.addAttribute("lichen", Lichen.getInstance().getLichen());

        return "analyze";
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadPost(Model model, @RequestParam("file") MultipartFile file, HttpSession httpSession) {

        String filename = file.getOriginalFilename().replaceAll(" ", "_");
        String path = createImage(filename, file);
        String cmd = "convert " + path +  " -level 1%,95% -sharpen 0x2 "  + path;

        DefaultExecutor exe = new DefaultExecutor();

        int w;
        int h;

        try {
            exe.execute(CommandLine.parse(cmd));

            BufferedImage img = ImageIO.read(new File(path));
            w = img.getWidth();
            h = img.getHeight();

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errormessage", "Die hochgeladene Datei konnte nicht gelesen werden. Die Datei muss im Format .jpg oder .png vorliegen.");
            return "error";
        }

        double f = 800.0/h;
        w *= f;

        model.addAttribute("width", w);

        model.addAttribute("imagepath", "image/" + filename);
        model.addAttribute("filename", filename);
        model.addAttribute("lichen", Lichen.getInstance().getLichen());

        return "analyze";
    }
}
