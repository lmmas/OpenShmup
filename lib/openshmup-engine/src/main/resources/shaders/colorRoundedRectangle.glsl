#type vertex
#version 330 core
layout(location=0) in vec2 a_QuadSize;
layout(location=1) in vec2 a_Position;
layout(location=2) in float a_RoundingRadius;
layout(location=3) in vec4 a_Color;

uniform ivec2 u_NativeResolution;

out VS_OUT{
    vec2 v_QuadSize;
    vec4 v_Color;
    float v_RoundingRadius;
} v_Out;



void main(){

    //Window coordinates to NDC coordinates conversion
    gl_Position = vec4(a_Position / u_NativeResolution * 2.0f - vec2(1.0f, 1.0f), 0.0f, 1.0f);
    v_Out.v_QuadSize = a_QuadSize / u_NativeResolution * 2.0f;
    v_Out.v_Color = a_Color;
    v_Out.v_RoundingRadius = a_RoundingRadius * 2 / a_QuadSize.x;
}

#type geometry
#version 330 core
layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in VS_OUT{
    vec2 v_QuadSize;
    vec4 v_Color;
    float v_RoundingRadius;
}v_in[];

uniform float u_NativeAspectRatio;

flat out vec4 v_Color;
flat out float v_RoundingRadius;
flat out float v_QuadRelativeHeight;
out vec2 v_PositionInQuad;

void build_quad(vec4 position, vec2 quadSize, float roundingRadius, vec4 color){
    float quadRelativeHeight;
    if (quadSize.x != 0){
        quadRelativeHeight = quadSize.y / quadSize.x / u_NativeAspectRatio;
    }
    else {
        quadRelativeHeight = 0.0f;
    }
    vec2 tempCoords = quadSize / 2.0f;

    //common values
    v_Color = color;
    v_RoundingRadius = roundingRadius;
    v_QuadRelativeHeight = quadRelativeHeight;

    //vertex-specific values
    gl_Position = position + vec4(-tempCoords.x, tempCoords.y, 0.0, 0.0);//top-left
    v_PositionInQuad = vec2(-1.0f, v_QuadRelativeHeight);
    EmitVertex();
    gl_Position = position + vec4(tempCoords.x, tempCoords.y, 0.0, 0.0);//top-right
    v_PositionInQuad = vec2(1.0f, v_QuadRelativeHeight);
    EmitVertex();
    gl_Position = position + vec4(-tempCoords.x, -tempCoords.y, 0.0, 0.0);//bottom-left
    v_PositionInQuad = vec2(-1.0f, -v_QuadRelativeHeight);
    EmitVertex();
    gl_Position = position + vec4(tempCoords.x, -tempCoords.y, 0.0, 0.0);//bottom-right
    v_PositionInQuad = vec2(1.0f, -v_QuadRelativeHeight);
    EmitVertex();
    EndPrimitive();
}

void main(){
    build_quad(gl_in[0].gl_Position, v_in[0].v_QuadSize, v_in[0].v_RoundingRadius, v_in[0].v_Color);
}

#type fragment
#version 330 core

flat in vec4 v_Color;
flat in float v_RoundingRadius;
flat in float v_QuadRelativeHeight;
in vec2 v_PositionInQuad;


out vec4 fragColor;

float roundedBoxSDF(vec2 positionInQuad, float quadRelativeHeight, float roundingRadius){
    vec2 q = abs(positionInQuad) - vec2(1.0f, quadRelativeHeight) + roundingRadius;
    return length(max(q, 0.0f)) + min(max(q.x, q.y), 0.0f) - roundingRadius;
}

void main(){
    float distance = roundedBoxSDF(v_PositionInQuad, v_QuadRelativeHeight, v_RoundingRadius);
    float aa = fwidth(distance);
    float shapeAlpha = 1.0f - smoothstep(-aa, aa, distance);
    fragColor = vec4(v_Color.rgb, v_Color.a * shapeAlpha);
}