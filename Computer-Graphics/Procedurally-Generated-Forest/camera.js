let cameraYaw = 0;
let cameraPitch = 0;
let sensitivity = 0.002;
let up = [0, 1, 0];
let keysPressed = {};
let mouseDown = false;
let lastMouseX = 0;
let lastMouseY = 0;

document.addEventListener('keydown', (event) => {
    keysPressed[event.key] = true;
});

document.addEventListener('keyup', (event) => {
    keysPressed[event.key] = false;
});

function updateCameraPosition(deltaTime, cameraDirection, cameraPosition) {
    const right = m4.normalize(m4.cross(up, cameraDirection));

    if (keysPressed['w']) {
        cameraPosition = m4.addVectors(cameraPosition, m4.scaleVector(cameraDirection, SPEED * deltaTime));
    }
    if (keysPressed['s']) {
        cameraPosition = m4.addVectors(cameraPosition, m4.scaleVector(cameraDirection, -SPEED * deltaTime));
    }
    if (keysPressed['d']) {
        cameraPosition = m4.addVectors(cameraPosition, m4.scaleVector(right, -SPEED * deltaTime));
    }
    if (keysPressed['a']) {
        cameraPosition = m4.addVectors(cameraPosition, m4.scaleVector(right, SPEED * deltaTime));
    }
    return cameraPosition;
}

canvas.addEventListener('mousedown', (event) => {
    mouseDown = true;
    lastMouseX = event.clientX;
    lastMouseY = event.clientY;
});

canvas.addEventListener('mouseup', (event) => {
    mouseDown = false;
});

function updateCameraDirection(event, cameraDirection) {
    if (mouseDown) {
        const deltaX = event.clientX - lastMouseX;
        const deltaY = event.clientY - lastMouseY;
        lastMouseX = event.clientX;
        lastMouseY = event.clientY;

        cameraYaw -= deltaX * sensitivity;
        cameraPitch -= deltaY * sensitivity;

        cameraPitch = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, cameraPitch));

        const yawMatrix = m4.yRotation(cameraYaw);
        const pitchMatrix = m4.xRotation(cameraPitch);
        const combinedMatrix = m4.multiply(yawMatrix, pitchMatrix);
        cameraDirection = m4.transformDirection(combinedMatrix, [0, -0.3, -1]);
    }

    return cameraDirection;
}

function degToRad(deg) {
    return deg * Math.PI / 180;
  }

var fieldOfViewRadians = degToRad(60);
const zNear = 1;  // Set near clipping plane to a small positive value
const zFar = 2000;   // Far clipping plane