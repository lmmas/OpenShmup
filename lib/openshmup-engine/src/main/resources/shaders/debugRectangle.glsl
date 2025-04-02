#type vertex
#version 330 core
layout(location=0) in vec2 aPosition;
layout(location=1) in vec2 aQuadSize;
layout(location=2) in vec2 aTexturePosition;
layout(location=3) in vec2 aTextureSize;
layout(location=4) in int aTextureSlot;

out VS_OUT{
    vec2 gQuadSize;
    vec2 gTexturePosition;
    vec2 gTextureSize;
} vs_out;



void main(){

    //Window coordinates to NDC coordinates conversion
    gl_Position = vec4(aPosition * 2.0f - vec2(1.0f, 1.0f), 0.0f, 1.0f);
    vs_out.gQuadSize = aQuadSize * 2.0f;
    vs_out.gTexturePosition = aTexturePosition;
    vs_out.gTextureSize = aTextureSize;
}

#type geometry
#version 330 core
layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in VS_OUT{
    vec2 gQuadSize;
    vec2 gTexturePosition;
    vec2 gTextureSize;
}data_in[];

out vec2 fTextureCoords;

void build_quad(vec4 position, vec2 quadSize, vec2 texturePosition, vec2 textureSize){
    vec2 tempCoords = quadSize / 2.0f;
    gl_Position = position + vec4(-tempCoords[0], tempCoords[1], 0.0, 0.0); //top-left
    fTextureCoords = texturePosition + vec2(0.0, textureSize[1]);
    EmitVertex();
    gl_Position = position + vec4(tempCoords[0], tempCoords[1], 0.0, 0.0); //top-right
    fTextureCoords = texturePosition + textureSize;
    EmitVertex();
    gl_Position = position + vec4(-tempCoords[0], -tempCoords[1], 0.0, 0.0);//bottom-left
    fTextureCoords = texturePosition;
    EmitVertex();
    gl_Position = position + vec4(tempCoords[0], -tempCoords[1], 0.0, 0.0); //bottom-right
    fTextureCoords = texturePosition + vec2(textureSize[0], 0.0);
    EmitVertex();
    EndPrimitive();
}

void main(){
    build_quad(gl_in[0].gl_Position, data_in[0].gQuadSize, data_in[0].gTexturePosition, data_in[0].gTextureSize);
}

#type fragment
#version 330 core

in vec2 fTextureCoords;

out vec4 fragColor;

void main(){
    if(fTextureCoords[0] < 0.1f || fTextureCoords[0] > 0.9f || fTextureCoords[1] < 0.1f || fTextureCoords[1] > 0.9f){
        fragColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);
    }
    else{
        fragColor = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    }
}