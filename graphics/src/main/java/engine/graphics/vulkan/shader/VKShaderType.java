package engine.graphics.vulkan.shader;

import engine.graphics.shader.ShaderType;
import org.lwjgl.vulkan.VK10;

public enum VKShaderType {
    VERTEX_SHADER(ShaderType.VERTEX_SHADER, VK10.VK_PIPELINE_STAGE_VERTEX_SHADER_BIT, VK10.VK_SHADER_STAGE_VERTEX_BIT),
    TESSELLATION_CONTROL_SHADER(ShaderType.TESSELLATION_CONTROL_SHADER, VK10.VK_PIPELINE_STAGE_TESSELLATION_CONTROL_SHADER_BIT, VK10.VK_SHADER_STAGE_TESSELLATION_CONTROL_BIT),
    TESSELLATION_EVALUATION_SHADER(ShaderType.TESSELLATION_EVALUATION_SHADER, VK10.VK_PIPELINE_STAGE_TESSELLATION_EVALUATION_SHADER_BIT, VK10.VK_SHADER_STAGE_TESSELLATION_EVALUATION_BIT),
    GEOMETRY_SHADER(ShaderType.GEOMETRY_SHADER, VK10.VK_PIPELINE_STAGE_GEOMETRY_SHADER_BIT, VK10.VK_SHADER_STAGE_GEOMETRY_BIT),
    FRAGMENT_SHADER(ShaderType.FRAGMENT_SHADER, VK10.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT, VK10.VK_SHADER_STAGE_FRAGMENT_BIT),
    COMPUTE_SHADER(ShaderType.COMPUTE_SHADER, VK10.VK_PIPELINE_STAGE_COMPUTE_SHADER_BIT, VK10.VK_SHADER_STAGE_COMPUTE_BIT);

    private engine.graphics.shader.ShaderType peer;
    public int pipelineVk;
    public int shaderVk;

    public static VKShaderType valueOf(ShaderType type) {
        return values()[type.ordinal()];
    }

    VKShaderType(ShaderType peer, int pipelineVk, int shaderVk) {
        this.peer = peer;
        this.pipelineVk = pipelineVk;
        this.shaderVk = shaderVk;
    }
}
