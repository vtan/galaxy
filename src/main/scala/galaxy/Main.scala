package galaxy

import galaxy.common.V2
import galaxy.game.{AppState, GameState, Renderer, UiState}
import galaxy.rendering._

import java.util.concurrent.atomic.AtomicReference
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.nanovg.NanoVGGL3._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

object Main {
  private val requestedWindowSize = V2(1728, 972)

  private val eventsRef = new AtomicReference(List.empty[GlfwEvent])
  private val lastCursorPosition = new AtomicReference(V2[Double](0, 0))

  def main(args: Array[String]): Unit = {
    val window = createWindow()

    glfwSetCursorPosCallback(window, (_, x, y) => {
      val newPosition = V2(x, y)
      val V2(xPrev, yPrev) = lastCursorPosition.getAndSet(newPosition)
      val _ = eventsRef.updateAndGet(CursorPositionEvent(x, y, x - xPrev, y - yPrev) :: _)
    })
    glfwSetMouseButtonCallback(window, (_, button, action, modifiers) => {
      val V2(x, y) = lastCursorPosition.get
      val _ = eventsRef.updateAndGet(MouseButtonEvent(button, action, modifiers, x, y) :: _)
    })
    glfwSetScrollCallback(window, (_, x, y) => {
      val _ = eventsRef.updateAndGet(ScrollEvent(x, y) :: _)
    })

    GL.createCapabilities()
    val nvg = nvgCreate(NVG_ANTIALIAS)
    render(window, nvg)

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

    val window = glfwCreateWindow(requestedWindowSize.x, requestedWindowSize.y, "Hello World!", NULL, NULL)
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

  private def render(window: Long, nvg: Long): Unit = {
    val font = nvgCreateFont(nvg, "Roboto", "assets/roboto/Roboto-Regular.ttf")
    nvgFontFaceId(nvg, font)

    val windowWidth = Array(0)
    val windowHeight = Array(0)
    val framebufferWidth = Array(0)
    val framebufferHeight = Array(0)
    glfwGetWindowSize(window, windowWidth, windowHeight)
    glfwGetFramebufferSize(window, framebufferWidth, framebufferHeight)
    val pixelRatio = framebufferWidth(0).toFloat / windowWidth(0).toFloat

    val screenSize = V2(windowWidth(0).toFloat, windowHeight(0).toFloat)
    val uiFramebuffer = nvgluCreateFramebuffer(nvg, framebufferWidth(0), framebufferHeight(0), 0)

    val appState = AppState(
      gameState = GameState.initial,
      uiState = UiState.initial(screenSize.map(_.toDouble))
    )
    var renderContext = RenderContext(
      nvg = nvg,
      pixelRatio = pixelRatio,
      screenSize = screenSize,
      appState = appState,
      dispatchedUpdates = List.empty,
      events = List.empty,
      layoutContext = LayoutContext.initial,
      uiFramebuffer = uiFramebuffer
    )

    var lastFrameTime = 0.0
    var fpsWindowTotalTime = 0.0
    var fpsWindowLength = 0

    while (!glfwWindowShouldClose(window)) {
      val events = eventsRef.getAndSet(Nil).reverse
      renderContext = renderContext.beginFrame(events)
      Renderer.render()(renderContext)

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