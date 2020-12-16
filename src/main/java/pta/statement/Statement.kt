package pta.statement

interface Statement {

    val kind: Kind

    enum class Kind{
        ALLOCATION,
        ASSIGN,
        INSTANCE_LOAD,
        INSTANCE_STORE,
        CALL
    }
}