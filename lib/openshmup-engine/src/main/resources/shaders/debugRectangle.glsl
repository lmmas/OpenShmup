#type vertex
#version 330 core
layout(location=0) in vec2 a_QuadSize;
layout(location=1) in vec2 a_Position;
layout(location=2) in vec4 a_Color;

uniform ivec2 u_NativeResolution;
uniform ivec2 u_WindowResolution;

out VS_OUT{
    vec2 v_QuadSize;
    vec2 v_QuadSizePixels;
    vec4 v_Color;
} v_Out;



void main(){

    //Window coordinates to NDC coordinates conversion
    gl_Position = vec4(a_Position / u_NativeResolution * 2.0f - vec2(1.0f, 1.0f), 0.0f, 1.0f);
    v_Out.v_QuadSize = a_QuadSize / u_NativeResolution * 2.0f;
    v_Out.v_Color = a_Color;
    v_Out.v_QuadSizePixels = u_WindowResolution * v_Out.v_QuadSize;
}

#type geometry
#version 330 core
layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in VS_OUT{
    vec2 v_QuadSize;
    vec2 v_QuadSizePixels;
    vec4 v_Color;
}v_in[];

out vec2 v_InternalCoordsPixels;
flat out vec4 v_Color;
flat out vec2 v_QuadSizePixels;

void build_quad(vec4 position, vec2 quadSize, vec2 quadSizePixels, vec4 color){
    //common values
    v_QuadSizePixels = quadSizePixels;
    v_Color = color;

    //vertex-specific values
    vec2 tempCoords = quadSize / 2.0f;
    gl_Position = position + vec4(-tempCoords.x, tempCoords.y, 0.0, 0.0);//top-left
    v_InternalCoordsPixels = vec2(0.0f, quadSizePixels.y);
    EmitVertex();

    gl_Position = position + vec4(tempCoords.x, tempCoords.y, 0.0, 0.0);//top-right
    v_InternalCoordsPixels = quadSizePixels;
    EmitVertex();

    gl_Position = position + vec4(-tempCoords.x, -tempCoords.y, 0.0, 0.0);//bottom-left
    v_InternalCoordsPixels = vec2(0.0f, 0.0f);
    EmitVertex();

    gl_Position = position + vec4(tempCoords.x, -tempCoords.y, 0.0, 0.0);//bottom-right
    v_InternalCoordsPixels = vec2(quadSizePixels.x, 0.0f);
    EmitVertex();

    EndPrimitive();
}

void main(){
    build_quad(gl_in[0].gl_Position, v_in[0].v_QuadSize, v_in[0].v_QuadSizePixels, v_in[0].v_Color);
}

#type fragment
#version 330 core

in vec2 v_InternalCoordsPixels;
flat in vec2 v_QuadSizePixels;
flat in vec4 v_Color;

out vec4 fragColor;

void main(){
    int borderWidth = 3;
    if (v_InternalCoordsPixels.x < float(borderWidth) || v_InternalCoordsPixels.x > v_QuadSizePixels.x - float(borderWidth) || v_InternalCoordsPixels.y < float(borderWidth) || v_InternalCoordsPixels.y > v_QuadSizePixels.y - float(borderWidth)){
        fragColor = v_Color;
    }
    else {
        fragColor = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    }
}