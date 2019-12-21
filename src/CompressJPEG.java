import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
 
public class CompressJPEG {
 
    public static void main(String[] args) throws IOException {
 
        String[] files;
        File input = new File(".");
        try(Scanner in = new Scanner(System.in))       
        {
            System.out.println("Enter the quality of compressed images(in %): ");
            System.out.println("e.g.: 50");
            float qualityInPercentage = in.nextFloat(); //user input for quality
            if(input.isDirectory())
            {
                files = input.list();
                for(int i = 0; i < files.length; i++)
                {
                    if(files[i].contains(".jpg") || files[i].contains(".jpeg") || files[i].contains(".png"))
                    {
                        File imageFile = new File(files[i]);
                        File compressedImageFile = new File(files[i].replace(".jpeg", "_compressed.jpeg").replace(".jpg", "_compressed.jpg").replace(".png", "_compressed.png"));
                
                        InputStream is = new FileInputStream(imageFile);
                        OutputStream os = new FileOutputStream(compressedImageFile);
                
                        float quality = qualityInPercentage/100;
                
                        // create a BufferedImage as the result of decoding the supplied InputStream
                        BufferedImage image = ImageIO.read(is);
                
                        // get all image writers for JPG format
                        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                
                        if (!writers.hasNext())
                        {
                            os.close();
                            throw new IllegalStateException("No writers found");
                        }
                        ImageWriter writer = (ImageWriter) writers.next();
                        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
                        writer.setOutput(ios);
                
                        ImageWriteParam param = writer.getDefaultWriteParam();
                
                        // compress to a given quality
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(quality);
                
                        // appends a complete image stream containing a single image and
                        //associated stream and image metadata and thumbnails to the output
                        writer.write(null, new IIOImage(image, null, null), param);
                
                        // close all streams
                        is.close();
                        os.close();
                        ios.close();
                        writer.dispose();
                
                    }
                }
            }
    }catch(Exception e)
    {
        e.printStackTrace();
        
    }
}
}