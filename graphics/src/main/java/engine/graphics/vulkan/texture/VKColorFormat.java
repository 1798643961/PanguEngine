package engine.graphics.vulkan.texture;

import engine.graphics.texture.ColorFormat;
import org.lwjgl.vulkan.VK10;

public enum VKColorFormat {
    UNDEFINED(VK10.VK_FORMAT_UNDEFINED, null),
    BGRA_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_B8G8R8A8_UNORM, ColorFormat.BGRA8),
    RGBA_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8B8A8_UNORM, ColorFormat.RGBA8),
    BGRA_NORMALIZED(VK10.VK_FORMAT_B8G8R8A8_SNORM, ColorFormat.BGRA8),
    RGBA_NORMALIZED(VK10.VK_FORMAT_R8G8B8A8_SNORM, ColorFormat.RGBA8),
    BGRA_UINT(VK10.VK_FORMAT_B8G8R8A8_UINT, ColorFormat.BGRA8),
    RGBA_UINT(VK10.VK_FORMAT_R8G8B8A8_UINT, ColorFormat.RGBA8),
    BGRA_INT(VK10.VK_FORMAT_B8G8R8A8_SINT, ColorFormat.BGRA8),
    RGBA_INT(VK10.VK_FORMAT_R8G8B8A8_SINT, ColorFormat.RGBA8),
    BGRA_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8A8_USCALED, ColorFormat.BGRA8),
    RGBA_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8A8_USCALED, ColorFormat.BGRA8),
    BGRA_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8A8_SSCALED, ColorFormat.BGRA8),
    RGBA_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8A8_SSCALED, ColorFormat.RGBA8),
    R8_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8_UNORM, ColorFormat.RED8),
    R8_NORMALIZED(VK10.VK_FORMAT_R8_SNORM, ColorFormat.RED8),
    R8_UINT(VK10.VK_FORMAT_R8_UINT, ColorFormat.RED8UI),
    R8_INT(VK10.VK_FORMAT_R8_SINT, ColorFormat.RED8),
    R8_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8_USCALED, ColorFormat.RED8),
    R8_SCALED_FLOAT(VK10.VK_FORMAT_R8_SSCALED, ColorFormat.RED8),
    R8G8_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8_UNORM, ColorFormat.RG8),
    R8G8_NORMALIZED(VK10.VK_FORMAT_R8G8_SNORM, ColorFormat.RG8),
    R8G8_UINT(VK10.VK_FORMAT_R8G8_UINT, ColorFormat.RG8),
    R8G8_INT(VK10.VK_FORMAT_R8G8_SINT, ColorFormat.RG8),
    R8G8_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8_USCALED, ColorFormat.RG8),
    R8G8_SCALED_FLOAT(VK10.VK_FORMAT_R8G8_SSCALED, ColorFormat.RG8),
    RGB_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8B8_UNORM, ColorFormat.RGB8),
    RGB_NORMALIZED(VK10.VK_FORMAT_R8G8B8_SNORM, ColorFormat.RGB8),
    RGB_UINT(VK10.VK_FORMAT_R8G8B8_UINT, ColorFormat.RGB8),
    RGB_INT(VK10.VK_FORMAT_R8G8B8_SINT, ColorFormat.RGB8),
    RGB_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8_USCALED, ColorFormat.RGB8),
    RGB_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8_SSCALED, ColorFormat.RGB8),
    BGR_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_B8G8R8_UNORM, ColorFormat.BGR8),
    BGR_NORMALIZED(VK10.VK_FORMAT_B8G8R8_SNORM, ColorFormat.BGR8),
    BGR_UINT(VK10.VK_FORMAT_B8G8R8_UINT, ColorFormat.BGR8),
    BGR_INT(VK10.VK_FORMAT_B8G8R8_SINT, ColorFormat.BGR8),
    BGR_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8_USCALED, ColorFormat.BGR8),
    BGR_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8_SSCALED, ColorFormat.BGR8),

    DEPTH_32(VK10.VK_FORMAT_D32_SFLOAT, ColorFormat.DEPTH32),
    DEPTH_32_STENCIL_8(VK10.VK_FORMAT_D32_SFLOAT_S8_UINT, null),
    DEPTH_24_STENCIL_8(VK10.VK_FORMAT_D24_UNORM_S8_UINT, ColorFormat.DEPTH24_STENCIL8),
    DEPTH_16_STENCIL_8(VK10.VK_FORMAT_D16_UNORM_S8_UINT, null),
    DEPTH_16(VK10.VK_FORMAT_D16_UNORM, null);
    private final int vk;
    private final ColorFormat peer;

    VKColorFormat(int vk, ColorFormat peer) {
        this.vk = vk;
        this.peer = peer;
    }

    public int getVk() {
        return vk;
    }

    public ColorFormat getPeer() {
        return peer;
    }

    public static VKColorFormat fromVkFormat(int vk) {
        for (VKColorFormat value : values()) {
            if (value.vk == vk) {
                return value;
            }
        }
        return UNDEFINED;
    }
}