"use strict";

let cameraPosition = [2, 10, 100];
let cameraDirection = [0, 0, -1];
let FOREST_SIZE = 100;
let FOREST_DENSITY = 100;
let SEED = 0;
let TREE_SCALE = 1;
let GRASS_AMOUNT = 1;
let TREE_AMOUNT = 5;
let WORLD_DATA = generateWorld(SEED, FOREST_DENSITY, FOREST_SIZE, TREE_SCALE, GRASS_AMOUNT, TREE_AMOUNT); 
const SPEED = 30;

async function main() {
  // Get A WebGL context
  /** @type {HTMLCanvasElement} */
  const canvas = document.querySelector("#canvas");
  const gl = canvas.getContext("webgl2");
  if (!gl) {
    return;
  }

  fitToContainer(canvas);

  // Tell the twgl to match position with a_position etc..
  twgl.setAttributePrefix("a_");

  const vs = `
    #version 300 es
    in vec4 a_position;
    in vec3 a_normal;

    uniform mat4 u_projection;
    uniform mat4 u_view;
    uniform mat4 u_world;
    uniform mat4 u_lightWorldViewProjection;

    out vec3 v_normal;
    out vec4 v_shadowCoord;

    void main() {   
        // Apply world transformation to the vertex
        vec4 worldPosition = u_world * a_position;
        
        // Calculate the final position of the vertex
        gl_Position = u_projection * u_view * worldPosition;

        // Pass the normal, transformed to world space
        v_normal = mat3(u_world) * a_normal;

        // Calculate shadow coordinates using global world position
        v_shadowCoord = u_lightWorldViewProjection * worldPosition;
    }
    `;
    const fs = `
    #version 300 es
    precision highp float;

    in vec3 v_normal;
    in vec4 v_shadowCoord;

    uniform vec4 u_diffuse;
    uniform vec3 u_reverseLightDirection;
    uniform vec3 u_ambientLight;
    uniform sampler2D u_shadowMap; 

    out vec4 outColor;

    float getShadow(vec4 shadowCoord) {
        vec3 projCoords = shadowCoord.xyz / shadowCoord.w;
        projCoords = projCoords * 0.5 + 0.5;  // Transform to [0, 1] range

        if (projCoords.x < 0.0 || projCoords.x > 1.0 || projCoords.y < 0.0 || projCoords.y > 1.0) {
            return 1.0;
        }

        float currentDepth = projCoords.z - 0.0006;

        float shadow = 0.0;
        vec2 texelSize = 1.0 / vec2(textureSize(u_shadowMap, 0)); 

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                float pcfDepth = texture(u_shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
                shadow += currentDepth > pcfDepth ? 0.5 : 1.0;
            }
        }
        shadow /= 25.0;

        return shadow;
    }



    void main () {
        vec3 normal = normalize(v_normal);
        vec3 lightDir = normalize(u_reverseLightDirection);

        // Diffuse lighting with a softening factor
        float diffuse = max(dot(normal, lightDir), 0.0) * 0.3 + 0.5;

        // Combine=ing ambient and diffuse lighting
        vec3 color = u_diffuse.rgb * (u_ambientLight + diffuse);

        // Calculate shadow
        float shadow = getShadow(v_shadowCoord);

        outColor = vec4(color * shadow, u_diffuse.a);
    }
    `;

    const shadowVs = `
    #version 300 es
    in vec4 a_position;

    uniform mat4 u_world;
    uniform mat4 u_lightViewProjection;

    void main() {
    gl_Position = u_lightViewProjection * u_world * a_position;
    }

    `; 
    const shadowFs = `
    #version 300 es
    precision highp float;

    void main() {
    // No output needed, depth is automatically written to the depth buffer
    }
    `; 

  // compiles and links the shaders, looks up attribute and uniform locations
  const meshProgramInfo = twgl.createProgramInfo(gl, [vs, fs]);
  const shadowProgramInfo = twgl.createProgramInfo(gl, [shadowVs, shadowFs]);

  const objFileName = 'models/grass.obj';  
  const mtlFileName = 'models/grass.mtl';  
  const texFileName = 'textures/Texture.png';  

  const treeFileName = 'models/Tree.obj';  
  const treeMtlFileName = 'models/Tree.mtl';  
  const treeTexFileName = 'textures/Texture.png';  

  const Tree02FileName = 'models/Tree02.obj';  
  const Tree02MtlFileName = 'models/Tree02.mtl';  
  const Tree02TexFileName = 'textures/Texture.png';
  
  const Tree03FileName = 'models/Tree03.obj';  
  const Tree03MtlFileName = 'models/Tree03.mtl';  
  const Tree03TexFileName = 'textures/Texture.png';  

  const rockFileName = 'models/rock.obj';  
  const rockMtlFileName = 'models/rock.mtl';  
  const rockTexFileName = 'textures/Texture.png';  

  async function loadFile(file) {
    const response = await fetch(file);
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.text();
  }

  const [objText, mtlText, treeText, treeMtlText, Tree02Text, Tree02MtlText, rockText, rockMtlText, Tree03Text,Tree03MtlText] = await Promise.all([
    loadFile(objFileName),
    loadFile(mtlFileName),
    loadFile(treeFileName),
    loadFile(treeMtlFileName),
    loadFile(Tree02FileName),
    loadFile(Tree02MtlFileName),
    loadFile(rockFileName),
    loadFile(rockMtlFileName),
    loadFile(Tree03FileName),
    loadFile(Tree03MtlFileName),
  ]);

  const obj = parseOBJ(objText);
  const materials = parseMTL(mtlText);

  const treeObj = parseOBJ(treeText);
  const treeMaterials = parseMTL(treeMtlText);

  const tree02Obj = parseOBJ(Tree02Text);
  const tree02Materials = parseMTL(Tree02MtlText);

  const rockObj = parseOBJ(rockText);
  const rockMaterials = parseMTL(rockMtlText);

  const tree03Obj = parseOBJ(Tree03Text);
  const tree03Materials = parseMTL(Tree03MtlText);

  const textures = {
    defaultWhite: twgl.createTexture(gl, {src: [255, 255, 255, 255]}),
    defaultGreen: twgl.createTexture(gl, {src: [3, 46, 15, 255]})
  };

  function loadTexture(objectMaterial, source, textures, gl) {
    for (const material of Object.values(objectMaterial)) {
      Object.entries(material)
        .filter(([key]) => key.endsWith('Map'))
        .forEach(([key, filename]) => {
          let texture = textures[filename];
          if (!texture) {
            texture = twgl.createTexture(gl, {src: source, flipY: true});
            textures[filename] = texture;
          }
          material[key] = texture;
        });
    }
  }

  loadTexture(materials, texFileName, textures, gl);
  loadTexture(treeMaterials, treeTexFileName, textures, gl);
  loadTexture(tree02Materials, Tree02TexFileName, textures, gl);
  loadTexture(rockMaterials, rockTexFileName, textures, gl);
  loadTexture(tree03Materials, Tree03TexFileName, textures, gl);

  const defaultMaterial = {
    diffuse: [1, 1, 1],
    diffuseMap: textures.defaultGreen,
    ambient: [0, 0, 0],
    specular: [1, 1, 1],
    specularMap: textures.defaultGreen,
    shininess: 200,
    opacity: 1,
  };

  const groundMaterial = {
    diffuse: [1, 1, 1],
    diffuseMap: twgl.createTexture(gl, { src: 'textures/grass.jpg' }),
    ambient: [1, 1, 1],
    specular: [1, 1, 1],
    shininess: 1,
    opacity: 1,
  };

  function generateGround(gl, program, forestSize, numSegments = 64) {
    const positions = [];
    const texcoords = [];
    const normals = [];
    const indices = [];
    
    // Adiciona o centro do círculo
    positions.push(0, 0, 0); // x, y, z
    texcoords.push(0.5, 0.5); // Center texcoord
    normals.push(0, 1, 0); // normal
    
    // Gera os vértices para o círculo
    for (let i = 0; i <= numSegments; i++) {
      const angle = i * 2 * Math.PI / numSegments;
      const x = Math.cos(angle) * forestSize * 2;
      const z = Math.sin(angle) * forestSize * 2;
      
      positions.push(x, 0, z);
      texcoords.push((x / (2 * forestSize)) + 0.5, (z / (2 * forestSize)) + 0.5);
      normals.push(0, 1, 0);
    }
    
    // Gera os índices para os triangulos do círculo
    for (let i = 1; i <= numSegments; i++) {
      indices.push(0, i, i + 1);
    }
    
    const arrays = {
      position: { numComponents: 3, data: positions },
      texcoord: { numComponents: 2, data: texcoords },
      normal: { numComponents: 3, data: normals },
      indices: { numComponents: 3, data: indices }
    };
    
    const bufferInfo = twgl.createBufferInfoFromArrays(gl, arrays);
    const vao = twgl.createVAOFromBufferInfo(gl, program, bufferInfo);
    
    return {
      bufferInfo: bufferInfo,
      vao: vao
    };
  }

  function updateTreeAmountValue() {
    const slider = document.getElementById('treeAmountSlider');
    const sliderValue = slider.value;

    return parseInt(sliderValue);
  }

  function updateForestDensityValue() {
    const slider = document.getElementById('objectCountSlider');
    const sliderValue = slider.value;

    return sliderValue;
  }

  function updateForestSizeValue() {
    const slider = document.getElementById('forestSizeSlider');
    const sliderValue = slider.value;

    return sliderValue;
  }

  function updateTreeScaleValue() {
    const slider = document.getElementById('treeScaleSlider');
    const sliderValue = slider.value;

    return parseFloat(sliderValue);
  }

  function updateGrassAmountValue() {
    const slider = document.getElementById('grassAmountSlider');
    const sliderValue = slider.value;

    return parseInt(sliderValue);
  }
  

  const groundObj = generateGround(gl, meshProgramInfo, FOREST_SIZE);

  function createParts(gl, programInfo, obj, materials) {
    return obj.geometries.map(({material, data}) => {
      if (data.color) {
        if (data.position.length === data.color.length) {
          data.color = {numComponents: 3, data: data.color};
        }
      } else {
        data.color = {value: [1, 1, 1, 1]};
      }
  
      const bufferInfo = twgl.createBufferInfoFromArrays(gl, data);
      const vao = twgl.createVAOFromBufferInfo(gl, programInfo, bufferInfo);
  
      return {
        material: {...defaultMaterial, ...materials[material]},
        bufferInfo,
        vao,
      };
    });
  }

  const grassParts = createParts(gl, meshProgramInfo, obj, materials);
  const treeParts = createParts(gl, meshProgramInfo, treeObj, treeMaterials);
  const tree02Parts = createParts(gl, meshProgramInfo, tree02Obj, tree02Materials);
  const rockParts = createParts(gl, meshProgramInfo, rockObj, rockMaterials);
  const tree03Parts = createParts(gl, meshProgramInfo, tree03Obj, tree03Materials);


  let cameraPosition = [2, 10, 100];
  let cameraDirection = [0, 0, -1];
  

  canvas.addEventListener('mousemove', (event) => {
    cameraDirection = updateCameraDirection(event, cameraDirection);
  });

  document.getElementById('seedButton').addEventListener('click', function() {
    const seed = document.getElementById('inputBox').value;

    FOREST_SIZE = updateForestSizeValue();
    FOREST_DENSITY = updateForestDensityValue();
    TREE_SCALE = updateTreeScaleValue();
    GRASS_AMOUNT = updateGrassAmountValue();
    TREE_AMOUNT = updateTreeAmountValue();  
    WORLD_DATA = generateWorld(seed, FOREST_DENSITY, FOREST_SIZE, TREE_SCALE, GRASS_AMOUNT, TREE_AMOUNT);
  });


  function degToRad(deg) {
    return deg * Math.PI / 180;
  }


  let then = 0;
  function render(time) {
    time *= 0.001;  // convert to seconds
  
    twgl.resizeCanvasToDisplaySize(gl.canvas);
    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height);
    gl.enable(gl.DEPTH_TEST);
    gl.enable(gl.CULL_FACE);


    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
    gl.clearColor(174/255, 198/255, 207/255, 1);
    
    
    const deltaTime = time - then;
    then = time;

    cameraPosition = updateCameraPosition(deltaTime, cameraDirection, cameraPosition);
  
    const aspect = gl.canvas.clientWidth / gl.canvas.clientHeight;
    const projection = m4.perspective(fieldOfViewRadians, aspect, zNear, zFar);

    // Compute the camera's matrix using look at.
    const camera = m4.lookAt(cameraPosition, m4.addVectors(cameraPosition, cameraDirection), up);
  
    // Make a view matrix from the camera matrix.
    const view = m4.inverse(camera);
  
    const sharedUniforms = {
      u_lightDirection: m4.normalize([1, 0, 5]),
      u_view: view,
      u_projection: projection,
    };
  
    gl.useProgram(meshProgramInfo.program);
  
    // calls gl.uniform
    twgl.setUniforms(meshProgramInfo, sharedUniforms);
  
    // Set the transformation matrix for the ground
    let groundWorldMatrix = m4.identity();
    groundWorldMatrix = m4.xRotate(groundWorldMatrix, degToRad(180));

    gl.bindVertexArray(groundObj.vao);

    twgl.setUniforms(meshProgramInfo, {
      u_world: groundWorldMatrix,
    }, groundMaterial);

    twgl.drawBufferInfo(gl, groundObj.bufferInfo);
  
    for (const { position, scale, type } of WORLD_DATA) {
      let parts;
  
      if (type === 0) {
        parts = grassParts;
      } else if (type === 1) {
        parts = treeParts;
      } else if (type === 2) {
        parts = tree02Parts;
      } else if (type === 3) {
        parts = rockParts;
      } else {
        parts = tree03Parts;
      }
  
      for (const { bufferInfo, vao, material } of parts) {
        // set the attributes for this part.
        gl.bindVertexArray(vao);
  
        let u_world = m4.identity();
        u_world = m4.translate(u_world, ...position.slice(0, 3));
        u_world = m4.scale(u_world, scale, scale, scale);
  
        twgl.setUniforms(meshProgramInfo, {
          u_world,
        }, material);
  
        // calls gl.drawArrays or gl.drawElements
        twgl.drawBufferInfo(gl, bufferInfo);
      }
    }
    requestAnimationFrame(render);
  }
  
  requestAnimationFrame(render);
}  

