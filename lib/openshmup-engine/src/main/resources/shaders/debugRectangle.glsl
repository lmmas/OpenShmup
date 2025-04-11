#type vertex
#version 330 core
layout(location=0) in vec2 a_Position;
layout(location=1) in vec2 a_QuadSize;
layout(location=2) in vec4 a_Color;

out VS_OUT{
    vec2 v_QuadSize;
    vec4 v_Color;
} v_Out;



void main(){

    //Window coordinates to NDC coordinates conversion
    gl_Position = vec4(a_Position * 2.0f - vec2(1.0f, 1.0f), 0.0f, 1.0f);
    v_Out.v_QuadSize = a_QuadSize * 2.0f;
    v_Out.v_Color = a_Color;
}

#type geometry
#version 330 core
layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in VS_OUT{
    vec2 v_QuadSize;
    vec4 v_Color;
}v_in[];

out vec2 v_InternalPosition;
flat out vec4 v_Color;

void build_quad(vec4 position, vec2 quadSize, vec4 color){
    vec2 tempCoords = quadSize / 2.0f;
    gl_Position = position + vec4(-tempCoords[0], tempCoords[1], 0.0, 0.0); //top-left
    v_InternalPosition = vec2(0.0f, 1.0f);
    v_Color = color;
    EmitVertex();
    gl_Position = position + vec4(tempCoords[0], tempCoords[1], 0.0, 0.0); //top-right
    v_InternalPosition = vec2(1.0f, 1.0f);
    v_Color = color;
    EmitVertex();
    gl_Position = position + vec4(-tempCoords[0], -tempCoords[1], 0.0, 0.0);//bottom-left
    v_InternalPosition = vec2(0.0f, 0.0f);
    v_Color = color;
    EmitVertex();
    gl_Position = position + vec4(tempCoords[0], -tempCoords[1], 0.0, 0.0); //bottom-right
    v_InternalPosition = vec2(1.0f, 0.0f);
    v_Color = color;
    EmitVertex();
    EndPrimitive();
}

void main(){
    build_quad(gl_in[0].gl_Position, v_in[0].v_QuadSize, v_in[0].v_Color);
}

#type fragment
#version 330 core

in vec2 v_InternalPosition;
flat in vec4 v_Color;

out vec4 fragColor;

void main(){
    if(v_InternalPosition[0] < 0.1f || v_InternalPosition[0] > 0.9f || v_InternalPosition[1] < 0.1f || v_InternalPosition[1] > 0.9f){
        fragColor = v_Color;
    }
    else{
        fragColor = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    }
}