package engine.render;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.lwjgl.opengl.GL33.*;


public class Shader {

    private int shaderProgramID;
    private final String filepath;
    private String vertexSource;
    private String geometrySource;
    private String fragmentSource;

    private static final HashMap<String, Shader> shaderMap = new HashMap<String, Shader>();

    private Shader(String filepath){
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));

            String[] splitSource = source.split("#type\\sfragment|#type\\sgeometry");

            assert splitSource.length == 2 || splitSource.length == 3: "Invalid source separation of shader '" + filepath + "'";
            if (splitSource.length == 2){
                vertexSource = splitSource[0].replaceFirst("#type\\svertex","").trim();
                fragmentSource = splitSource[1].replaceFirst("#type\\sfragment","").trim();
            }
            else if(splitSource.length == 3){
                vertexSource = splitSource[0].replaceFirst("#type\\svertex","").trim();
                geometrySource = splitSource[1].replaceFirst("#type\\sgeometry","").trim();
                fragmentSource = splitSource[2].replaceFirst("#type\\sfragment","").trim();
            }
        } catch(IOException e){
            assert false: "Error (Shader): could not open shader file '" + filepath + "'.";
        }
    }

    public static Shader loadShader(String filepath){
        if (shaderMap.containsKey(filepath)){
            return shaderMap.get(filepath);
        }
        else{
            Shader newShader = new Shader(filepath);
            shaderMap.put(filepath, newShader);
            newShader.compile();
            return newShader;
        }
    }

    public String getFilepath() {
        return filepath;
    }

    public void compile(){
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        assert success != GL_FALSE: glGetShaderInfoLog(vertexID);
        String log = glGetShaderInfoLog(vertexID);
        if (!log.isEmpty()) {
            System.out.println("Vertex Shader Compilation Log:\n" + log);
        }



        int geometryID = -1;
        if(geometrySource != null){
            geometryID = glCreateShader(GL_GEOMETRY_SHADER);
            glShaderSource(geometryID, geometrySource);
            glCompileShader(geometryID);
            success = glGetShaderi(geometryID, GL_COMPILE_STATUS);
            assert success != GL_FALSE: glGetShaderInfoLog(geometryID);
            log = glGetShaderInfoLog(geometryID);
            if (!log.isEmpty()) {
                System.out.println("Geometry Shader Compilation Log:\n" + log);
            }
        }

        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        assert success != GL_FALSE: glGetShaderInfoLog(fragmentID);
        log = glGetShaderInfoLog(fragmentID);
        if (!log.isEmpty()) {
            System.out.println("Fragment Shader Compilation Log:\n" + log);
        }

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        if(geometryID != -1){
            glAttachShader(shaderProgramID, geometryID);
        }
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        assert success != GL_FALSE: glGetProgramInfoLog(shaderProgramID);
        log = glGetProgramInfoLog(shaderProgramID);
        if (!log.isEmpty()) {
            System.out.println("Shader Linking Log:\n" + log);
        }

        glDeleteShader(vertexID);
        if (geometryID != -1){
            glDeleteShader(geometryID);
        }
        glDeleteShader(fragmentID);
        validate();
    }

    public void validate(){
        glValidateProgram(shaderProgramID);
        assert glGetProgrami(shaderProgramID, GL_VALIDATE_STATUS) != GL_FALSE : "Shader validation error: " + glGetProgramInfoLog(shaderProgramID);
    }

    public void use(){
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadUniform(String uniformName, float[] vector){
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        uploadUniform(uniformLocation, vector);
    }

    public void uploadUniform(int uniformLocation, float[] vector){
        assert vector.length <= 4: "Invalid vector dimension: " + vector.length;
        use();
        switch(vector.length){
            case 2:
                glUniform2f(uniformLocation, vector[0], vector[1]);
                break;

            case 3:
                glUniform3f(uniformLocation, vector[0], vector[1], vector[2]);
                break;

            case 4:
                glUniform4f(uniformLocation, vector[0], vector[1], vector[2], vector[3]);
                break;
        }
    }

    public void uploadUniform(String uniformName, int i){
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform1i(uniformLocation, i);
    }

    public void uploadUniform(int uniformLocation, int[] vector){
        assert vector.length <= 4: "Invalid vector dimension: " + vector.length;
        use();
        switch(vector.length){
            case 2:
                glUniform2i(uniformLocation, vector[0], vector[1]);
                break;

            case 3:
                glUniform3i(uniformLocation, vector[0], vector[1], vector[2]);
                break;

            case 4:
                glUniform4i(uniformLocation, vector[0], vector[1], vector[2], vector[3]);
                break;
        }
    }

    public void uploadUniformIntArray(String uniformName, int[] array) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        use();
        glUniform1iv(uniformLocation, array);
    }

    public void uploadUniform(String uniformName, int[] vector){
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        uploadUniform(uniformLocation, vector);
    }

    public void uploadTexture(String uniformName, int slot){
        uploadUniform(uniformName, slot);
    }

    public int[] getUniform(String uniformName){
        use();
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        assert uniformLocation != -1: "Uniform '" + uniformName + "' not found";
        IntBuffer buffer = BufferUtils.createIntBuffer(2);
        glGetUniformiv(shaderProgramID, uniformLocation, buffer);
        return new int[]{buffer.get(0), buffer.get(1)};
    }
}