// Função que gera um número aleatório com base em um seed
function createSeededRandomGenerator(seed) {
  // Parâmetros do LCG conhecidos por gerar boas sequências
  const a = 1664525;
  const c = 1013904223;
  const m = 4294967296; // 2^32

  function nextSeed(seed) {
    return (a * seed + c) % m;
  }

  function random() {
    seed = nextSeed(seed);
    return seed / m;
  }

  return random;
}

// Verifica se a posição de uma árvore é válida em relação a outros objetos no canvas
function isTreePositionValid(newX, newZ, objectData, TREE_AMOUNT, canvasSize) {
  const baseMinDistance = 10;
  const minDistance = Math.max(2, baseMinDistance - TREE_AMOUNT / 2);

  function isWithinBounds(x, z, canvasSize) {
    const margin = minDistance;
    return x >= margin && x <= (canvasSize - margin) &&
           z >= margin && z <= (canvasSize - margin);
  }

  for (const obj of objectData) {
    if (obj.type === 1 || obj.type === 2 || obj.type === 4) {  
      const existingX = obj.position[0];
      const existingZ = obj.position[2];

      const distanceSq = (newX - existingX) ** 2 + (newZ - existingZ) ** 2;
      if (distanceSq < minDistance ** 2) {
        return false;
      }
    }
  }
  return true; 
}


