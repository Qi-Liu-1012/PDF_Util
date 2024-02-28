package com.lgq.pdf_util.util;


import net.ifok.image.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author liuguoqiang
 * @Date 2024/2/27
 * @Version 1.0
 */
public class ImgUtils {
    public static void main(String[] args) throws Exception {
        File file1 = new File("E:\\download\\浏览器\\mysql.png");
        File file2 = new File("E:\\download\\浏览器\\mysql_out_"
                + UUID.randomUUID()
                + ".ico");
        convertPngToIco(file1, file2, true);
    }

    public static void toIco(File inFile, File outFile) throws Exception {
        BufferedImage pngImg = ImageIO.read(inFile);
        Image iconImage = pngImg.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = icon.createGraphics();
        graphics.drawImage(iconImage, 0, 0, null);
        graphics.dispose();

        ImageIO.write(icon, "ICO", outFile);
    }

    public static void convertPngToIco(File inFile, File outFile, boolean needAlpha) throws Exception {
        String parent = inFile.getParent();
        File tempFile = new File(parent + "\\"
                + UUID.randomUUID()
                + ".png");
        ArrayList<BufferedImage> icons = new ArrayList<>();
        BufferedImage bufferedImage = null;
        if (needAlpha) {
            transferAlpha(inFile, tempFile);
            inFile = tempFile;
            bufferedImage = transferAlpha(inFile, outFile);
            icons.add(bufferedImage);
        }else {
            bufferedImage = ImageIO.read(inFile);
            icons.add(getScaledInstance(bufferedImage, 16, 16));
            icons.add(getScaledInstance(bufferedImage, 32, 32));
            icons.add(getScaledInstance(bufferedImage, 64, 64));
            icons.add(getScaledInstance(bufferedImage, 128, 128));
        }
        ICOEncoder.write(icons, outFile);

        if (tempFile.exists()){
            tempFile.delete();
        }
    }

    public static BufferedImage getScaledInstance(BufferedImage bufferedImage, int toWidth, int toHeight) {
        Image scaledInstance = bufferedImage.getScaledInstance(toWidth, toHeight, Image.SCALE_SMOOTH);
        BufferedImage newBufferedImage = new BufferedImage(toWidth,
                toHeight, BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(scaledInstance, 0, 0, Color.WHITE, null);
        return newBufferedImage;
    }

    public static BufferedImage transferAlpha(File file, File tempFile) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = null;
        InputStream is = null;
        BufferedImage bufferedImage = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            is = new FileInputStream(file);
            // 如果是MultipartFile类型，那么自身也有转换成流的方法：is = file.getInputStream();
            BufferedImage bi = ImageIO.read(is);
            Image image = (Image) bi;
            ImageIcon imageIcon = new ImageIcon(image);
            bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }

                    bufferedImage.setRGB(j2, j1, rgb);

                }
            }

            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", tempFile);// 直接输出文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(byteArrayOutputStream)) {
                byteArrayOutputStream.close();
            }
            if (Objects.nonNull(is)) {
                is.close();
            }
        }
        return bufferedImage;
    }
}
