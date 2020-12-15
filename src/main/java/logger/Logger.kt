package logger

import base.ILogger

object Logger {
    private var logger: ILogger = DefaultLogger

    fun init(logger: ILogger) {
        this.logger = logger
    }

    fun v(tag: String, msg: String) = logger.v(tag, msg)

    fun d(tag: String, msg: String) = logger.d(tag, msg)

    fun i(tag: String, msg: String) = logger.i(tag, msg)

    fun e(tag: String, msg: String) = logger.e(tag, msg)

}