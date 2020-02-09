package engine.graphics.shader;

import engine.Platform;
import engine.client.asset.AssetURL;
import engine.graphics.gl.shader.CompiledShader;
import engine.graphics.gl.shader.ShaderProgram;
import engine.graphics.gl.shader.ShaderType;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShaderProgramBuilder {

    private final Map<ShaderType, AssetURL> shaders = new HashMap<>();

    public ShaderProgramBuilder addShader(@Nonnull ShaderType type, @Nonnull AssetURL path) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(path);
        shaders.put(type, path);
        return this;
    }

    public ShaderProgram build() {
        CompiledShader[] loadedShaders = new CompiledShader[shaders.size()];
        int i = 0;
        for (Map.Entry<ShaderType, AssetURL> entry : shaders.entrySet()) {
            var shaderPath = Platform.getEngineClient().getAssetManager().getSourceManager().getPath(entry.getValue().toFileLocation());
            if (shaderPath.isPresent()) {
                try {
                    loadedShaders[i] = CompiledShader.compile(entry.getKey(), Files.readString(shaderPath.get()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return new ShaderProgram(loadedShaders);
    }
}