//Função que gera os objetos no canvas com base em um seed e parâmetros de objetos
function generateWorld(seed, FOREST_DENSITY, FOREST_SIZE, TREE_SCALE, GRASS_AMOUNT, TREE_AMOUNT) {

  FOREST_DENSITY = FOREST_DENSITY * FOREST_SIZE / 40;
  GRASS_AMOUNT = GRASS_AMOUNT * FOREST_SIZE / 40;

  const random = createSeededRandomGenerator(seed);
  const objectData = [];
  const maxAttempts = 100; 

  for (let i = 0; i < FOREST_DENSITY; i++) {
    let type;
    let x, z, scale;
    let attempts = 0;

    do {
      //Aleatóriamente escolhe o tipo de objeto a ser gerado (0 = grama, 1,2,4 = Árvore, 3 = pedra)
        type = random() < 0.66 ? 0 : Math.floor(random() * 4) + 1;
        x = (random() - 0.5) * FOREST_SIZE;
        z = (random() - 0.5) * FOREST_SIZE;

        if (type === 3) {
            scale = random() * 0.6 + 0.1;
        } else if (type === 0) {
            scale = random() * 0.5 + 1;
        } else {
            scale = random() * 0.8 + TREE_SCALE;
        }
        // Verifica se a posição da árvore é válida
        if ((type !== 0 && type !== 3) && !isTreePositionValid(x, z, objectData, TREE_AMOUNT)) {
            type = undefined; 
        }

        attempts++;
    } while (type === undefined && attempts < maxAttempts);

    if (type !== undefined) {
        objectData.push({ position: [x, 0, z, 1], scale, type });
    } else {
        console.warn(`Failed to place object ${i + 1} after ${maxAttempts} attempts.`);
    }
  }

  for (let i = 0; i < GRASS_AMOUNT * 100; i++) {
    let x = (random() - 0.5) * FOREST_SIZE;
    let z = (random() - 0.5) * FOREST_SIZE;
    let scale = random() * 0.5 + 1;

    objectData.push({ position: [x, 0, z, 1], scale, type: 0 });
  }

  return objectData;
}

function fitToContainer(canvas){
  // Make it visually fill the positioned parent
  canvas.style.width ='100%';
  canvas.style.height='100%';
  // ...then set the internal size to match
  canvas.width  = canvas.offsetWidth;
  canvas.height = canvas.offsetHeight;
}

main();
