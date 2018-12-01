import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.itheima.kotlin.game.core.Window

class MyWindow : Window() {

    override fun onCreate() {
        println("窗体创建")
    }

    override fun onDisplay() {
        println("窗体展示")
    }

    override fun onKeyPressed(event: KeyEvent) {
        when (event.code) {
            KeyCode.ENTER -> println("enter 按键")

        }

    }

    override fun onRefresh() {
        println("刷新")
    }



}

fun main(args: Array<String>) {
    Application.launch(MyWindow::class.java)
}