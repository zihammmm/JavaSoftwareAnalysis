package logger

import base.ILogger

object DefaultLogger: ILogger {
    override fun v(tag: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun d(tag: String, msg: String) {
        println("[$tag/d]: $msg")
    }

    override fun i(tag: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun e(tag: String, msg: String) {
        TODO("Not yet implemented")
    }
}