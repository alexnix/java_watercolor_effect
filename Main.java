import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage bi = ImageIO.read(new File("/home/alex/Documents/cat.jpg"));
            int[][][] img = new int[bi.getHeight()][bi.getWidth()][3];
            int[] pixel;
            for (int y = 0; y < bi.getHeight(); y++) {
                for (int x = 0; x < bi.getWidth(); x++) {
                    pixel = bi.getRaster().getPixel(x, y, new int[3]);
                    System.out.println(pixel[0] + " - " + pixel[1] + " - " + pixel[2] + " - " + (bi.getWidth() * y + x));
                    img[y][x][0] = pixel[0];
                    img[y][x][1] = pixel[1];
                    img[y][x][2] = pixel[2];
                }
            }

            new WatercolorEffect(img, bi.getWidth(), bi.getHeight()).apply();

        } catch (IOException e) {

        }
    }
}
