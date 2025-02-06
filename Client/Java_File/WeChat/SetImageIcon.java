package WeChat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;

public class SetImageIcon {
    public static void SetIcon(File file, JButton button, int width, int height) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = bufferedImage.getScaledInstance
                    (width, height, Image.SCALE_SMOOTH);
            Icon icon = new ImageIcon(image);
            button.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SetIcon(File path, JButton button, int width, int height, String name)  {
        File file = new File(path + "/"  + name + ".png");
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = bufferedImage.getScaledInstance
                    (width, height, Image.SCALE_SMOOTH);
            Icon icon = new ImageIcon(image);
            button.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SetIcon(String path, JButton button, int width, int height, String name)  {
        InputStream fileIn = null;
        try {
            String s = System.getProperty("user.dir");
            ZipFile zipFile = new ZipFile(s + "/nice.jar", Charset.forName("GBK"));
            fileIn = zipFile.getInputStream(zipFile.getEntry(path + name + ".png"));
        } catch (IOException e) {
            System.out.println("zipError: " + e.getMessage());
        }

        try {
            BufferedImage bufferedImage = null;
            if (fileIn != null) bufferedImage = ImageIO.read(fileIn);
            Image image = null;
            if (bufferedImage != null)
                image = bufferedImage.getScaledInstance
                        (width, height, Image.SCALE_SMOOTH);
            Icon icon = null;
            if (image != null) icon = new ImageIcon(image);
            button.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SetIcon(BufferedImage bufferedImage, JButton button, int width, int height) {
        if (bufferedImage != null) {
            Image image = bufferedImage.getScaledInstance
                    (width, height, Image.SCALE_SMOOTH);
            Icon icon = new ImageIcon(image);
            button.setIcon(icon);
        }
    }
    public static BufferedImage getBufferedImage(BufferedImage bufferedImage) {
        BufferedImage newBufferedImage;
        newBufferedImage= new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        //TYPE_INT_RGB:jpg
        //TYPE_3BYTE_BGR: 为gif图
        //TYPE_INT_ARGB_PRE:为png
        newBufferedImage.createGraphics().drawImage(
                bufferedImage, 0, 0, Color.WHITE, null);
        return newBufferedImage;
    }
    public static BufferedImage getBufferedImage(byte[] bufferedImage_byte) {
        BufferedImage bufferedImage = null;
        ByteArrayInputStream image_in = new ByteArrayInputStream(bufferedImage_byte);
        try {
            bufferedImage = ImageIO.read(image_in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}
