#type vertex
#version 330 core
layout(location=0) in vec2 a_Position;
layout(location=1) in vec2 a_QuadSize;
layout(location=2) in vec2 a_TexturePosition;
layout(location=3) in vec2 a_TextureSize;
layout(location=4) in int a_TextureSlot;

out VS_OUT{
    vec2 v_QuadSize;
    vec2 v_TexturePosition;
    vec2 v_TextureSize;
    int v_TextureSlot;
} vs_out;



void main(){

    //Window coordinates to NDC coordinates conversion
    gl_Position = vec4(a_Position * 2.0f - vec2(1.0f, 1.0f), 0.0f, 1.0f);
    vs_out.v_QuadSize = a_QuadSize * 2.0f;
    vs_out.v_TexturePosition = a_TexturePosition;
    vs_out.v_TextureSize = a_TextureSize;
    vs_out.v_TextureSlot = a_TextureSlot;
}

#type geometry
#version 330 core
layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in VS_OUT{
    vec2 v_QuadSize;
    vec2 v_TexturePosition;
    vec2 v_TextureSize;
    int v_TextureSlot;
}data_in[];

out vec2 v_TextureCoords;
flat out int v_TextureSlot;
flat out vec2 v_TextureCoordsMin;
flat out vec2 v_TextureCoordsMax;


void build_quad(vec4 position, vec2 quadSize, vec2 texturePosition, vec2 textureSize, int textureSlot){
    vec2 tempCoords = quadSize / 2.0f;
    v_TextureSlot = textureSlot;
    v_TextureCoordsMin = texturePosition;
    v_TextureCoordsMax = texturePosition + textureSize;
    gl_Position = position + vec4(-tempCoords[0], tempCoords[1], 0.0, 0.0); //top-left
    v_TextureCoords = texturePosition + vec2(0.0, textureSize[1]);
    EmitVertex();
    gl_Position = position + vec4(tempCoords[0], tempCoords[1], 0.0, 0.0); //top-right
    v_TextureCoords = texturePosition + textureSize;
    EmitVertex();
    gl_Position = position + vec4(-tempCoords[0], -tempCoords[1], 0.0, 0.0);//bottom-left
    v_TextureCoords = texturePosition;
    EmitVertex();
    gl_Position = position + vec4(tempCoords[0], -tempCoords[1], 0.0, 0.0); //bottom-right
    v_TextureCoords = texturePosition + vec2(textureSize[0], 0.0);
    EmitVertex();
    EndPrimitive();
}

void main(){
    build_quad(gl_in[0].gl_Position, data_in[0].v_QuadSize, data_in[0].v_TexturePosition, data_in[0].v_TextureSize, data_in[0].v_TextureSlot);
}

#type fragment
#version 330 core

uniform sampler2D TEX_SAMPLER[32];
in vec2 v_TextureCoords;
flat in int v_TextureSlot;
flat in vec2 v_TextureCoordsMin;
flat in vec2 v_TextureCoordsMax;

out vec4 fragColor;

void main(){
    float safetyMargin = 0.00001f;
    vec2 safeTextureCoords = clamp(v_TextureCoords, v_TextureCoordsMin + safetyMargin, v_TextureCoordsMax - safetyMargin);
    fragColor = texture(TEX_SAMPLER[v_TextureSlot], safeTextureCoords);
    //fragColor = vec4(safeTextureCoords, 0.0f, 1.0f);
}