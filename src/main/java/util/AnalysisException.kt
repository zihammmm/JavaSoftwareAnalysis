package util

class AnalysisException : RuntimeException {
    constructor()

    constructor(msg: String) : super(msg)

    constructor(t: Throwable) : super(t)

    constructor(msg: String, t: Throwable) : super(msg, t)
}