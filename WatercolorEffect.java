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

    }

    private void dry_brush(int[][][] img) {

    }

    private void median(int[][][] img) {

    }

    // The Luminosity blend mode preserves the hue and chroma of the bottom layer, while adopting the luma of the top layer.
    private int[][][] luminosity(int[][][] a, int[][][] b) {
        return null;
    }

    // Screen blend mode the values of the pixels in the two layers are inverted, multiplied, and then inverted again
    private int[][][] screen(int[][][] a, int[][][] b) {
        return null;
    }

    // Soft Light: https://en.wikipedia.org/wiki/Blend_modes
    private int[][][] soft_light(int[][][] a, int[][][] b) {
        return null;
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
