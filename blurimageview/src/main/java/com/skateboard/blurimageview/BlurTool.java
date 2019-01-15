package com.skateboard.blurimageview;

import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;

public class BlurTool {
    static float pos[] = {-1.0f, -1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f, 0.0f,
            -1.0f, -1.0f, 0.0f, 1.0f,
            1.0f, -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 0.0f};

    public static void parapre(@NotNull String vertex, @NotNull String fragment, @NotNull Bitmap bitmap, int width, int height) {
        generateProgram(vertex, fragment);
        prepareVertex();
        prepareTexture(bitmap);
//        prepareFrameBuffer(width, height);
    }

//    void prepareFrameBuffer(int width, int height) {
//
//        glGenFramebuffers(2, FBUFFERS);
//        glGenTextures(2, FBUFFERTEXTURE);
//        for (int i = 0; i < 2; i++) {
//            glBindFramebuffer(GL_FRAMEBUFFER, FBUFFERS[i]);
//            glBindTexture(GL_TEXTURE_2D, FBUFFERTEXTURE[i]);
//            glTexImage2D(
//                    GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL
//            );
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
//            glGenerateMipmap(GL_TEXTURE_2D);
//            glFramebufferTexture2D(
//                    GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, FBUFFERTEXTURE[i], 0
//            );
//            if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
//                LOGE("frame buffer not completed");
//            }
//        }
//
//    }


//    private static void prepareFrameBuffer(int width, int height, int[] FBUFFERTEXTURE) {
//
//        int frameBuffer[] = new int[2];
//        int texture[] = new int[2];
//        GLES30.glGenFramebuffers(2, frameBuffer, 0);
//        GLES30.glGenTextures(2, texture,0);
//        for (int i = 0; i < 2; i++) {
//            GLES30.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[i]);
//            GLES20.glBindTexture(GLES10.GL_TEXTURE_2D, FBUFFERTEXTURE[i]);
//            GLES10.glTexImage2D(
//                    GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GL_UNSIGNED_BYTE, null
//            );
//            GLES20.glTexParameteri(GL_TEXTURE_2D, GLES10.GL_TEXTURE_MIN_FILTER, GLES10.GL_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
//            glGenerateMipmap(GL_TEXTURE_2D);
//            glFramebufferTexture2D(
//                    GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, FBUFFERTEXTURE[i], 0
//            );
//            if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
//                LOGE("frame buffer not completed");
//            }
//        }
//    }


    private static void prepareTexture(Bitmap bitmap) {
        int[] texture = new int[1];
        GLES30.glGenTextures(1, texture, 0);
        GLES30.glBindTexture(GL_TEXTURE_2D, texture[0]);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_R, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glGenerateMipmap(GL_TEXTURE_2D);

        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

    }


    private static void generateProgram(String vertex, String fragment) {

        int vertexShader = createShader(vertex, GLES30.GL_VERTEX_SHADER);
        int fragmentShader = createShader(fragment, GLES30.GL_FRAGMENT_SHADER);

        int program = GLES30.glCreateProgram();

        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glLinkProgram(program);

        checkGLError("program");

    }


    static void prepareVertex() {
        int[] VAO = new int[1];
        int[] VBO = new int[1];

        GLES30.glGenVertexArrays(1, VAO, 0);
        GLES30.glBindVertexArray(VAO[0]);


        GLES30.glGenBuffers(1, VBO, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, pos.length * 4, arrayToBuffer(pos), GL_STATIC_DRAW);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 4 * 4, 0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 4 * 4, 2 * 4);
        GLES30.glBindVertexArray(0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        checkGLError("prepareVertex");


    }

    static Buffer arrayToBuffer(float[] pos) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(pos.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = buffer.asFloatBuffer();
        floatBuffer.put(pos);
        floatBuffer.position(0);
        return floatBuffer;

    }


    private static int createShader(String sourceCode, int shaderType) {
        int shaderId = GLES30.glCreateShader(shaderType);
        GLES30.glShaderSource(shaderId, sourceCode);
        GLES30.glCompileShader(shaderId);

        checkGLError("createShader");

        return shaderId;
    }


    public static void checkGLError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("MyApp", op + ": glError " + error);
        }
    }
}
