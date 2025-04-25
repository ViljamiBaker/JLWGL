#version 330 core
out vec4 FragColor;
  
in vec3 vertexCoord;

void main()
{
    vec3 col = vertexCoord.xyx;
    FragColor = vec4(col,0.5);
} 