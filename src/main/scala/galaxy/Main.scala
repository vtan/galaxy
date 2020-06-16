package galaxy

import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.nanovg.NanoVGGL3._
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

object Main {
  private val screenWidth = 1728
  private val screenHeight = 972
  private var nvg: Long = 0

  def main(args: Array[String]): Unit = {
    val window = createWindow()

    GL.createCapabilities()
    nvg = nvgCreate(NVG_ANTIALIAS)
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

  private def render(window: Long): Unit = {
    val renderer = new Renderer(
      screenSize = V2(screenWidth.toDouble, screenHeight.toDouble),
      nvg
    )

    var lastFrameTime = 0.0
    var fpsWindowTotalTime = 0.0
    var fpsWindowLength = 0

    while (!glfwWindowShouldClose(window)) {
      renderer.render()

      glfwSwapBuffers(window)
      glfwPollEvents()

      val now = glfwGetTime()
      fpsWindowTotalTime += now - lastFrameTime
      lastFrameTime = now
      fpsWindowLength += 1

      if (fpsWindowLength == 16) {
        val fps = fpsWindowLength / fpsWindowTotalTime
        glfwSetWindowTitle(window, String.format("FPS: %.0f", fps))
        fpsWindowTotalTime = 0.0
        fpsWindowLength = 0
      }
    }
  }
}