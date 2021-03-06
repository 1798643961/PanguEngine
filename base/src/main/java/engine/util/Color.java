package engine.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Objects;

public class Color {

    public static final Color BLACK = Color.fromRGB(0x000000);
    public static final Color RED = Color.fromRGB(0xff0000);
    public static final Color GREEN = Color.fromRGB(0x00ff00);
    public static final Color BLUE = Color.fromRGB(0x0000ff);
    public static final Color WHITE = Color.fromRGB(0xffffff);

    public static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0f);

    public static Color fromGray(int gray) {
        return new Color(gray, gray, gray);
    }

    public static Color fromGray(float gray) {
        return new Color(gray, gray, gray);
    }

    public static Color fromRGB(String rgb) {
        return fromRGB(Integer.parseInt(rgb, 16));
    }

    public static Color fromRGB(int rgb) {
        return new Color((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
    }

    public static Color fromARGB(String argb) {
        return fromARGB(Integer.parseInt(argb, 16));
    }

    public static Color fromARGB(int argb) {
        return new Color((argb >> 16) & 255, (argb >> 8) & 255, argb & 255, (argb >> 24) & 255);
    }

    public static Color fromRGBA(String argb) {
        return fromRGBA(Integer.parseInt(argb, 16));
    }

    public static Color fromRGBA(int argb) {
        return new Color((argb >> 24) & 255, (argb >> 16) & 255, (argb >> 8) & 255, argb & 255);
    }

    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;

    public Color(int red, int green, int blue) {
        this(red / 255f, green / 255f, blue / 255f, 1f);
    }

    public Color(float red, float green, float blue) {
        this(red, green, blue, 1f);
    }

    public Color(int red, int green, int blue, int alpha) {
        this(red / 255f, green / 255f, blue / 255f, alpha / 255f);
    }

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getRedAsInt() {
        return (int) (red * 255);
    }

    public int getGreenAsInt() {
        return (int) (green * 255);
    }

    public int getBlueAsInt() {
        return (int) (blue * 255);
    }

    public int getAlphaAsInt() {
        return (int) (alpha * 255);
    }

    public FloatBuffer get(FloatBuffer dst) {
        return get(dst.position(), dst);
    }

    public FloatBuffer get(int index, FloatBuffer dst) {
        dst.put(index, red);
        dst.put(index + 1, green);
        dst.put(index + 2, blue);
        dst.put(index + 3, alpha);
        return dst;
    }

    public ByteBuffer get(ByteBuffer dst) {
        return get(dst.position(), dst);
    }

    public ByteBuffer get(int index, ByteBuffer dst) {
        dst.putFloat(index, red);
        dst.putFloat(index + 4, green);
        dst.putFloat(index + 8, blue);
        dst.putFloat(index + 12, alpha);
        return dst;
    }

    public float[] get(float[] dst) {
        return get(0, dst);
    }

    public float[] get(int index, float[] dst) {
        dst[index] = red;
        dst[index + 1] = green;
        dst[index + 2] = blue;
        dst[index + 3] = alpha;
        return dst;
    }

    public FloatBuffer getRGB(FloatBuffer dst) {
        return get(dst.position(), dst);
    }

    public FloatBuffer getRGB(int index, FloatBuffer dst) {
        dst.put(index, red);
        dst.put(index + 1, green);
        dst.put(index + 2, blue);
        return dst;
    }

    public ByteBuffer getRGB(ByteBuffer dst) {
        return get(dst.position(), dst);
    }

    public ByteBuffer getRGB(int index, ByteBuffer dst) {
        dst.putFloat(index, red);
        dst.putFloat(index + 4, green);
        dst.putFloat(index + 8, blue);
        return dst;
    }

    public float[] getRGB(float[] dst) {
        return get(0, dst);
    }

    public float[] getRGB(int index, float[] dst) {
        dst[index] = red;
        dst[index + 1] = green;
        dst[index + 2] = blue;
        return dst;
    }

    public int toRGB() {
        return (getRedAsInt() << 16) | (getGreenAsInt() << 8) | getBlueAsInt();
    }

    public int toARGB() {
        return (getAlphaAsInt() << 24) | toRGB();
    }

    public int toRGBA() {
        return (toRGB() << 8) | getAlphaAsInt();
    }

    public float[] toRGBAFloatArray() {
        return new float[]{red, green, blue, alpha};
    }

    public Color invert() {
        return difference(Color.WHITE);
    }

    public Color difference(Color color) {
        return new Color(Math.abs(color.red - this.red) * color.alpha + red * (1 - color.alpha), Math.abs(color.green - this.green) * color.alpha + color.green * (1 - color.alpha), Math.abs(color.blue - this.blue) * color.alpha + blue * (1 - color.alpha), alpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Float.compare(color.red, red) == 0 &&
                Float.compare(color.green, green) == 0 &&
                Float.compare(color.blue, blue) == 0 &&
                Float.compare(color.alpha, alpha) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", alpha=" + alpha +
                '}';
    }
}
