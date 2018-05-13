import java.util.Arrays;

public class WatercolorEffect implements Effect {

    private int w, h;
    private int[][][] img;

    private void copy(int[][][] source, int[][][] dst) {
        for(int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                dst[i][j][0] = source[i][j][0];
                dst[i][j][1] = source[i][j][1];
                dst[i][j][2] = source[i][j][2];
            }
        }
    }

    private void cutout(int[][][] img) {
        int num_steps = 6;
        int step_size = 256/num_steps;
        int steps[] = new int[num_steps + 1];
        int i = 0, j = 0, k = 0;

        while(i*step_size < 256) {
            steps[i] = step_size * i++;
        }

        steps[i] = 256;

        for(i = 0; i < h; i++) {
            for(j = 0; j < w; j++) {
                for(k = 0; k < 256/step_size - 1; k ++) {
                    if(img[i][j][0] > steps[k] && img[i][j][0] < steps[k+1]) {
                        img[i][j][0] = steps[k];
                    }
                    if(img[i][j][1] > steps[k] && img[i][j][1] < steps[k+1]) {
                        img[i][j][1] = steps[k];
                    }
                    if(img[i][j][2] > steps[k] && img[i][j][2] < steps[k+1]) {
                        img[i][j][2] = steps[k];
                    }
                }
            }
        }
    }

    private void dry_brush(int[][][] img) {

    }

    private void median(int[][][] img) {
        int kernel_size = 5; // 5x5
        int margin = (kernel_size - 1)/2;

        int cpy[][][] = new int[h][w][3];
        copy(img, cpy);

        for(int i = margin; i < h - margin; i++) {
            for(int j = margin; j < w - margin; j++) {
                int[] r, g, b;
                r = new int[kernel_size*kernel_size];
                g = new int[kernel_size*kernel_size];
                b = new int[kernel_size*kernel_size];

                int cx = -1, cy = -1;

                for(int k = i - margin; k <= i + margin; k++) {
                    cx++;
                    for(int l = j - margin; l <= j + margin; l++) {
                        cy++;
                        r[cy + cx*kernel_size] = cpy[k][l][0];
                        g[cy + cx*kernel_size] = cpy[k][l][1];
                        b[cy + cx*kernel_size] = cpy[k][l][2];
                    }
                }

                Arrays.sort(r);
                Arrays.sort(g);
                Arrays.sort(b);

                img[i][j][0] = r[kernel_size*kernel_size/2];
                img[i][j][1] = g[kernel_size*kernel_size/2];
                img[i][j][2] = b[kernel_size*kernel_size/2];
            }
        }
    }

    // The Luminosity blend mode preserves the hue and chroma of the bottom layer, while adopting the luma of the top layer.
    private int[][][] luminosity(int[][][] a, int[][][] b) {
        return null;
    }

    // Screen blend mode the values of the pixels in the two layers are inverted, multiplied, and then inverted again
    private int[][][] screen(int[][][] a, int[][][] b) {
        int r[][][] = new int[h][w][3];
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                r[i][j][0] = (int)Math.floor((1 - (1 - a[i][j][0]/255)*(1 - b[i][j][0]/255))*255);
                r[i][j][1] = (int)Math.floor((1 - (1 - a[i][j][1]/255)*(1 - b[i][j][1]/255))*255);
                r[i][j][2] = (int)Math.floor((1 - (1 - a[i][j][2]/255)*(1 - b[i][j][2]/255))*255);
            }
        }
        return r;
    }

    // Soft Light: https://en.wikipedia.org/wiki/Blend_modes
    private int[][][] soft_light(int[][][] a, int[][][] b) {
        // Am ales formula Pegtop deoarece nu are discontinuitae
        int r[][][] = new int[h][w][3];
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                r[i][j][0] = (1 - 2*b[i][j][0]/255)*(a[i][j][0]/255)*(a[i][j][0]/255) + 2*(a[i][j][0]/255)*(b[i][j][0]/255);
                r[i][j][1] = (1 - 2*b[i][j][1]/255)*(a[i][j][1]/255)*(a[i][j][1]/255) + 2*(a[i][j][1]/255)*(b[i][j][1]/255);
                r[i][j][2] = (1 - 2*b[i][j][2]/255)*(a[i][j][2]/255)*(a[i][j][2]/255) + 2*(a[i][j][2]/255)*(b[i][j][2]/255);

                r[i][j][0] = (int)Math.floor(r[i][j][0]*255);
                r[i][j][1] = (int)Math.floor(r[i][j][1]*255);
                r[i][j][2git s] = (int)Math.floor(r[i][j][2]*255);
            }
        }
        return r;
    }

    public WatercolorEffect(int[][][] img, int w, int h) {
        this.w = w;
        this.h = h;
        this.img = img;
    }

    @Override
    public void apply() {
        int[][][] l1 = new int[h][w][3], l2 = new int[h][w][3], l3 = new int[h][w][3];

        copy(img, l1);
        copy(img, l2);
        copy(img, l3);

        cutout(l1);
        dry_brush(l2);
        median(l3);

        soft_light(screen(luminosity(img, l1), l2), l3);
    }
}
