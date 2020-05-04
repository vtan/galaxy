package galaxy

import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11C._
import org.lwjgl.opengl.GL12C._
import org.lwjgl.opengl.GL13C._
import org.lwjgl.opengl.GL14C._
import org.lwjgl.opengl.GL15C._
import org.lwjgl.opengl.GL20C._
import org.lwjgl.opengl.GL21C._
import org.lwjgl.opengl.GL30C._
import org.lwjgl.opengl.GL31C._
import org.lwjgl.opengl.GL32C._
import org.lwjgl.opengl.GL33C._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._
import scala.io.Source

object Main {
  private val screenWidth = 1728
  private val screenHeight = 972

  def main(args: Array[String]): Unit = {
    val window = createWindow()
    GL.createCapabilities()
    render(window)

    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def createWindow(): Long = {
    GLFWErrorCallback.createPrint(System.err).set
    if (!glfwInit) {
      throw new IllegalStateException("Unable to initialize GLFW")
    }
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

    val window = glfwCreateWindow(screenWidth, screenHeight, "Hello World!", NULL, NULL)
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window")
    }
    glfwSetKeyCallback(window, (window, key, _, action, _) =>
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true)
      }
    )

    val stack = stackPush
    try {
      val pWidth = stack.mallocInt(1)
      val pHeight = stack.mallocInt(1)
      glfwGetWindowSize(window, pWidth, pHeight)
      val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
      val x = (vidmode.width - pWidth.get(0)) / 2
      val y = (vidmode.height - pHeight.get(0)) / 2
      glfwSetWindowPos(window, x, y)
    } finally {
      if (stack != null) stack.close()
    }

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)

    window
  }

  private def loadShaderProgram(vertexShaderPath: String, fragmentShdaderPath: String): Int = {
    val vertexShader = loadShader(vertexShaderPath, GL_VERTEX_SHADER)
    val fragmentShader = loadShader(fragmentShdaderPath, GL_FRAGMENT_SHADER)

    val program = glCreateProgram()
    glAttachShader(program, vertexShader)
    glAttachShader(program, fragmentShader)
    glLinkProgram(program)

    val status = Array(0)
    glGetProgramiv(program, GL_LINK_STATUS, status)
    if (status.head == 0) {
      val log = glGetProgramInfoLog(program)
      throw new RuntimeException(s"Failed to link program ($vertexShaderPath, $fragmentShdaderPath): $log")
    } else {
      glDeleteShader(vertexShader)
      glDeleteShader(fragmentShader)

      program
    }
  }

  private def loadShader(path: String, shaderType: Int): Int = {
    val shader = glCreateShader(shaderType)
    val source = Source.fromFile(path).mkString
    glShaderSource(shader, source)
    glCompileShader(shader)

    val status = Array(0)
    glGetShaderiv(shader, GL_COMPILE_STATUS, status)
    if (status.head == 0) {
      val log = glGetShaderInfoLog(shader)
      throw new RuntimeException(s"Failed to compile shader $path: $log")
    } else {
      shader
    }
  }

  private def render(window: Long): Unit = {
    val projection = M44.ortho(0, screenWidth, screenHeight, 0, -1, 1)
    val model = M44.translation(200, 100, 0) * M44.scale(400, 300, 1)
    val vertices: Array[Float] = Array(
      0.0f, 1.0f,
      1.0f, 0.0f,
      0.0f, 0.0f,
      0.0f, 1.0f,
      1.0f, 1.0f,
      1.0f, 0.0f
    )
    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    glBindVertexArray(vao)
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, NULL)
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)

    val shader = loadShaderProgram(
      "assets/shaders/rectangle.vert.glsl",
      "assets/shaders/rectangle.frag.glsl"
    )
    val colorUniform = glGetUniformLocation(shader, "color")
    val mvpUniform = glGetUniformLocation(shader, "mvp")

    glUseProgram(shader)
    glUniform4f(colorUniform, 0, 0.5f, 1, 1)
    glUniformMatrix4fv(mvpUniform, true, (projection * model).array)

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

      glBindVertexArray(vao)
      glDrawArrays(GL_TRIANGLES, 0, 6)
      glBindVertexArray(0)

      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }
}