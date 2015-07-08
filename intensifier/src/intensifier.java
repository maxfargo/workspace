import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.util.Iterator;

public class intensifier {
    static BufferedImage img;
    static private int commonHeight;
    static private int commonWidth;
    static BufferedImage frame1;
    static BufferedImage frame2;
    static BufferedImage frame3;
    static BufferedImage frame4;
    static int speed;
    static double intensity;
//    private int subtitleHeight;
//    private int subtitleWidth;
    private static int mode;
    private static Color subColor;
    private static Color tintColor;
    
    public static void main(String[] args) throws java.io.IOException{
//        String input;
        int frame2Offset;
        int frame2OffsetHeight;
        Boolean tint = false;
        speed = 20;
        intensity = 0.98;
        String filename = "intensifier.gif";
        String subtitle = "";
        mode = 0;
        subColor = Color.yellow;
        tintColor = Color.red;
        BufferedImage mask;
        float burn = 0.5f;
        Boolean growBurn = false;
        int growth = 20;
        for(int ii = 0; ii < args.length; ii++){
            if(args[ii].equals("-s")){
            	speed = Integer.parseInt(args[ii+1]);
            } else if(args[ii].equals("-i")){
                if(args[ii+1].equals("Soft")){
                    intensity = 0.995;
                }else if(args[ii+1].equals("Light")){
                    intensity = 0.98;
                } else if(args[ii+1].equals("Medium")){
                    intensity = 0.95;
                } else if(args[ii+1].equals("Heavy")){
                    intensity = 0.9;
                } else if (args[ii+1].equals("Dizzy")){
                    intensity = 0.75;
                } else if (args[ii+1].equals("Stop")){
                    intensity = 1.0;
                }
            } else if(args[ii].equals("-m")){
                if(args[ii+1].equals("1")){
                    mode = 1;
                }else if(args[ii+1].equals("2")){
                    mode = 2;
                }else if(args[ii+1].equals("3")){
                    mode = 3;
                }
            } else if(args[ii].equals("-f")){
                filename = args[ii+1];
            } else if(args[ii].equals("-t")){
                subtitle = args[ii+1];
            } else if(args[ii].equals("-c")){
                if(args[ii+1].equals("Red")){
                    subColor = Color.red;
                }else if(args[ii+1].equals("Black")){
                    subColor = Color.black;
                }else if(args[ii+1].equals("White")){
                    subColor = Color.white;
                }else if(args[ii+1].equals("Green")){
                    subColor = Color.green;
                }else if(args[ii+1].equals("Blue")){
                    subColor = Color.blue;
                }else if(args[ii+1].equals("Cyan")){
                    subColor = Color.cyan;
                }else if(args[ii+1].equals("Pink")){
                    subColor = Color.pink;
                }else if(args[ii+1].equals("Pale Green")){
                	subColor = new Color(0x78,0x99,0x34);
                }
            } else if(args[ii].equals("-g")){
                growth = Integer.parseInt(args[ii+1]);
            } else if(args[ii].equals("-tint")){
                tint = true;
                if(args[ii+1].equals("Yellow")){
                    tintColor = Color.yellow;
                }else if(args[ii+1].equals("Black")){
                    tintColor = Color.black;
                }else if(args[ii+1].equals("White")){
                    tintColor = Color.white;
                }else if(args[ii+1].equals("Green")){
                    tintColor = Color.green;
                }else if(args[ii+1].equals("Blue")){
                    tintColor = Color.blue;
                }else if(args[ii+1].equals("Cyan")){
                    tintColor = Color.cyan;
                }else if(args[ii+1].equals("Pink")){
                    tintColor = Color.pink;
                }else if(args[ii+1].equals("Pale Green")){
                	tintColor = new Color(0x78,0x99,0x34);
                }
            } else if(args[ii].equals("-growBurn")) growBurn = true;
        else if(args[ii].equals("-b")){
                burn = (float) Integer.parseInt(args[ii+1]) * 0.1f;
            }
        }
        if(args.length > 0){
            try{
                img = ImageIO.read(new File(args[0]));
            }catch(IOException e){ }
            ImageOutputStream output;
            commonHeight = (int) Math.ceil(img.getHeight()*intensity);
            commonWidth = (int) Math.ceil(img.getWidth()*intensity);
            frame2Offset = (int) Math.ceil((img.getWidth()-commonWidth)/2);
            frame2OffsetHeight = (int) Math.ceil((img.getHeight()-commonHeight)/2);
            if(mode == 0 || mode == 2 || mode == 3){
                frame1 = img.getSubimage(0, 0, commonWidth, commonHeight);
                if(mode == 0  || mode == 3){
                    frame2 = img.getSubimage(frame2Offset, (img.getHeight() - commonHeight), (img.getWidth()-2*frame2Offset), (img.getHeight() - (img.getHeight() - commonHeight)));
                    if(frame2.getWidth() > frame1.getWidth()){
                        frame2 = img.getSubimage(frame2Offset, (img.getHeight() - commonHeight), (img.getWidth()-2*frame2Offset)-1, (img.getHeight() - (img.getHeight() - commonHeight)));
                    }
                }
                frame3 = img.getSubimage((img.getWidth() - commonWidth),0,(img.getWidth() - (img.getWidth() - commonWidth)),commonHeight);
            } else if(mode == 1){
                frame1 = img;
                frame2 = img.getSubimage(frame2Offset, frame2OffsetHeight, (img.getWidth()-2*frame2Offset), (img.getHeight()-2*frame2OffsetHeight));
                frame2 = resize(frame2, frame1.getWidth(), frame1.getHeight());
            }
            if(tint){
                mask = generateMask(frame1, tintColor, burn);
                frame1 = tint(frame1, mask);
                if(mode != 2){
                    mask = generateMask(frame2, tintColor, burn);
                    frame2 = tint(frame2, mask);                    
                }
                if(mode != 1){
                    mask = generateMask(frame3, tintColor, burn);
                    frame3 = tint(frame3, mask);
                }
            }
            frame1 = addSubtitle(frame1, subtitle);
            if(mode != 2) frame2 = addSubtitle(frame2, subtitle);
            if(mode != 1) frame3 = addSubtitle(frame3, subtitle);
            System.out.println("Frame1: " + frame1.getWidth() + "x" + frame1.getHeight());
            if(mode != 2) System.out.println("Frame2: " + frame2.getWidth() + "x" + frame2.getHeight());
            if(mode != 1) System.out.println("Frame3: " + frame3.getWidth() + "x" + frame3.getHeight()); 
            output = new FileImageOutputStream(new File(filename));
            GifSequenceWriter writer = new GifSequenceWriter(output, frame1.getType(), speed, true);
            if(mode !=3) {
                writer.writeToSequence(frame1);
                if(mode != 2) writer.writeToSequence(frame2);
                if(mode != 1) writer.writeToSequence(frame3);
            }else{
                for(int ii = 0; ii<growth; ii++){
                    if(growBurn) burn = (float) ii * 0.02f;
                    if(intensity != 1.0){
                        commonHeight = (int) Math.ceil(img.getHeight()*(intensity-(ii*0.01)));
                        commonWidth = (int) Math.ceil(img.getWidth()*(intensity-(ii*0.01)));
                        frame2Offset = (int) Math.ceil((img.getWidth()-commonWidth)/2);
                        frame2OffsetHeight = (int) Math.ceil((img.getHeight()-commonHeight)/2);
                    }
                    frame4 = img.getSubimage(0, 0, commonWidth, commonHeight);
                    frame4 = resize(frame4, frame1.getWidth(), frame1.getHeight());
                    if(tint){
                        mask = generateMask(frame4, tintColor, burn);
                        frame4 = tint(frame4, mask); 
                    }
                    frame4 = addSubtitle(frame4, subtitle);
                    frame2 = img.getSubimage(frame2Offset, (img.getHeight() - commonHeight), (img.getWidth()-2*frame2Offset), (img.getHeight() - (img.getHeight() - commonHeight)));
                    if(frame2.getWidth() > frame4.getWidth()){
                        frame2 = img.getSubimage(frame2Offset, (img.getHeight() - commonHeight), (img.getWidth()-2*frame2Offset)-1, (img.getHeight() - (img.getHeight() - commonHeight)));
                    }
                    frame2 = resize(frame2, frame1.getWidth(), frame1.getHeight());
                    if(tint){
                        mask = generateMask(frame2, tintColor, burn);
                        frame2 = tint(frame2, mask); 
                    }
                    frame2 = addSubtitle(frame2, subtitle);
                    frame3 = img.getSubimage((img.getWidth() - commonWidth),0,(img.getWidth() - (img.getWidth() - commonWidth)),commonHeight);
                    frame3 = resize(frame3, frame1.getWidth(), frame1.getHeight());
                    if(tint){
                        mask = generateMask(frame3, tintColor, burn);
                        frame3 = tint(frame3, mask); 
                    }
                    frame3 = addSubtitle(frame3, subtitle);
                    writer.writeToSequence(frame4);
                    if(intensity != 1.0){
                        writer.writeToSequence(frame2);
                        writer.writeToSequence(frame3);
                    }
                }
            }
            writer.close();
            output.close();
            
        }
    }

    private static BufferedImage addSubtitle(BufferedImage old, String sub){
        int width = old.getWidth();
        int height = old.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(subColor);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(sub)) / 2;
        int y = height/10;
        y = y * 9;
        g2d.drawString(sub, x, y);
        g2d.dispose();
        return newImg;
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }  
    
    //Tinting code
    public static GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }
    
    public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }
    
    public static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
    
    public static BufferedImage generateMask(BufferedImage imgSource, Color color, float alpha) {
        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D g2 = imgMask.createGraphics();
        applyQualityRenderingHints(g2);

        g2.drawImage(imgSource, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        g2.setColor(color);

        g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
        g2.dispose();

        return imgMask;
    }
    
    public static BufferedImage tint(BufferedImage master, BufferedImage tint) {
        int imgWidth = master.getWidth();
        int imgHeight = master.getHeight();

        BufferedImage tinted = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D g2 = tinted.createGraphics();
        applyQualityRenderingHints(g2);
        g2.drawImage(master, 0, 0, null);
        g2.drawImage(tint, 0, 0, null);
        g2.dispose();

        return tinted;
    }

}